package com.digwex;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import java.lang.ref.WeakReference;

//import static com.digwex.components.log.Log.DISABLE;
//import static com.digwex.components.log.Log.println;

@SuppressLint("Registered")
public class ImmersiveAppCompatActivity extends AppCompatActivity {
    private HideHandler mHideHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create a handler to set immersive mode on a delay
        mHideHandler = new HideHandler(this);

        // set on create

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
                visibility -> {

                    //System.out.println("VISIBILITY: " + visibility);

                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        setToImmersiveMode();
                    }
                });
        setToImmersiveMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToImmersiveMode();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) {
            mHideHandler.removeMessages(0);
            mHideHandler.sendEmptyMessageDelayed(0, 300);
        }
        else mHideHandler.removeMessages(0);
    }

    private void setToImmersiveMode() {
        // set to immersive
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //println(DISABLE, MainActivity.class, "HIDE_NAVIGATION_BAR");
    }

    private static class HideHandler extends Handler {
        private final WeakReference<ImmersiveAppCompatActivity> mActivity;

        HideHandler(ImmersiveAppCompatActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ImmersiveAppCompatActivity activity = mActivity.get();
            if(activity != null) activity.setToImmersiveMode();
        }
    }
}
