package com.example.templechen.newyoutube.video.shared;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import com.example.templechen.newyoutube.util.Utils;
import com.example.templechen.newyoutube.video.AutoResizeTextureView;

public class SharedTextureView extends AutoResizeTextureView {

    private static SharedTextureView mInstance;
    private static SurfaceTexture mSurfaceTexture;

    public static SharedTextureView getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedTextureView(context.getApplicationContext());
            mSurfaceTexture = Utils.genSurfaceTexture();
            mInstance.setSurfaceTexture(mSurfaceTexture);
        }
        return mInstance;
    }

    public SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }

    private SharedTextureView(Context context) {
        super(context);
    }

    private SharedTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private SharedTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
