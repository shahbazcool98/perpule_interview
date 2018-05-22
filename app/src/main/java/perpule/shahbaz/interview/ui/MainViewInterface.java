package perpule.shahbaz.interview.ui;

import java.util.List;

import perpule.shahbaz.interview.models.Mp3;

public interface MainViewInterface {

    void showToast(String s);
    void displayMp3s(List<Mp3> mp3List);
    void displayError(String s);

}
