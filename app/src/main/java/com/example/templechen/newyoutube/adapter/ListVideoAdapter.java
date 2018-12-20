package com.example.templechen.newyoutube.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.example.templechen.newyoutube.R;
import com.example.templechen.newyoutube.video.MediaPlayerTool;
import com.example.templechen.newyoutube.video.PlayTextureView;

public class ListVideoAdapter extends RecyclerView.Adapter<ListVideoAdapter.ListVideoViewHolder> {

    private Context mContext;
    private String[] mData;

    public ListVideoAdapter(Context mContext, String[] mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ListVideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video, viewGroup, false);
        return new ListVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListVideoViewHolder listVideoViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public class ListVideoViewHolder extends RecyclerView.ViewHolder {

        public PlayTextureView mPlayTextureView;

        public ListVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            mPlayTextureView = itemView.findViewById(R.id.play_texture_view);
        }
    }

}
