package com.example.templechen.newyoutube.gl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import com.example.templechen.newyoutube.video.MediaPlayerTool;

public class VideoGLSurfaceView extends GLSurfaceView {

    private VideoGLRenderer mRenderer;

    public VideoGLSurfaceView(Context context) {
        super(context);
    }

    public void init(boolean isPreviewStarted, Context context, MediaPlayerTool mediaPlayerTool){
        setEGLContextClientVersion(2);
        mRenderer = new VideoGLRenderer();
        mRenderer.init(isPreviewStarted, context, this, mediaPlayerTool);
        setRenderer(mRenderer);
    }

    public void destroy(){
        if (mRenderer != null){
            mRenderer.destroy();
            mRenderer = null;
        }
    }
}
