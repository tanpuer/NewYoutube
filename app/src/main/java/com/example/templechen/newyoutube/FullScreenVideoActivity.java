package com.example.templechen.newyoutube;

import android.app.SharedElementCallback;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.transition.*;
import android.view.View;
import com.example.templechen.newyoutube.base.BaseActivity;
import com.example.templechen.newyoutube.video.MediaPlayerTool;
import com.example.templechen.newyoutube.video.PlayTextureView;
import com.example.templechen.newyoutube.video.transition.TextureViewTransition;

import java.util.List;

public class FullScreenVideoActivity extends BaseActivity {

    private PlayTextureView mPlayTextureView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);
        mPlayTextureView = findViewById(R.id.play_texture_view);
        mPlayTextureView.initTextureView(null);
        mMediaPlayerTool = MediaPlayerTool.getInstance();
        mPlayTextureView.setSize(mMediaPlayerTool.getVideoWidth(), mMediaPlayerTool.getVideoHeight());


//        mPlayTextureView.setTransitionName("textureView");
        mPlayTextureView.getTextureView().setTransitionName("textureView");

        TransitionSet transitionSet=new TransitionSet();
//        transitionSet.addTransition(transition);

        transitionSet.addTransition(new ChangeBounds());
        transitionSet.addTransition(new ChangeTransform());
        transitionSet.addTransition(new ChangeClipBounds());
        transitionSet.addTransition(new TextureViewTransition());
//        transitionSet.addTransition(new ChangeImageTransform());

//        transitionSet.addTarget(mPlayTextureView);
        transitionSet.addTarget(mPlayTextureView.getTextureView());
        transitionSet.setDuration(800);
        getWindow().setSharedElementEnterTransition(transitionSet);
//        getWindow().setSharedElementExitTransition(transitionSet);

        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
            }

            @Override
            public void onSharedElementsArrived(List<String> sharedElementNames, List<View> sharedElements, OnSharedElementsReadyListener listener) {
                super.onSharedElementsArrived(sharedElementNames, sharedElements, listener);

            }

            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
            }


        });

        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);

            }
        });

    }

    @Override
    protected void onResume() {
        mMediaPlayerTool.setSurfaceTexture(mPlayTextureView.getSurfaceTexture());
        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.control_btn).setVisibility(View.VISIBLE);
            }
        }, 900);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        findViewById(R.id.control_btn).setVisibility(View.INVISIBLE);
    }
}
