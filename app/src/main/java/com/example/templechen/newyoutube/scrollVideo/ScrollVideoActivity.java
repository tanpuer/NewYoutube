package com.example.templechen.newyoutube.scrollVideo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.display.DisplayManager;
import android.media.*;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import com.example.templechen.newyoutube.R;
import com.example.templechen.newyoutube.base.BaseActivity;
import com.example.templechen.newyoutube.video.MediaPlayerTool;
import com.example.templechen.newyoutube.video.PlayTextureView;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ScrollVideoActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ScrollVideoActivity";

    private PlayTextureView mPlayTextureView;
    private float startX = 0f;
    private float startY = 0f;
    private float widthPivot = 0f;
    private float heightPivot = 0f;
    private boolean initPivot = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_video);
        mPlayTextureView = findViewById(R.id.play_texture_view);
        mPlayTextureView.initTextureView(null);
        mMediaPlayerTool = new MediaPlayerTool.Builder().setContext(this)
                .setTextureView(mPlayTextureView)
                .setSurfaceTexture(mPlayTextureView.getSurfaceTexture())
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
                mPlayTextureView.setSize(width, height);
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

        record_btn = findViewById(R.id.record_btn);
        record_btn.setOnClickListener(this);

        mPlayTextureView.setSurfaceTextureListener(new PlayTextureView.ISurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startX = event.getX();
                startY = event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float endX = event.getX();
                float endY = event.getY();

                doTransForm(endX - startX, endY - startY);

                startX = endX;
                startY = endY;
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            default:
                break;
        }
        return false;
    }

    private void doTransForm(float deltaX, float delatY) {
        Size viewSize = new Size(mPlayTextureView.getTextureView().getWidth(), mPlayTextureView.getTextureView().getHeight());
        Size videoSize = new Size(mMediaPlayerTool.getVideoWidth(), mMediaPlayerTool.getVideoHeight());
        if (!initPivot) {
            widthPivot = mPlayTextureView.getTextureView().getWidth()/2;
            heightPivot = mPlayTextureView.getTextureView().getHeight()/2;
            initPivot = true;
        }
        Matrix matrix = getCenterCropMatrix(viewSize, videoSize, deltaX, delatY);
        mPlayTextureView.getTextureView().setTransform(matrix);
        Log.d(TAG, "doTransForm: " + widthPivot + ", " + heightPivot);
    }

    private Matrix getCenterCropMatrix(Size viewSize, Size videoSize, float deltaX, float deltaY) {
        float sx = (float) viewSize.getWidth() / videoSize.getWidth();
        float sy = (float) viewSize.getHeight() / videoSize.getHeight();
        float maxScale = Math.max(sx, sy);
        sx = maxScale / sx;
        sy = maxScale / sy;
        widthPivot = widthPivot - deltaX;
        heightPivot = heightPivot + deltaY;
        if (widthPivot > videoSize.getWidth()) {
            widthPivot = videoSize.getWidth();
        }else if (widthPivot <0){
            widthPivot = 0;
        }

        if (heightPivot > videoSize.getHeight()) {
            heightPivot = videoSize.getHeight();
        }else if (heightPivot < 0){
            heightPivot = 0;
        }

        return getMatrix(sx, sy, widthPivot, heightPivot);
    }

    private Matrix getMatrix(float sx, float sy, float px, float py) {
        Matrix matrix = new Matrix();
        matrix.setScale(sx, sy, px, py);
        return matrix;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecord();
    }

    //record textureview to mp4
    private Button record_btn;

    //copy from google grafika
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private MediaMuxer muxer;
    private Surface inputSurface;
    private MediaCodec videoEncoder;
    private boolean muxerStarted;
    private int trackIndex = -1;

    private static final int REQUEST_CODE_CAPTURE_PERM = 1234;
    private static final String VIDEO_MIME_TYPE = "video/avc";
    private android.media.MediaCodec.Callback encoderCallback;

    private void initRecord() {

        mediaProjectionManager = (MediaProjectionManager) getSystemService(
                android.content.Context.MEDIA_PROJECTION_SERVICE);
        encoderCallback = new MediaCodec.Callback() {
            @Override
            public void onInputBufferAvailable(@NonNull MediaCodec codec, int index) {
                Log.d(TAG, "Input Buffer Avail");
            }

            @Override
            public void onOutputBufferAvailable(@NonNull MediaCodec codec, int index, @NonNull MediaCodec.BufferInfo info) {
                ByteBuffer encodedData = videoEncoder.getOutputBuffer(index);
                if (encodedData == null) {
                    throw new RuntimeException("couldn't fetch buffer at index " + index);
                }

                if ((info.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    info.size = 0;
                }

                if (info.size != 0) {
                    if (muxerStarted) {
                        encodedData.position(info.offset);
                        encodedData.limit(info.offset + info.size);
                        muxer.writeSampleData(trackIndex, encodedData, info);
                    }
                }

                videoEncoder.releaseOutputBuffer(index, false);

            }

            @Override
            public void onError(@NonNull MediaCodec codec, @NonNull MediaCodec.CodecException e) {
                Log.e(TAG, "MediaCodec " + codec.getName() + " onError:", e);
            }

            @Override
            public void onOutputFormatChanged(@NonNull MediaCodec codec, @NonNull MediaFormat format) {
                Log.d(TAG, "Output Format changed");
                if (trackIndex >= 0) {
                    throw new RuntimeException("format changed twice");
                }
                trackIndex = muxer.addTrack(videoEncoder.getOutputFormat());
                if (!muxerStarted && trackIndex >= 0) {
                    muxer.start();
                    muxerStarted = true;
                }
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startRecording() {
        DisplayManager dm = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display defaultDisplay;
        if (dm != null) {
            defaultDisplay = dm.getDisplay(Display.DEFAULT_DISPLAY);
        } else {
            throw new IllegalStateException("Cannot display manager?!?");
        }
        if (defaultDisplay == null) {
            throw new RuntimeException("No display found.");
        }

        // Get the display size and density.
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        int screenDensity = metrics.densityDpi;

        prepareVideoEncoder(screenWidth, screenHeight);

        try {
            File outputFile = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES) + "/grafika", "Screen-record-" +
                    Long.toHexString(System.currentTimeMillis()) + ".mp4");
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }
            muxer = new MediaMuxer(outputFile.getCanonicalPath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException ioe) {
            throw new RuntimeException("MediaMuxer creation failed", ioe);
        }


        // Start the video input.
        mediaProjection.createVirtualDisplay("Recording Display", screenWidth,
                screenHeight, screenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR/* flags */, inputSurface,
                null /* callback */, null /* handler */);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void prepareVideoEncoder(int width, int height) {
        MediaFormat format = MediaFormat.createVideoFormat(VIDEO_MIME_TYPE, width, height);
        int frameRate = 30; // 30 fps

        // Set some required properties. The media codec may fail if these aren't defined.
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, 6000000); // 6Mbps
        format.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate);
        format.setInteger(MediaFormat.KEY_CAPTURE_RATE, frameRate);
        format.setInteger(MediaFormat.KEY_REPEAT_PREVIOUS_FRAME_AFTER, 1000000 / frameRate);
        format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1); // 1 seconds between I-frames

        // Create a MediaCodec encoder and configure it. Get a Surface we can use for recording into.
        try {
            videoEncoder = MediaCodec.createEncoderByType(VIDEO_MIME_TYPE);
            videoEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            inputSurface = videoEncoder.createInputSurface();
            videoEncoder.setCallback(encoderCallback);
            videoEncoder.start();
        } catch (IOException e) {
            releaseEncoders();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void releaseEncoders() {
        if (muxer != null) {
            if (muxerStarted) {
                muxer.stop();
            }
            muxer.release();
            muxer = null;
            muxerStarted = false;
        }
        if (videoEncoder != null) {
            videoEncoder.stop();
            videoEncoder.release();
            videoEncoder = null;
        }
        if (inputSurface != null) {
            inputSurface.release();
            inputSurface = null;
        }
        if (mediaProjection != null) {
            mediaProjection.stop();
            mediaProjection = null;
        }
        trackIndex = -1;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void stopRecording() {
        releaseEncoders();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (REQUEST_CODE_CAPTURE_PERM == requestCode) {
            Button b = findViewById(R.id.record_btn);
            b.setEnabled(true);
            if (resultCode == RESULT_OK) {
                mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, intent);
                startRecording();
                b.setText("recording...");
            } else {
                // user did not grant permissions
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Permission is required to record the screen.")
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_btn: {
                if (muxerStarted) {
                    stopRecording();
                    ((Button) findViewById(R.id.record_btn)).setText("click to record");
                } else {
                    Intent permissionIntent = mediaProjectionManager.createScreenCaptureIntent();
                    startActivityForResult(permissionIntent, REQUEST_CODE_CAPTURE_PERM);
                    findViewById(R.id.record_btn).setEnabled(false);
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (muxerStarted) {
            stopRecording();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayerTool != null) {
            mMediaPlayerTool.reset();
        }
    }

}
