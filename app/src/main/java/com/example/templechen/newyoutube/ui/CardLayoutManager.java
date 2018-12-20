package com.example.templechen.newyoutube.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class CardLayoutManager extends LinearLayoutManager {
    public CardLayoutManager(Context context) {
        super(context);
    }

    public CardLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        detachAndScrapAttachedViews(recycler);
        for (int i = 0; i < getItemCount(); i++) {
            View child = getChildAt(i);
            measureChildWithMargins(child, 0, 0);
            addView(child);
            int width = getDecoratedMeasuredWidth(child);
            int height = getDecoratedMeasuredHeight(child);
            layoutDecorated(child, 0, 0, width, height);
            if (i < getItemCount() -1) {
                child.setScaleX(0.8f);
                child.setScaleY(0.8f);
            }
        }
    }
}
