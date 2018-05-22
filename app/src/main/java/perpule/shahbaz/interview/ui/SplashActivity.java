package perpule.shahbaz.interview.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import perpule.shahbaz.interview.R;

public class SplashActivity extends AppCompatActivity implements SplashViewInterface {

    @BindView(R.id.start_btn)
    Button startBtn;

    private String TAG = "SplashActivity";
    SplashPresenter splashPresenter;

    private static int PERMISSION_RESULT = 501;

    private int mode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        ButterKnife.bind(this);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode=2;
                checkAskPermission();
            }
        });

        setupMVP();
        mode=1;
        checkAskPermission();

    }



    private void setupMVP() {
        splashPresenter = new SplashPresenter(this, this);
    }


    private void getMp3s() {
        splashPresenter.getMp3s();
    }

    private void startMain()
    {
        splashPresenter.startMain();
    }


    @Override
    public void showToast(String str) {
        Toast.makeText(SplashActivity.this,str, Toast.LENGTH_LONG).show();
    }


    @Override
    public void displayError(String e) {
        showToast(e);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PERMISSION_RESULT)
        {
            checkAskPermission();
        }
    }


    ///////////////////////   permission for marshmallow  ///////////////////


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle(getString(R.string.need_permission_str));
        builder.setMessage(getString(R.string.app_needs_permission_str));
        builder.setPositiveButton(R.string.goto_settings_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel_str), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                showToast(getString(R.string.splash_permission_str));
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, PERMISSION_RESULT);
    }


    /*
    mode = 1 for start download
    mode = 2 for start main activity
     */

    private void checkAskPermission()
    {
        try {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {


                    if (report.areAllPermissionsGranted()) {
                        if(mode==1)
                        {
                            getMp3s();
                        }
                        else
                        {
                            startMain();
                        }
                    }
                    else
                    {
                        showSettingsDialog();
                    }

                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            })
                    .withErrorListener(new PermissionRequestErrorListener() {
                        @Override
                        public void onError(DexterError error) {
                            showToast(getString(R.string.splash_permission_str));
                        }
                    }).check();

        }
        catch (Exception e)
        {
            showToast(getString(R.string.error_str));
        }
    }

}
