package marketwatch.com.app.marketwatch;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pratikb on 07-03-2018.
 */

public class RetrofitClient {
    private static final String ROOT_URL = "https://marketwatchnew.000webhostapp.com/marketwatch/stockmarket/";
      static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder().baseUrl(ROOT_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static RetrofitService getApiService() {
        return (RetrofitService) getRetrofitInstance().create(RetrofitService.class);
    }
}
