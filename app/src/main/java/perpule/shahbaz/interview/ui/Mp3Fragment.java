package perpule.shahbaz.interview.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import perpule.shahbaz.interview.R;
import perpule.shahbaz.interview.models.Mp3;

public class Mp3Fragment extends Fragment implements Mp3FViewInterface {


    @BindView(R.id.tvDesc)
    TextView tvDesc;

    @BindView(R.id.btnRetry)
    Button btnRetry;

    @BindView(R.id.btnContinue)
    Button btnContinue;

    private String TAG = "Mp3Fragment";
    Mp3FPresenter mp3FPresenter;

    ProgressDialog  progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mp3, container, false);

        ButterKnife.bind(this, view);

        setupMVP();
        setupViews();
        init();

        return view;
    }



    private void setupMVP() {
        mp3FPresenter = new Mp3FPresenter(this, getActivity());
    }

    private void setupViews(){
       try {

           progressDialog = new ProgressDialog(getActivity(),
                   R.style.LoadDialogTheme);
           progressDialog.setIndeterminate(true);
           progressDialog.setMessage(getActivity().getString(R.string.downloading_str));
           progressDialog.setCancelable(false);

           btnRetry.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   init();
               }
           });

           btnContinue.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   MainPresenter.selectedPos += 1;
                    init();
               }
           });
       }
       catch (Exception e){
           Log.e(TAG, Log.getStackTraceString(e));}
    }

    private void setDesc(String track)
    {
        tvDesc.setText(track);
    }

    private void init()
    {
        mp3FPresenter.loadCurrentTrack();
    }

    @Override
    public void showProgress()
    {
        try {
            progressDialog.show();
        }
        catch (Exception e)
        {}
    }

    @Override
    public void hideProgres()
    {
       try {
           progressDialog.hide();
       }
       catch (Exception e){}
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(getActivity(),str, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlay(String desc) {
        setDesc(desc);
        btnRetry.setVisibility(View.GONE);
    }

    @Override
    public void displayError(String e) {
        btnRetry.setVisibility(View.VISIBLE);
        showToast(e);
    }

    @Override
    public void onStart() {
        super.onStart();
        mp3FPresenter.onViewAttached();
    }
    @Override
    public void onStop() {
        super.onStop();
        mp3FPresenter.onViewDetached();
    }


}
