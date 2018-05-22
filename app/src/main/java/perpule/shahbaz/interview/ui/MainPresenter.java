package perpule.shahbaz.interview.ui;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import perpule.shahbaz.interview.models.Mp3;
import perpule.shahbaz.interview.utils.SPManager;

public class MainPresenter implements MainPresenterInterface {

    MainViewInterface mvi;
    private String TAG = "MainPresenter";

    Context context;

    SPManager spManager;

    public static List<Mp3> mp3List;
    public static int selectedPos = 0;

    public MainPresenter(MainViewInterface mvi, Context context) {
        this.mvi = mvi;
        this.context = context;

        spManager = new SPManager(context);
    }

    @Override
    public void getMp3s()
    {
        JsonArray mp3Array = spManager.getMp3Response();

        if(mp3Array!=null && mp3Array.size()>0)
        {
            mp3List = new ArrayList<>();
            for(JsonElement element : mp3Array)
            {
                JsonObject object = (JsonObject) element;
                try
                {
                    mp3List.add(new Mp3(object.get("itemId").getAsString(), object.get("desc").getAsString(),
                            object.get("audio").getAsString()));
                }
                catch (Exception e){}
            }

            mvi.displayMp3s(mp3List);
        }

    }

}
