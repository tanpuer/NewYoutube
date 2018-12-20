package com.example.templechen.newyoutube.video;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.view.Surface;
import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;

import java.io.IOException;
import java.lang.ref.SoftReference;

public class MediaPlayerTool implements IjkMediaPlayer.OnInfoListener,
        IjkMediaPlayer.OnErrorListener,
        IjkMediaPlayer.OnBufferingUpdateListener,
        IjkMediaPlayer.OnPreparedListener,
        IjkMediaPlayer.OnCompletionListener {

    private static final String TAG = "MediaPlayerTool";

    private IMediaPlayer mMediaPlayer;
    private static MediaPlayerTool mMediaPlayerTool;
    private boolean loadIjkSucc;
    private SurfaceTexture mSurfaceTexture;
    private float mVolume;
    private long mDuration;
    private IMediaDataSource mMediaDataSource;
    private IVideoListener mVideoListener;
    private boolean paused;
    private boolean autoStart;
    private Context mContext;
    private SoftReference<PlayTextureView> srPlayTextureView;

    private MediaPlayerTool() {
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            loadIjkSucc = true;
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            loadIjkSucc = false;
        }
    }

    public synchronized static MediaPlayerTool getInstance() {
        if (mMediaPlayerTool == null) {
            mMediaPlayerTool = new MediaPlayerTool();
        }
        return mMediaPlayerTool;
    }

    public void initMediaPlayer() {
        if (loadIjkSucc) {
            IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "OPT_CATEGORY_PLAYER", 1);

            mMediaPlayer = ijkMediaPlayer;
        } else {
            mMediaPlayer = new AndroidMediaPlayer();
        }
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        mSurfaceTexture = surfaceTexture;
        if (mMediaPlayer != null && mSurfaceTexture != null) {
            mMediaPlayer.setSurface(new Surface(surfaceTexture));
        }
    }

    public void prepare() {
        if (mMediaPlayer != null) {
            mMediaPlayer.prepareAsync();
        }
    }

    public void setDataSource(IMediaDataSource iMediaDataSource) {
        if (mMediaPlayer != null) {
            mMediaDataSource = iMediaDataSource;
            mMediaPlayer.setDataSource(mMediaDataSource);
        }
    }

    public void setSimpleDataSource(Context context, String url) {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.setDataSource(context, Uri.parse(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setPlayTextureView(PlayTextureView playTextureView) {
        if (srPlayTextureView != null) {
            srPlayTextureView.clear();
        }
        srPlayTextureView = new SoftReference(playTextureView);
    }

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            paused = true;
        }
    }

    public boolean isMediaPaused() {
        if (mMediaPlayer != null) {
            return paused;
        }
        return false;
    }

    public int getVideoWidth() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getVideoWidth();
        }
        return 0;
    }

    public int getVideoHeight() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getVideoHeight();
        }
        return 0;
    }

    public void setLooping(boolean looping) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setLooping(looping);
        }
    }

    public void setVolume(int volume) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setVolume(volume, volume);
        }
    }

    public float getmVolume() {
        return mVolume;
    }

    public long getmDuration() {
        return mDuration;
    }

    public long getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0L;
    }

    public void seekTo(long mSec) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(mSec);
        }
    }

    public boolean getIsPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    public void reset() {
        if (srPlayTextureView != null) {
            PlayTextureView playTextureView = srPlayTextureView.get();
            if (playTextureView != null) {
                playTextureView.resetTextureView();
            }
            srPlayTextureView.clear();
            srPlayTextureView = null;
        }
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        if (mMediaDataSource != null) {
            mMediaDataSource = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
    }

    public void release() {
        if (srPlayTextureView != null) {
            PlayTextureView playTextureView = srPlayTextureView.get();
            if (playTextureView != null) {
                playTextureView.resetTextureView();
            }
            srPlayTextureView.clear();
            srPlayTextureView = null;
        }
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        if (mMediaDataSource != null) {
            mMediaDataSource = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void resetInSharedMode() {
        if (mMediaDataSource != null) {
            mMediaDataSource = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
    }

    public void releaseInSharedMode() {
        if (mMediaDataSource != null) {
            mMediaDataSource = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percentage) {
        if (mVideoListener != null) {
            mVideoListener.onBufferingUpdate(percentage);
        }
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        if (mVideoListener != null) {
            mVideoListener.onVideoComplete();
        }
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
        if (mVideoListener != null) {
            mVideoListener.onError();
        }
        reset();
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED: {
                if (mVideoListener != null) {
                    mVideoListener.onRotationChanged(extra);
                    break;
                }
            }
            default:
                break;
        }
        return true;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        if (mMediaPlayer != null) {
            if (autoStart) {
                mMediaPlayer.start();
            }
            mDuration = mMediaPlayer.getDuration();
            if (mVideoListener != null) {
                mVideoListener.onVideoStart();
            }
        }
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    public void setVideoListener(IVideoListener mVideoListener) {
        this.mVideoListener = mVideoListener;
    }

    public interface IVideoListener {

        void onVideoStart();

        void onVideoComplete();

        void onBufferingUpdate(int percentage);

        void onError();

        void onRotationChanged(int rotation);

    }

    public static class Builder {
        private MediaPlayerTool mediaPlayerTool;
        private SurfaceTexture surfaceTexture;
        private String url;
        private boolean loop;
        private boolean autoStart;
        private Context context;
        private PlayTextureView playTextureView;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setTextureView(PlayTextureView playTextureView) {
            this.playTextureView = playTextureView;
            return this;
        }

        public Builder setSurfaceTexture(SurfaceTexture surfaceTexture) {
            this.surfaceTexture = surfaceTexture;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setLoop(boolean loop) {
            this.loop = loop;
            return this;
        }

        public Builder setAutoStart(boolean autoStart) {
            this.autoStart = autoStart;
            return this;
        }

        public MediaPlayerTool build() {
            if (mediaPlayerTool == null) {
                mediaPlayerTool = MediaPlayerTool.getInstance();
                mediaPlayerTool.initMediaPlayer();
            }
            mediaPlayerTool.setPlayTextureView(playTextureView);
            mediaPlayerTool.setSurfaceTexture(surfaceTexture);
            mediaPlayerTool.setSimpleDataSource(context, url);
            mediaPlayerTool.setAutoStart(autoStart);
            mediaPlayerTool.setLooping(loop);
            return mediaPlayerTool;
        }

    }

}
