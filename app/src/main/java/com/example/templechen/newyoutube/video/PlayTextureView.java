package com.example.templechen.newyoutube.video;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.example.templechen.newyoutube.R;

import static com.example.templechen.newyoutube.video.AutoResizeTextureView.SCALE_TYPE_FIT_CENTER;

public class PlayTextureView extends RelativeLayout {

    private static final int TEXTURE_ALIGN_PARENT_CENTER = 0;
    private static final int TEXTURE_ALIGN_PARENT_LEFT = 1;
    private static final int TEXTURE_ALIGN_PARENT_RIGHT = 2;

    private AutoResizeTextureView mTextureView;
    private SurfaceTexture mSurfaceTexture;
    private ISurfaceTextureListener mSurfaceTextureListener;
    private int mScaleType = SCALE_TYPE_FIT_CENTER;
    private int mTextureViewAlignMode = TEXTURE_ALIGN_PARENT_CENTER;

    public PlayTextureView(Context context) {
        this(context, null);
    }

    public PlayTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PlayTextureView);
            mScaleType = a.getInt(R.styleable.PlayTextureView_scale_type, SCALE_TYPE_FIT_CENTER);
            mTextureViewAlignMode = a.getInt(R.styleable.PlayTextureView_align_mode, TEXTURE_ALIGN_PARENT_CENTER);
            a.recycle();
        }
//        initTextureView(null);
    }

    public void initTextureView(SurfaceTexture surfaceTexture) {
        //add textureView
        mTextureView = new AutoResizeTextureView(getContext());
        mTextureView.setmScaleType(mScaleType);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mTextureViewAlignMode == TEXTURE_ALIGN_PARENT_CENTER) {
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
        } else if (mTextureViewAlignMode == TEXTURE_ALIGN_PARENT_LEFT) {
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else if (mTextureViewAlignMode == TEXTURE_ALIGN_PARENT_RIGHT) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        this.addView(mTextureView, params);

        if (surfaceTexture == null) {
            mSurfaceTexture = genSurfaceTexture();
        } else {
            mSurfaceTexture = surfaceTexture;
        }
        mTextureView.setSurfaceTexture(mSurfaceTexture);
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                if (mSurfaceTextureListener != null) {
                    mSurfaceTextureListener.onSurfaceTextureAvailable(surface, width, height);
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                if (mSurfaceTextureListener != null) {
                    mSurfaceTextureListener.onSurfaceTextureSizeChanged(surface, width, height);
                }
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                if (mSurfaceTextureListener != null) {
                    return mSurfaceTextureListener.onSurfaceTextureDestroyed(surface);
                }
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                if (mSurfaceTextureListener != null) {
                    mSurfaceTextureListener.onSurfaceTextureUpdated(surface);
                }
            }
        });
    }

    public AutoResizeTextureView getTextureView() {
        return mTextureView;
    }

    public interface ISurfaceTextureListener {

        void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height);

        void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height);

        boolean onSurfaceTextureDestroyed(SurfaceTexture surface);

        void onSurfaceTextureUpdated(SurfaceTexture surface);

    }

    public SurfaceTexture getSurfaceTexture() {
        if (mSurfaceTexture == null) {
            mSurfaceTexture = genSurfaceTexture();
        }
        return mSurfaceTexture;
    }

    public void setSurfaceTextureListener(ISurfaceTextureListener mSurfaceTextureListener) {
        this.mSurfaceTextureListener = mSurfaceTextureListener;
    }

    public void setSize(int width, int height) {
        mTextureView.setSize(width, height);
    }

    private SurfaceTexture genSurfaceTexture() {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        SurfaceTexture surfaceTexture = new SurfaceTexture(textures[0]);
        surfaceTexture.detachFromGLContext();
        return surfaceTexture;
    }

    public void clearSurface() {
//        mTextureView.lockCanvas();
    }

    public void resetTextureView() {
        resetTextureView(null);
    }

    public void resetTextureView(SurfaceTexture surfaceTexture) {
        removeView(mTextureView);
        initTextureView(surfaceTexture);
    }

}
