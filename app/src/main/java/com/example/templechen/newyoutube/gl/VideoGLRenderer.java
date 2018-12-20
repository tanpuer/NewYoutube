package com.example.templechen.newyoutube.gl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import com.example.templechen.newyoutube.gl.filterEngine.BaseFilterEngine;
import com.example.templechen.newyoutube.video.MediaPlayerTool;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

public class VideoGLRenderer implements GLSurfaceView.Renderer {

    private int mOESTextureId = -1;
    private SurfaceTexture mSurfaceTexture;
    private boolean bIsPreviewStarted;
    private Context mContext;
    private BaseFilterEngine mFilterEngine;
    private int[] mFBOIds = new int[1];
    private float[] transformMatrix = new float[16];
    private VideoGLSurfaceView mGLSurfaceView;
    private MediaPlayerTool mMediaPlayerTool;


    public void init(boolean isPreviewStarted, Context context, VideoGLSurfaceView mGLSurfaceView, MediaPlayerTool mediaPlayerTool){
        bIsPreviewStarted = isPreviewStarted;
        mContext = context;
        this.mGLSurfaceView = mGLSurfaceView;
        this.mMediaPlayerTool = mediaPlayerTool;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mOESTextureId = GLUtils.createOESTextureObject();
        //设置不同的filter
        mFilterEngine = new BaseFilterEngine(mContext, mOESTextureId);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (!bIsPreviewStarted){
            bIsPreviewStarted = initSurfaceTexture();
            return;
        }
        if (mSurfaceTexture != null){
            mSurfaceTexture.updateTexImage();
            mSurfaceTexture.getTransformMatrix(transformMatrix);
            mFilterEngine.setTransformMatrix(transformMatrix);
        }

        glClearColor(0f,0f,0f,0f);

        mFilterEngine.drawFrame();
    }

    private boolean initSurfaceTexture(){
        if (mGLSurfaceView == null){
            return false;
        }
        //根据外部纹理id创建surfaceTexture
        mSurfaceTexture = new SurfaceTexture(mOESTextureId);
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                mGLSurfaceView.requestRender();
            }
        });
        //将此surfaceTexture作为player的输出
        mMediaPlayerTool = new MediaPlayerTool.Builder().setContext(mContext)
                .setSurfaceTexture(mSurfaceTexture)
                .setUrl("https://oimryzjfe.qnssl.com/content/1F3D7F815F2C6870FB512B8CA2C3D2C1.mp4")
                .setLoop(true)
                .setAutoStart(true)
                .build();
        mMediaPlayerTool.prepare();

        mMediaPlayerTool.setVideoListener(new MediaPlayerTool.IVideoListener() {
            @Override
            public void onVideoStart() {
                int width = mMediaPlayerTool.getVideoWidth();
                int height = mMediaPlayerTool.getVideoHeight();
//                mPlayTextureView.setSize(width, height);
                
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
        return true;
    }

    public void destroy(){
        mFilterEngine = null;
        if (mSurfaceTexture != null){
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        mOESTextureId = -1;
        bIsPreviewStarted = false;
    }
}
