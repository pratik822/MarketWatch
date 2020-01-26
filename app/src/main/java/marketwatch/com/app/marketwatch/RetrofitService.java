package marketwatch.com.app.marketwatch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by pratikb on 07-03-2018.
 */

public interface RetrofitService {
    @Headers({
            "Accept: application/json",
    })
    @GET("getcreateData.php")
    Call<List<NewsData>> getData();

    @Headers("Content-Type: text/html")
    @FormUrlEncoded
    @POST("fcmTockengenrate.php")
    Call<String>sendTocken(@Field("fcmkey") String code);
}
