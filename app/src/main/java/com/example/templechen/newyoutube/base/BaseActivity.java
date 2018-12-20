package com.example.templechen.newyoutube.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Explode;
import android.view.Window;
import com.example.templechen.newyoutube.video.MediaPlayerTool;

public abstract class BaseActivity extends AppCompatActivity {

    protected MediaPlayerTool mMediaPlayerTool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inside your activity (if you did not enable transitions in your theme)
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        mMediaPlayerTool = MediaPlayerTool.getInstance();
//        mMediaPlayerTool.setContext(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayerTool != null) {
            mMediaPlayerTool.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMediaPlayerTool != null && mMediaPlayerTool.isMediaPaused()) {
            mMediaPlayerTool.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayerTool != null) {
            //        mMediaPlayerTool.release();
        }
    }
}
