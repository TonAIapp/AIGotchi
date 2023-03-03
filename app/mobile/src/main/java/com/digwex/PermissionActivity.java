package com.digwex;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import static android.util.Log.DEBUG;
import static com.digwex.components.log.Log.println;

public class PermissionActivity extends ImmersiveAppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
    }

    @Override
    protected void onResume() {
        super.onResume();
        println(DEBUG, PermissionActivity.class, "Request permissions");
        //if (ActivityCompat.shouldShowRequestPermissionRationale(this,
        //        Manifest.permission.READ_EXTERNAL_STORAGE)) {
        //    mAllerts[1].show();
        //    return;
        //}
        // No explanation needed; request the permission

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        println(DEBUG, PermissionActivity.class, "PERMISSION_INFO" +
                "\n\tRequestCode: %d" +
                "\n\tCount: %d" +
                "\n\tValue[0]: %d" +
                "\n\tValue[1]: %d",
                requestCode,
                grantResults.length,
                grantResults[0],
                grantResults[1]);

        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length == 2
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    setResult(Activity.RESULT_OK);
                } else {
                    setResult(Activity.RESULT_CANCELED);
                }
                finish();
                break;
            }
        }
    }
}
