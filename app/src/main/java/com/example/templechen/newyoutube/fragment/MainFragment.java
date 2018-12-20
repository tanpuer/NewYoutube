package com.example.templechen.newyoutube.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.templechen.newyoutube.FullScreenVideoActivity;
import com.example.templechen.newyoutube.R;
import com.example.templechen.newyoutube.base.BaseFragment;
import com.example.templechen.newyoutube.video.MediaPlayerTool;
import com.example.templechen.newyoutube.video.PlayTextureView;

public class MainFragment extends BaseFragment {

    private PlayTextureView mPlayTextureView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mPlayTextureView = view.findViewById(R.id.play_texture_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMediaPlayerTool = MediaPlayerTool.getInstance();
        mMediaPlayerTool.setSurfaceTexture(mPlayTextureView.getSurfaceTexture());
        mMediaPlayerTool.setSimpleDataSource(getActivity(), "https://oimryzjfe.qnssl.com/content/93fcbd491e40159e949bb4cb191e231e.mp4");
        mMediaPlayerTool.setLooping(true);
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
                Intent intent = new Intent(getActivity(), FullScreenVideoActivity.class);
                startActivity(intent);
            }
        });
    }

}
