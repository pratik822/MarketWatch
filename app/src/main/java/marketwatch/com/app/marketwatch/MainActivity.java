package marketwatch.com.app.marketwatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recycleview;
    List<NewsData>datalist=new ArrayList<>();
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycleview=(RecyclerView)findViewById(R.id.recycleview);
        recycleview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recycleview.setLayoutManager(mLayoutManager);
        Adapter adapter=new Adapter(MainActivity.this,datalist);
        recycleview.setAdapter(adapter);
       RetrofitService service=RetrofitClient.getApiService();


        service.getData().enqueue(new Callback<List<NewsData>>() {
            @Override
            public void onResponse(Response<List<NewsData>> response, Retrofit retrofit) {
                Log.d("responce",new Gson().toJson(response));
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
