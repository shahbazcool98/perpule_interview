package perpule.shahbaz.interview.models;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Mp3Response {

    @SerializedName("data")
    @Expose
    private JsonArray mp3s = null;

    public Mp3Response() {
    }


    public Mp3Response(JsonArray mp3s) {
        super();
        this.mp3s = mp3s;
    }


    public JsonArray getMp3s() {
        return mp3s;
    }

    public void setMp3s(JsonArray mp3s) {
        this.mp3s = mp3s;
    }
}
