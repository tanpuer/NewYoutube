package com.example.templechen.newyoutube.video;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Size;
import android.view.TextureView;
import com.example.templechen.newyoutube.third.ScalableType;

public class AutoResizeTextureView extends TextureView {

    private static final String TAG = "AutoResizeTextureView";

    public static final int SCALE_TYPE_FIT_CENTER = 0;
    public static final int SCALE_TYPE_CENTER_CROP = 1;

    private int mVideoWidth;
    private int mVideoHeight;
    private float mScaleX = 1.0f;
    private float mScaleY = 1.0f;
    private int mScaleType = 0;

    public AutoResizeTextureView(Context context) {
        super(context);
    }

    public AutoResizeTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoResizeTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (mVideoWidth != 0 && mVideoHeight != 0) {
//            int width = MeasureSpec.getSize(widthMeasureSpec);
//            int height = MeasureSpec.getSize(heightMeasureSpec);
//            mScaleX = mVideoWidth * 1.0f / width;
//            mScaleY = mVideoHeight * 1.0f / height;
//            if (mScaleType == SCALE_TYPE_FIT_CENTER) {
//                float scale = Math.max(mScaleX, mScaleY);
//                setMeasuredDimension((int) (mVideoWidth / scale), (int) (mVideoHeight /scale));
//            }
//            else if (mScaleType == SCALE_TYPE_CENTER_CROP) {
//                float scale = Math.min(mScaleX, mScaleY);
//                setMeasuredDimension((int) (mVideoWidth / scale), (int) (mVideoHeight /scale));
//            }
//        }
    }

    public void setSize(int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
//        Size viewSize = new Size(getWidth(), getHeight());
//        Size videoSize = new Size(mVideoWidth, mVideoHeight);
//        ScaleManager scaleManager = new ScaleManager(viewSize, videoSize);
//        Matrix matrix = scaleManager.getScaleMatrix(ScalableType.CENTER_CROP);
//        if (matrix != null) {
//            setTransform(matrix);
//        }

//        if (mScaleType == SCALE_TYPE_FIT_CENTER) {
//            requestLayout();
//        }
//        else if (mScaleType == SCALE_TYPE_CENTER_CROP) {
//            updateTextureViewSize(getWidth(), getHeight());
//        }

//        if (mScaleType == SCALE_TYPE_FIT_CENTER) {
//            requestLayout();
//        }else {
            updateTextureViewSize(getWidth(), getHeight());
//        }
    }

    public void updateTextureViewSize(int width, int height) {
        if (width == 0 || height ==0 ){
            return;
        }
        ScalableType mScalableType = mScaleType == SCALE_TYPE_FIT_CENTER ? ScalableType.FIT_CENTER: ScalableType.CENTER_CROP;
        Size viewSize = new Size(width, height);
        Size videoSize = new Size(mVideoWidth, mVideoHeight);

        if (mScaleType == SCALE_TYPE_FIT_CENTER) {
            Matrix fitCenterMatrix = getFixCenterMatrix(viewSize, videoSize);
            setTransform(fitCenterMatrix);
        } else if (mScaleType == SCALE_TYPE_CENTER_CROP) {
            Matrix centerCropMatrix = getCenterCropMatrix(viewSize, videoSize);
            setTransform(centerCropMatrix);
        }
    }

    private Matrix getFixCenterMatrix(Size viewSize, Size videoSize){
        float sx = (float) viewSize.getWidth() / videoSize.getWidth();
        float sy = (float) viewSize.getHeight() / videoSize.getHeight();
        float minScale = Math.min(sx, sy);
        sx = minScale / sx;
        sy = minScale / sy;
        return getMatrix(sx, sy, viewSize.getWidth() / 2f, viewSize.getHeight() / 2f);
    }

    private Matrix getCenterCropMatrix(Size viewSize, Size videoSize) {
        float sx = (float) viewSize.getWidth() / videoSize.getWidth();
        float sy = (float) viewSize.getHeight() / videoSize.getHeight();
        float maxScale = Math.max(sx, sy);
        sx = maxScale / sx;
        sy = maxScale / sy;
        return getMatrix(sx, sy, viewSize.getWidth() / 2f, videoSize.getHeight() / 2f);
    }

    private Matrix getMatrix(float sx, float sy, float px, float py) {
        Matrix matrix = new Matrix();
        matrix.setScale(sx, sy, px, py);
        return matrix;
    }

    public void setmScaleType(int mScaleType) {
        this.mScaleType = mScaleType;
        requestLayout();
    }

    public int getmScaleType() {
        return mScaleType;
    }
}
