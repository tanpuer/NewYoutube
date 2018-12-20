package com.example.templechen.newyoutube.util;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.util.DisplayMetrics;

public class Utils {

    public static SurfaceTexture genSurfaceTexture() {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        SurfaceTexture surfaceTexture = new SurfaceTexture(textures[0]);
        surfaceTexture.detachFromGLContext();
        return surfaceTexture;
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
