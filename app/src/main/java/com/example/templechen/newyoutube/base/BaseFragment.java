package com.example.templechen.newyoutube.base;


import android.support.v4.app.Fragment;
import com.example.templechen.newyoutube.video.MediaPlayerTool;

public abstract class BaseFragment extends Fragment {

    protected MediaPlayerTool mMediaPlayerTool;

    @Override
    public void onPause() {
        super.onPause();
        if (mMediaPlayerTool != null) {
            mMediaPlayerTool.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMediaPlayerTool != null && mMediaPlayerTool.isMediaPaused()) {
            mMediaPlayerTool.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayerTool != null) {
            //        mMediaPlayerTool.release();
        }
    }
}
