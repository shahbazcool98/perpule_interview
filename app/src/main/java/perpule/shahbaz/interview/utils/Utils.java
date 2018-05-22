package perpule.shahbaz.interview.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.io.File;

import perpule.shahbaz.interview.R;

public class Utils {

    public static boolean isConnected(Context context){
        ConnectivityManager
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public static String getFileName(String url)
    {
        String fileName = "";
        try {
             fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
        }
        catch (Exception e){e.printStackTrace();}

        return fileName;
    }


    public static String getLocalFile(Context context , String url)
    {
        String localPath = null;

        try
        {
            String fileName = getFileName(url);
            localPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+context.getString(R.string.app_name)+"/"+fileName;

            File file = new File(localPath);

            if(!file.exists())
            {
                localPath = null;
            }

        }
        catch (Exception e){}

        return localPath;
    }

}
