package perpule.shahbaz.interview.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import perpule.shahbaz.interview.DownloadService;
import perpule.shahbaz.interview.R;
import perpule.shahbaz.interview.utils.Utils;

import static android.app.Activity.RESULT_OK;

public class Mp3FPresenter implements Mp3FPresenterInterface {

    Mp3FViewInterface mvi;
    private String TAG = "Mp3FPresenter";

    private String currentFilePath = "";

    private MediaPlayer mp;

    Context context;

    public Mp3FPresenter(Mp3FViewInterface mvi, Context context) {
        this.mvi = mvi;
        this.context = context;
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                boolean resultStatus = bundle.getBoolean(DownloadService.RESULT);
                String filePath = bundle.getString(DownloadService.SERVER_URL);
                //int mode = bundle.getInt(DownloadService.DOWNLOAD_MODE);

                if(filePath.equals(currentFilePath))
                {
                    if (resultStatus) {
                        loadCurrentTrack();
                    } else {
                        mvi.displayError(context.getString(R.string.download_failed));
                    }
                    mvi.hideProgres();
                    prepareNextTrack(MainPresenter.selectedPos);
                }
            }
        }
    };



    @Override
    public void loadCurrentTrack()
    {
        stopMp3();
        mvi.hideProgres();

        if(MainPresenter.selectedPos>=MainPresenter.mp3List.size())
        {
            mvi.showToast(context.getString(R.string.last_track));
            MainPresenter.selectedPos = 0;
        }

        currentFilePath = MainPresenter.mp3List.get(MainPresenter.selectedPos).getAudio();
        String trackName = MainPresenter.mp3List.get(MainPresenter.selectedPos).getDesc();

        mvi.onPlay(trackName);

        String localTrack = Utils.getLocalFile(context , currentFilePath);

        if(localTrack==null)
        {
            // start download
            if(!Utils.isConnected(context))
            {
                mvi.displayError(context.getString(R.string.offline_str));
                return;
            }

            mvi.showProgress();

            Intent intent = new Intent(context, DownloadService.class);
            intent.putExtra(DownloadService.SERVER_URL, currentFilePath);
            intent.putExtra(DownloadService.DOWNLOAD_MODE, 0);
            context.startService(intent);
        }
        else
        {
            playMp3(localTrack);
            prepareNextTrack(MainPresenter.selectedPos);
        }


    }

    @Override
    public void onViewAttached()
    {
        context.registerReceiver(receiver, new IntentFilter(
                DownloadService.NOTIFICATION));
    }

    @Override
    public void onViewDetached()
    {
        context.unregisterReceiver(receiver);
        stopMp3();
    }

    private void stopMp3()
    {
        try
        {
            mp.stop();
            mp.release();
        }
        catch (Exception e)
        {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private void playMp3(String url)
    {
        try
        {
                mp=new MediaPlayer();
                mp.setDataSource(url);
                mp.prepare();
                mp.start();
        }
        catch (Exception e){
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private void prepareNextTrack(int position)
    {
        try
        {
            String nextTrack = MainPresenter.mp3List.get(position+1).getAudio();
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
}
