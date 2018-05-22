package perpule.shahbaz.interview.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import perpule.shahbaz.interview.DownloadService;
import perpule.shahbaz.interview.R;
import perpule.shahbaz.interview.models.Mp3Response;
import perpule.shahbaz.interview.network.NetworkClient;
import perpule.shahbaz.interview.network.NetworkInterface;
import perpule.shahbaz.interview.utils.SPManager;
import perpule.shahbaz.interview.utils.Utils;

public class SplashPresenter implements SplashPresenterInterface {

    Context context;

    SPManager spManager;

    SplashViewInterface svi;
    String TAG = "SplashPresenter";

    public SplashPresenter(SplashViewInterface svi, Context context) {
        this.svi = svi;
        this.context = context;

        spManager = new SPManager(context);
    }


    @Override
    public void getMp3s()
    {
        if(spManager.getMp3Response()!=null)
        {
            return;
        }

        if(!Utils.isConnected(context))  // check internet connection
        {
            svi.displayError(context.getString(R.string.offline_str));
            return;
        }

        getObservable().subscribeWith(getObserver());
    }


    public Observable<Mp3Response> getObservable(){
        return NetworkClient.getRetrofit().create(NetworkInterface.class)
                .getMp3s()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public DisposableObserver<Mp3Response> getObserver() {
        return new DisposableObserver<Mp3Response>() {

            @Override
            public void onNext(@NonNull Mp3Response mp3Response) {
                Log.d(TAG, "result : " + mp3Response.getMp3s());
                spManager.setMp3Response(mp3Response.getMp3s());
                prepareInitialTrack();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "Error" + e);
                e.printStackTrace();
                svi.displayError("Error fetching Mp3 Data");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
            }
        };
    }

    private void prepareInitialTrack()
    {
        try
        {
            String nextTrack = spManager.getMp3Response().get(0).getAsJsonObject().get("audio").getAsString();
            if(Utils.getLocalFile(context , nextTrack)==null)
            {
                if(Utils.isConnected(context))
                {
                    Intent intent = new Intent(context, DownloadService.class);
                    intent.putExtra(DownloadService.SERVER_URL, nextTrack);
                    intent.putExtra(DownloadService.DOWNLOAD_MODE, 1);
                    context.startService(intent);
                }
            }
        }
        catch (Exception e){}
    }


    @Override
    public void startMain()  // start main activity
    {
        if(spManager.getMp3Response()!=null) {
            context.startActivity(new Intent(context, MainActivity.class));
        }
        else
        {
            getMp3s();
        }
    }

}
