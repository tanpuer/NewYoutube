package com.example.templechen.newyoutube;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.templechen.newyoutube.video.MediaPlayerTool;
import com.example.templechen.newyoutube.video.shared.PlayVideoView;

public class SharedTextureViewFullScreenTestActivity extends AppCompatActivity {

    private PlayVideoView mPlayTextureView;
    private MediaPlayerTool mMediaPlayerTool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_texture_view_full_screen_test);
        mPlayTextureView = findViewById(R.id.play_texture_view);

        mMediaPlayerTool = MediaPlayerTool.getInstance();
        mMediaPlayerTool.setSurfaceTexture(mPlayTextureView.getSurfaceTexture());
        mMediaPlayerTool.start();

        mMediaPlayerTool.setVideoListener(new MediaPlayerTool.IVideoListener() {
            @Override
            public void onVideoStart() {

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
    protected void onPause() {
        super.onPause();
        mMediaPlayerTool.pause();
        mPlayTextureView.removeTextureView();
    }
}
