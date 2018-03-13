package marketwatch.com.app.marketwatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recycleview;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    List<NewsData> datalist = new ArrayList<>();
    List<NewsData> finaldatalist = new ArrayList<>();
    LinearLayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final String TAG = "MainActivity";
    Boolean isRefresh = false;
    boolean doubleBackToExitPressedOnce = false;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Market Watch</font>"));

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
       // Log.d(TAG, "Refreshed token: " + refreshedToken);
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(this);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if (ServerData.getSetdata().size() > 0) {
            setadapter();
            for (int i = 0; i < ServerData.getSetdata().size(); i++) {
                if (!ServerData.getSetdata().get(i).getBuyprice().equalsIgnoreCase("info") && !ServerData.getSetdata().get(i).getTarget1().equalsIgnoreCase("info")) {
                    finaldatalist.add(ServerData.getSetdata().get(i));
                }
            }

            if (finaldatalist.size() > 0) {
                Adapter adapter = new Adapter(MainActivity.this, finaldatalist);
                recycleview.setAdapter(adapter);

            }
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_chat:
                Intent pass=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(pass);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void setadapter() {
        recycleview = (RecyclerView) findViewById(R.id.recycleview);
        mLayoutManager = new LinearLayoutManager(this);
        recycleview.setLayoutManager(mLayoutManager);
        recycleview.setItemAnimator(new DefaultItemAnimator());


    }

    public void getServerData() {
        RetrofitService service = RetrofitClient.getApiService();
        service.getData().enqueue(new Callback<List<NewsData>>() {
            @Override
            public void onResponse(Response<List<NewsData>> response, Retrofit retrofit) {
                Log.d("responce", new Gson().toJson(response));
                datalist = response.body();

                for (int i = 0; i < datalist.size(); i++) {
                    if (!datalist.get(i).getBuyprice().equalsIgnoreCase("info") && !datalist.get(i).getTarget1().equalsIgnoreCase("info")) {
                        finaldatalist.add(datalist.get(i));
                    }
                }

                if (finaldatalist.size() > 0) {
                    Adapter adapter = new Adapter(MainActivity.this, finaldatalist);
                    recycleview.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                    if (isRefresh == true) {
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        getServerData();

    }
}
