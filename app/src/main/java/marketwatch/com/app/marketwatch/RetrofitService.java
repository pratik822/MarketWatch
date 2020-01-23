package marketwatch.com.app.marketwatch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by pratikb on 07-03-2018.
 */

public interface RetrofitService {
    @Headers({
            "Accept: application/json",
    })
    @GET("getcreateData.php")
    Call<List<NewsData>> getData();
}
