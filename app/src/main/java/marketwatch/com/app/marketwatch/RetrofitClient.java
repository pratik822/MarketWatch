package marketwatch.com.app.marketwatch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pratikb on 07-03-2018.
 */

public class RetrofitClient {
    private static final String ROOT_URL = "https://marketwatchnew.000webhostapp.com/marketwatch/stockmarket/";
    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
      static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder().baseUrl(ROOT_URL).addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static RetrofitService getApiService() {
        return (RetrofitService) getRetrofitInstance().create(RetrofitService.class);
    }
}
