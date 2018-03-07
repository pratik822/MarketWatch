package marketwatch.com.app.marketwatch;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by pratikb on 07-03-2018.
 */

public class RetrofitClient {
    private static final String ROOT_URL = "http://pickoftheday.in/apps/v1.1/";
      static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder().baseUrl(ROOT_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static RetrofitService getApiService() {
        return (RetrofitService) getRetrofitInstance().create(RetrofitService.class);
    }
}
