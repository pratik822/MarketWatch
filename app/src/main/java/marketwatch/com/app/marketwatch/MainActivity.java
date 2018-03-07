package marketwatch.com.app.marketwatch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recycleview;
    List<NewsData> datalist = new ArrayList<>();
    List<NewsData> finaldatalist = new ArrayList<>();
    LinearLayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final String TAG = "MainActivity";

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getServerData();
        setadapter();

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
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
