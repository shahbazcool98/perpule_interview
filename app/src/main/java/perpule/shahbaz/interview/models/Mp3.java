package perpule.shahbaz.interview.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mp3 {

    @SerializedName("itemId")
    @Expose
    private String itemId;

    @SerializedName("desc")
    @Expose
    private String desc;

    @SerializedName("audio")
    @Expose
    private String audio;


    public Mp3() {
    }

    public Mp3(String itemId, String desc, String audio) {
        super();
        this.itemId = itemId;
        this.desc = desc;
        this.audio = audio;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
