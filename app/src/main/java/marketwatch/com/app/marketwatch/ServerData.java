package marketwatch.com.app.marketwatch;

import android.content.Context;
import android.net.ConnectivityManager;

import java.util.List;

/**
 * Created by Pratik on 09-03-2018.
 */

public class ServerData {
    static ServerData serverData;
    static List<NewsData>setdata;
    ServerData(){

        if(serverData==null){
            serverData=new ServerData();
        }


    }

    public static List<NewsData> getSetdata() {
        return setdata;
    }

    public static void setSetdata(List<NewsData> setdata) {
        ServerData.setdata = setdata;
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
