package com.example.templechen.newyoutube;

import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import com.example.templechen.newyoutube.base.BaseActivity;
import com.example.templechen.newyoutube.util.Utils;
import com.example.templechen.newyoutube.video.MediaPlayerTool;
import com.example.templechen.newyoutube.video.PlayTextureView;
import com.example.templechen.newyoutube.video.transition.TextureViewTransition;

import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private PlayTextureView mPlayTextureView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayTextureView = findViewById(R.id.play_texture_view);
        mPlayTextureView.initTextureView(null);
        mPlayTextureView.setSurfaceTextureListener(new PlayTextureView.ISurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });

        mMediaPlayerTool = new MediaPlayerTool.Builder().setContext(this)
                .setTextureView(mPlayTextureView)
                .setSurfaceTexture(mPlayTextureView.getSurfaceTexture())
                .setUrl("https://oimryzjfe.qnssl.com/content/47465d359406bb4b68c8c205e2974807.mp4")
//                .setUrl("https://oimryzjfe.qnssl.com/content/1F3D7F815F2C6870FB512B8CA2C3D2C1.mp4")
                .setLoop(true)
                .setAutoStart(true)
                .build();
        mMediaPlayerTool.prepare();

        mMediaPlayerTool.setVideoListener(new MediaPlayerTool.IVideoListener() {
            @Override
            public void onVideoStart() {
                int width = mMediaPlayerTool.getVideoWidth();
                int height = mMediaPlayerTool.getVideoHeight();
                mPlayTextureView.setSize(width, height);
                mMediaPlayerTool.start();
            }

            @Override
            public void onVideoComplete() {

            }

            @Override
            public void onBufferingUpdate(int percentage) {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onRotationChanged(int rotation) {

            }
        });


        mPlayTextureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FullScreenVideoActivity.class);
                if (Build.VERSION.SDK_INT > 21) {

                    mPlayTextureView.getTextureView().setTransitionName("textureView");
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, new Pair<>(mPlayTextureView.getTextureView(), "textureView"));


//                    mPlayTextureView.setTransitionName("textureView");
//                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, new Pair<>(mPlayTextureView, "textureView"));


                    startActivity(intent, activityOptions.toBundle());

//                    startActivity(intent);
                }else {
                    startActivity(intent);
                }

            }
        });


    }

    @Override
    protected void onResume() {
        mMediaPlayerTool.setSurfaceTexture(mPlayTextureView.getSurfaceTexture());
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayerTool.release();
        Log.d(TAG, "onDestroy: ");
    }

}
