package marketwatch.com.app.marketwatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Market Watch</font>"));

        Log.d("FCMToken", "token "+ FirebaseInstanceId.getInstance().getToken());
        queue = Volley.newRequestQueue(MainActivity.this);




        StringRequest jsonrequest = new StringRequest(Request.Method.POST, "http://marketwatchnew.000webhostapp.com/marketwatch/stockmarket/fcmTockengenrate.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this,response.toString(),Toast.LENGTH_LONG).show();


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getmyresponce", error.getMessage());
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("fcmkey",FirebaseInstanceId.getInstance().getToken());

                Log.d("getParams", params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                return header;
            }

        };

        queue.add(jsonrequest);



/*       RetrofitService service = RetrofitClient.getApiService();
        service.sendTocken(FirebaseInstanceId.getInstance().getToken()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("myresponce",response.body().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("myresponce",t.getMessage());
            }
        });*/

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
            public void onResponse(Call<List<NewsData>> call, Response<List<NewsData>> response) {
                Log.d("responce", new Gson().toJson(response));
                datalist = response.body();

                for (int i = 0; i < datalist.size(); i++) {
                    if (!datalist.get(i).getBuyprice().equalsIgnoreCase("info") && !datalist.get(i).getTarget1().equalsIgnoreCase("info")) {
                        finaldatalist.add(datalist.get(i));
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
            }

            @Override
            public void onFailure(Call<List<NewsData>> call, Throwable t) {

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
