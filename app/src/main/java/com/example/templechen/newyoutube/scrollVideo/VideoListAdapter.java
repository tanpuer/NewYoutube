package com.example.templechen.newyoutube.scrollVideo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.templechen.newyoutube.R;

import java.io.File;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListHolder> {

    private File[] mFiles;
    private Context mContext;

    public VideoListAdapter(File[] mFiles, Context mContext) {
        this.mFiles = mFiles;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VideoListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VideoListHolder(LayoutInflater.from(mContext).inflate(R.layout.item_record_video, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoListHolder videoListHolder, int i) {
        videoListHolder.mTitle.setText(mFiles[i].getName());
        videoListHolder.mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mFiles[i].getAbsolutePath();
                Intent intent = new Intent(mContext, PurePlayActivity.class);
                intent.putExtra("url", url);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFiles.length;
    }

    class VideoListHolder extends RecyclerView.ViewHolder {

        TextView mTitle;

        public VideoListHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
        }
    }
}
