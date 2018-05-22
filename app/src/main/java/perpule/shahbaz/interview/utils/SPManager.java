package perpule.shahbaz.interview.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SPManager {

    Context context;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String TAG = "SPManager";

    public SPManager(Context context)
    {
        this.context = context;

        pref = context.getApplicationContext().getSharedPreferences("PpShahbaz", 0); // 0 - for private mode
        editor = pref.edit();
    }

    public void setMp3Response(JsonArray data)
    {
        try
        {
            editor.putString("mp3Response", data.toString());
            editor.commit();
        }
        catch (Exception e){
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public JsonArray getMp3Response()
    {
        JsonArray data;

        try
        {
            data = new JsonParser().parse(pref.getString("mp3Response", null)).getAsJsonArray();
        }
        catch (Exception e)
        {
            data = null;
        }

        return data;
    }



}
