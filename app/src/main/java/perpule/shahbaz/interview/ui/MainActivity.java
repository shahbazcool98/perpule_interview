package perpule.shahbaz.interview.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import perpule.shahbaz.interview.R;
import perpule.shahbaz.interview.adapters.Mp3sAdapter;
import perpule.shahbaz.interview.models.Mp3;

public class MainActivity extends AppCompatActivity implements MainViewInterface {

    @BindView(R.id.rvMp3s)
    RecyclerView rvMp3s;

    private String TAG = "MainActivity";
    RecyclerView.Adapter adapter;
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupMVP();
        setupViews();
        getMp3List();
    }


    private void setupMVP() {
        mainPresenter = new MainPresenter(this, this);
    }

    private void setupViews(){
        rvMp3s.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getMp3List() {

        mainPresenter.getMp3s();

    }



    @Override
    public void showToast(String str) {
        Toast.makeText(MainActivity.this,str, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayMp3s(List<Mp3> mp3List) {
            adapter = new Mp3sAdapter(mp3List, MainActivity.this);
            rvMp3s.setAdapter(adapter);
    }

    @Override
    public void displayError(String e) {
        showToast(e);
    }

}
