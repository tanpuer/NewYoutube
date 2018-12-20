package com.example.templechen.newyoutube.gl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;
import com.example.templechen.newyoutube.R;
import com.example.templechen.newyoutube.base.BaseActivity;

public class VideoGLActivity extends BaseActivity {

    private VideoGLSurfaceView mGLSurfaceView;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_gl);
        mGLSurfaceView = new VideoGLSurfaceView(this);
        mGLSurfaceView.init(false, this, mMediaPlayerTool);
        mRelativeLayout = findViewById(R.id.relative_layout);
        mRelativeLayout.addView(mGLSurfaceView);
    }
}
