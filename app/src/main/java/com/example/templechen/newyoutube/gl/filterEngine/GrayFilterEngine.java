package com.example.templechen.newyoutube.gl.filterEngine;

import android.content.Context;
import com.example.templechen.newyoutube.R;
import com.example.templechen.newyoutube.gl.GLUtils;


import static android.opengl.GLES20.*;

public class GrayFilterEngine extends BaseFilterEngine{

    public GrayFilterEngine(Context context, int OESTextureId){
        super(context, OESTextureId);
        vertexShader = GLUtils.loadShader(GL_VERTEX_SHADER, GLUtils.readShaderFromResource(context,R.raw.gray_vertex_shader));
        fragmentShader = GLUtils.loadShader(GL_FRAGMENT_SHADER, GLUtils.readShaderFromResource(context,R.raw.gray_fragment_shader));
        mProgram = GLUtils.createProgram(vertexShader, fragmentShader);
    }

    @Override
    public void drawFrame() {
        super.drawFrame();
    }
}
