package com.example.templechen.newyoutube;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.example.templechen.newyoutube.video.MediaPlayerTool;
import com.example.templechen.newyoutube.video.shared.PlayVideoView;

public class SharedTextureViewTestActivity extends AppCompatActivity {

    private PlayVideoView mPlayTextureView;
    private MediaPlayerTool mMediaPlayerTool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_texture_view_test);
        mPlayTextureView = findViewById(R.id.play_texture_view);

        mMediaPlayerTool = new MediaPlayerTool.Builder()
                .setContext(this)
                .setSurfaceTexture(mPlayTextureView.getSurfaceTexture())
                .setUrl("https://oimryzjfe.qnssl.com/content/47465d359406bb4b68c8c205e2974807.mp4")
                .setLoop(true)
                .setAutoStart(false)
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
                Intent intent = new Intent(SharedTextureViewTestActivity.this, SharedTextureViewFullScreenTestActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayerTool.pause();
//        mMediaPlayerTool.releaseInSharedMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mPlayTextureView.addTextureView();
    }
}
