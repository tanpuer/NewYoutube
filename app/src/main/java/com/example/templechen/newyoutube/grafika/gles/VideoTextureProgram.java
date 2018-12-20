package com.example.templechen.newyoutube.grafika.gles;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.util.Log;
import com.example.templechen.newyoutube.R;
import com.example.templechen.newyoutube.gl.GLUtils;

import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;

public class VideoTextureProgram {

    protected static final String aPosition = "aPosition";
    protected static final String uTextureMatrix = "uTextureMatrix";
    protected static final String aTextureCoordinate = "aTextureCoordinate";
    protected static final String uTextureSampler = "uTextureSampler";

    protected Context mContext;
    protected int mOESTextureId;
    protected FloatBuffer floatBuffer;
    protected int vertexShader = -1;
    protected int fragmentShader = -1;
    protected int mProgram;
    protected float[] transformMatrix = new float[16];

    protected int aPositionLocation = -1;
    protected int uTextureMatrixLocation = -1;
    protected int aTextureCoordinateLocation = -1;
    protected int uTextureSamplerLocation = -1;

    public VideoTextureProgram(Context context, int OESTextureId){
        mContext = context;
        mOESTextureId = OESTextureId;
        floatBuffer = GLUtils.createBuffer(GLUtils.vertexData);

        vertexShader = GLUtils.loadShader(GL_VERTEX_SHADER, GLUtils.readShaderFromResource(context,R.raw.base_vertex_shader));
        fragmentShader = GLUtils.loadShader(GL_FRAGMENT_SHADER, GLUtils.readShaderFromResource(context,R.raw.base_fragment_shader));
        mProgram = GLUtils.createProgram(vertexShader, fragmentShader);
    }

    public void drawFrame(){
        aPositionLocation = glGetAttribLocation(mProgram, aPosition);
        uTextureMatrixLocation = glGetUniformLocation(mProgram, uTextureMatrix);
        aTextureCoordinateLocation = glGetAttribLocation(mProgram, aTextureCoordinate);
        uTextureSamplerLocation = glGetUniformLocation(mProgram, uTextureSampler);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mOESTextureId);

        glUniform1i(uTextureSamplerLocation, 0);
        glUniformMatrix4fv(uTextureMatrixLocation, 1, false,transformMatrix, 0);

        if (floatBuffer != null){
            floatBuffer.position(0);
            glEnableVertexAttribArray(aPositionLocation);
            glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 16, floatBuffer);
            floatBuffer.position(2);
            glEnableVertexAttribArray(aTextureCoordinateLocation);
            glVertexAttribPointer(aTextureCoordinateLocation, 2,GL_FLOAT, false, 16, floatBuffer);
        }
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void setTransformMatrix(float[] transformMatrix) {
        this.transformMatrix = transformMatrix;
    }

    public void release(){
        GLES20.glDeleteProgram(mProgram);
        mProgram = -1;
    }
}
