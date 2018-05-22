package perpule.shahbaz.interview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import perpule.shahbaz.interview.models.Mp3Response;
import perpule.shahbaz.interview.network.NetworkClient;
import perpule.shahbaz.interview.network.NetworkInterface;
import perpule.shahbaz.interview.utils.Utils;

public class DownloadService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String RESULT = "result";
    public static final String SERVER_URL = "serverUrl";
    public static final String DOWNLOAD_MODE = "downloadMode";
    public static final String NOTIFICATION = "perpule.shahbaz.interview.downloadReceiver";

    private String serverUrl = "";
    private String localUrl = "";
    private String localPath = "";
    private String fileName = "";

    /*
    0 = dpwnload and play
    1 = download
     */
    private int mode = 0;

    private String TAG = "DownloadService";

    public DownloadService() {
        super("DownloadService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        serverUrl = intent.getStringExtra(SERVER_URL);
        mode = intent.getIntExtra(DOWNLOAD_MODE, 1);
        fileName = Utils.getFileName(serverUrl);
        localPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+getString(R.string.app_name)+"/";
        localUrl = localPath+fileName;

        getObservable(serverUrl).subscribeWith(getObserver());
    }


    public Observable<ResponseBody> getObservable(String url){
        return NetworkClient.getRetrofit().create(NetworkInterface.class)
                .downloadFile(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public DisposableObserver<ResponseBody> getObserver() {
        return new DisposableObserver<ResponseBody>() {

            @Override
            public void onNext(@NonNull ResponseBody responseBody) {
                boolean writtenToDisk = writeResponseBodyToDisk(responseBody);
                Log.d(TAG, "file download was a success? " + writtenToDisk);
                publishResults(writtenToDisk, serverUrl, mode);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "Error" + e);
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
            }
        };
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {

        try {

            File direct = new File(localPath);
            if (!direct.exists()) {
                File wallpaperDirectory = new File(localPath);
                wallpaperDirectory.mkdirs();
            }

            File file = new File(localPath,fileName);
            file.createNewFile();

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(localUrl, false);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                    Log.d(TAG, "file path: " + localUrl);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return false;
        }
    }


    private void publishResults(boolean result, String path, int mode) {
        if(mode==0)
        {
            Intent intent = new Intent(NOTIFICATION);
            intent.putExtra(RESULT, result);
            intent.putExtra(SERVER_URL, path);
            intent.putExtra(DOWNLOAD_MODE, mode);
            sendBroadcast(intent);
        }
    }
}

