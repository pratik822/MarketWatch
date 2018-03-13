package marketwatch.com.app.marketwatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import io.fabric.sdk.android.Fabric;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
            public void onResponse(Response<List<NewsData>> response, Retrofit retrofit) {
             //   Log.d("responce", new Gson().toJson(response));
                ServerData.setSetdata(response.body());

              //  Log.d("responceserver", new Gson().toJson(ServerData.getSetdata()));

                if (ServerData.getSetdata().size() > 0) {
                    Intent main = new Intent(Splash.this, MainActivity.class);
                    startActivity(main);
                    finish();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();

            }
        });
    }
}
