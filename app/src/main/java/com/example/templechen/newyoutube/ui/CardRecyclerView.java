package com.example.templechen.newyoutube.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CardRecyclerView extends RecyclerView {
    public CardRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CardRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        View topView = getChildAt(getChildCount() - 1);
        float touchX = e.getX();
        float touchY = e.getY();
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                break;
            }
            case MotionEvent.ACTION_UP:{
                break;
            }
        }
        return super.onTouchEvent(e);
    }
}
