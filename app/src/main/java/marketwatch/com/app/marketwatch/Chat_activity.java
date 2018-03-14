package marketwatch.com.app.marketwatch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chat_activity extends AppCompatActivity implements View.OnClickListener {
    private ImageView btnSend;
    private EditText edtMessage;
    private RecyclerView rvMessage;
    LinearLayoutManager linearLayoutManager;
    private AppPreference mAppPreference;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    TimeAgo ago;
    Date date;
    private FirebaseRecyclerAdapter<Message, ChatViewHolder> adapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    ProgressBar progress;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Discussion</font>"));

        setContentView(R.layout.activity_chat_activity);
        setupUI();

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        init();
        intiRecycle();
        date = new Date();
        ago = new TimeAgo(Chat_activity.this);
    }

    public void setupUI() {
        btnSend = (ImageView) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);

        edtMessage = (EditText) findViewById(R.id.edt_message);
        rvMessage = (RecyclerView) findViewById(R.id.rv_chat);
        progress=(ProgressBar)findViewById(R.id.progress);
        mAdView = (AdView) findViewById(R.id.adView);
        progress.setVisibility(View.VISIBLE);
        rvMessage.setHasFixedSize(true);
        linearLayoutManager =
                new LinearLayoutManager(Chat_activity.this, LinearLayoutManager.VERTICAL, false);
        rvMessage.setLayoutManager(linearLayoutManager);
        rvMessage.setItemAnimator(new DefaultItemAnimator());


    }

    public void init() {
        mAppPreference = new AppPreference(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    public void intiRecycle() {
        adapter = new FirebaseRecyclerAdapter<Message, ChatViewHolder>(
                Message.class,
                R.layout.item_row_chat,
                ChatViewHolder.class,
                mDatabaseReference.child("chat").limitToLast(400)
        ) {
            @Override
            protected void populateViewHolder(ChatViewHolder viewHolder, Message model, int position) {
                viewHolder.tvMessage.setText(model.message);
                viewHolder.tvEmail.setText(model.username);
                viewHolder.tv_ago.setText(ago.timeAgo(new Date(model.times)));
                Log.d("mydates", ago.timeAgo(new Date(model.times)));
                progress.setVisibility(View.GONE);
            }
        };

        rvMessage.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                rvMessage.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.getItemCount() > 0) {
                            rvMessage.smoothScrollToPosition(rvMessage.getAdapter().getItemCount() - 1);
                        }
                    }
                }, 500);
            }
        });

        rvMessage.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adapter.getItemCount() > 0) {
                    rvMessage.smoothScrollToPosition(rvMessage.getAdapter().getItemCount() - 1);
                }
            }
        }, 1000);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_send) {
            String message = edtMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(message)) {
                Map<String, Object> param = new HashMap<>();
                param.put("sender", mAppPreference.getEmail());
                param.put("message", message);
                param.put("username", mAppPreference.getusername());
                param.put("times", new Date().toString());

                edtMessage.setText("");

                mDatabaseReference.child("chat")
                        .push()
                        .setValue(param)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    rvMessage.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            rvMessage.smoothScrollToPosition(rvMessage.getAdapter().getItemCount() - 1);
                                        }
                                    }, 800);
                                    // linearLayoutManager.scrollToPosition(adapter.getItemCount() - 1);
                                    Log.d("SendMessage", "Sukses");
                                } else {
                                    Log.d("SendMessage", "failed ");
                                }
                            }
                        });
            }
        }
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmail, tvMessage, tv_ago;

        public ChatViewHolder(View itemView) {
            super(itemView);

            tvEmail = (TextView) itemView.findViewById(R.id.tv_sender);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
            tv_ago = (TextView) itemView.findViewById(R.id.tv_ago);

        }
    }
}
