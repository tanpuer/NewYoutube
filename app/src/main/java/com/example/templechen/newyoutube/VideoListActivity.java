package com.example.templechen.newyoutube;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.example.templechen.newyoutube.adapter.ListVideoAdapter;
import com.example.templechen.newyoutube.base.BaseActivity;
import com.example.templechen.newyoutube.data.VideoData;
import com.example.templechen.newyoutube.video.MediaPlayerTool;

public class VideoListActivity extends BaseActivity {

    private static final String TAG = "VideoListActivity";

    private static final int RESUME_TYPE = 1;
    private static final int PAUSE_TYPE = 2;
    private static final int DESTRIY_TYPE = 3;
    private static final int RESET_TYPE = 4;

    private RecyclerView mRecyclerView;
    private ListVideoAdapter mListAdapter;
    private PagerSnapHelper mPagerSnapHelper;
    private String[] mData = VideoData.videoData;
    private View playView;
    private int currentPos = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_video);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.post(() -> {
            mRecyclerView.scrollToPosition(0);
            mRecyclerView.post(()-> playVisibleCheck(0));
        });

        //PagerSnapHelper
        mPagerSnapHelper = new PagerSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                return super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
            }

            @Nullable
            @Override
            public View findSnapView(RecyclerView.LayoutManager layoutManager) {
                return super.findSnapView(layoutManager);
            }
        };
        mPagerSnapHelper.attachToRecyclerView(mRecyclerView);

        mListAdapter = new ListVideoAdapter(this, mData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mListAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mPagerSnapHelper.findSnapView(mRecyclerView.getLayoutManager()) != playView) {
                        playVisibleCheck(RESET_TYPE);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        playVisibleCheck(RESUME_TYPE);
    }

    private void playVisibleCheck(int type) {

        Log.d(TAG, "playVisibleCheck: ");
        View view = mPagerSnapHelper.findSnapView(mRecyclerView.getLayoutManager());
        if (view == null) {
            return;
        }
        playView = view;

        final ListVideoAdapter.ListVideoViewHolder vh = (ListVideoAdapter.ListVideoViewHolder) mRecyclerView.getChildViewHolder(playView);
        final int position = mRecyclerView.getLayoutManager().getPosition(playView);
        if (currentPos == position) {
            return;
        }
        currentPos = position;

        switch (type){
            case RESET_TYPE: {
                if (mMediaPlayerTool != null) {
                    mMediaPlayerTool.reset();
                }
                break;
            }
            default:
                break;
        }

        vh.mPlayTextureView.resetTextureView(null);
        mMediaPlayerTool = new MediaPlayerTool.Builder().setContext(this)
                .setTextureView(vh.mPlayTextureView)
                .setSurfaceTexture(vh.mPlayTextureView.getSurfaceTexture())
                .setUrl(mData[position])
                .setLoop(true)
                .setAutoStart(true)
                .build();
        mMediaPlayerTool.prepare();

        mMediaPlayerTool.setVideoListener(new MediaPlayerTool.IVideoListener() {
            @Override
            public void onVideoStart() {
                int width = mMediaPlayerTool.getVideoWidth();
                int height = mMediaPlayerTool.getVideoHeight();
                vh.mPlayTextureView.setSize(width, height);
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
    }

}
