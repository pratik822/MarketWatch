package marketwatch.com.app.marketwatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;


import io.fabric.sdk.android.Fabric;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        if (ServerData.isNetworkAvailable(Splash.this)) {
            getServerData();

        } else {
            Toast.makeText(Splash.this, "Internet Not available", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void getServerData() {
        RetrofitService service = RetrofitClient.getApiService();

        service.getData().enqueue(new Callback<List<NewsData>>() {
            @Override
            public void onResponse(Call<List<NewsData>> call, Response<List<NewsData>> response) {
                ServerData.setSetdata(response.body());
                if (ServerData.getSetdata().size() > 0) {
                    Intent main = new Intent(Splash.this, MainActivity.class);
                    startActivity(main);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<NewsData>> call, Throwable t) {

            }
        });


    }
}
