package com.example.templechen.newyoutube.video.shared;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.example.templechen.newyoutube.R;

import static com.example.templechen.newyoutube.video.AutoResizeTextureView.SCALE_TYPE_FIT_CENTER;

public class PlayVideoView extends RelativeLayout {

    private static final int TEXTURE_ALIGN_PARENT_CENTER = 0;
    private static final int TEXTURE_ALIGN_PARENT_LEFT = 1;
    private static final int TEXTURE_ALIGN_PARENT_RIGHT = 2;

    private SharedTextureView mTextureView;
    private ISurfaceTextureListener mSurfaceTextureListener;
    private int mScaleType = SCALE_TYPE_FIT_CENTER;
    private int mTextureViewAlignMode = TEXTURE_ALIGN_PARENT_CENTER;
    private Context mContext;


    public PlayVideoView(Context context) {
        this(context, null);
    }

    public PlayVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        mContext = context;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PlayVideoView);
            mScaleType = a.getInt(R.styleable.PlayTextureView_scale_type, SCALE_TYPE_FIT_CENTER);
            mTextureViewAlignMode = a.getInt(R.styleable.PlayTextureView_align_mode, TEXTURE_ALIGN_PARENT_CENTER);
            a.recycle();
        }
        initTextureView();
    }

    public void initTextureView() {
        //add textureView
        mTextureView = SharedTextureView.getInstance(mContext);
        mTextureView.setmScaleType(mScaleType);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mTextureViewAlignMode == TEXTURE_ALIGN_PARENT_CENTER) {
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
        } else if (mTextureViewAlignMode == TEXTURE_ALIGN_PARENT_LEFT) {
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else if (mTextureViewAlignMode == TEXTURE_ALIGN_PARENT_RIGHT) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        addTextureView(params);
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

    public void removeTextureView() {
        removeView(mTextureView);
    }

    public void addTextureView() {
        addTextureView(null);
    }

    public void addTextureView(LayoutParams params) {
        if (mTextureView.getParent() != null) {
            if (mTextureView.getParent().hashCode() == this.hashCode()){
                return;
            }
            ((ViewGroup)(mTextureView.getParent())).removeView(mTextureView);
        }
        if (params == null) {
            addView(mTextureView);
        }else {
            addView(mTextureView, params);
        }
    }

    public interface ISurfaceTextureListener {

        void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height);

        void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height);

        boolean onSurfaceTextureDestroyed(SurfaceTexture surface);

        void onSurfaceTextureUpdated(SurfaceTexture surface);

    }

    public SurfaceTexture getSurfaceTexture() {
        return mTextureView.getSurfaceTexture();
    }

    public void setSurfaceTextureListener(ISurfaceTextureListener mSurfaceTextureListener) {
        this.mSurfaceTextureListener = mSurfaceTextureListener;
    }

    public void setSize(int width, int height) {
        mTextureView.setSize(width, height);
    }

}
