package com.example.templechen.newyoutube.scrollVideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.example.templechen.newyoutube.R;
import com.example.templechen.newyoutube.base.BaseActivity;
import com.example.templechen.newyoutube.video.MediaPlayerTool;
import com.example.templechen.newyoutube.video.PlayTextureView;

public class PurePlayActivity extends BaseActivity {

    private PlayTextureView mPlayTextureView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pure_play);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        mPlayTextureView = findViewById(R.id.play_texture_view);
        mPlayTextureView.initTextureView(null);
        mMediaPlayerTool = new MediaPlayerTool.Builder().setContext(this)
                .setTextureView(mPlayTextureView)
                .setSurfaceTexture(mPlayTextureView.getSurfaceTexture())
                .setUrl(url)
                .setLoop(false)
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayerTool != null) {
            mMediaPlayerTool.reset();
        }
    }
}
