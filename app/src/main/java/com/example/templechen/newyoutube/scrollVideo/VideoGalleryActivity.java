package com.example.templechen.newyoutube.scrollVideo;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import com.example.templechen.newyoutube.R;
import com.example.templechen.newyoutube.base.BaseActivity;

import java.io.File;

public class VideoGalleryActivity extends BaseActivity {

    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private int fileCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_gallery);
        mTextView = findViewById(R.id.title);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        checkRecordVideos();
    }

    private void checkRecordVideos() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + "/grafika");
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            fileCount = files.length;
            setTitle();

            mRecyclerView.setAdapter(new VideoListAdapter(files, this));
        }
    }

    private void setTitle() {
        mTextView.setText("you have recorded " + fileCount + " videos");
    }
}
