package marketwatch.com.app.marketwatch;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by pratikb on 07-03-2018.
 */

public interface RetrofitService {
    @GET("MobileAPI.php?")
    Call<List<NewsData>> getData();
}
