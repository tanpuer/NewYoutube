package com.example.templechen.newyoutube.video.transition;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.ViewGroup;
import com.example.templechen.newyoutube.video.AutoResizeTextureView;

public class TextureViewTransition extends Transition {

    private static final String TAG = "TextureViewTransition";

    private static final String TEXTURE_VIEW_WIDTH = "TEXTURE_VIEW_WIDTH";
    private static final String TEXTURE_VIEW_HEIGHT = "TEXTURE_VIEW_HEIGHT";
    private static final String TEXTURE_SCALE_MODE = "TEXTURE_SCALE_MODE";


    @Override
    public void captureStartValues(TransitionValues transitionValues) {
       setSizeAndPos(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        setSizeAndPos(transitionValues);
    }

    private void setSizeAndPos(TransitionValues transitionValues) {
        transitionValues.values.put(TEXTURE_VIEW_WIDTH, transitionValues.view.getWidth());
        transitionValues.values.put(TEXTURE_VIEW_HEIGHT, transitionValues.view.getHeight());
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }
        final int startWidth = (int) startValues.values.get(TEXTURE_VIEW_WIDTH);
        final int startHeight = (int) startValues.values.get(TEXTURE_VIEW_HEIGHT);

        final int endWidth = (int) endValues.values.get(TEXTURE_VIEW_WIDTH);
        final int endHeight = (int) endValues.values.get(TEXTURE_VIEW_HEIGHT);

        final AutoResizeTextureView autoResizeTextureView = (AutoResizeTextureView) endValues.view;
        int startScaleType = (int) startValues.values.get(TEXTURE_SCALE_MODE);
        int endScaleType = (int) endValues.values.get(TEXTURE_SCALE_MODE);

        ValueAnimator animator = ValueAnimator.ofObject(new MyTypeEvaluator(), new Params(startWidth, startHeight), new Params(endWidth, endHeight));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Params params = (Params) animation.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate: " + autoResizeTextureView.getWidth() + ", " + autoResizeTextureView.getHeight());

                autoResizeTextureView.updateTextureViewSize((int) params.width, (int) params.height);
            }
        });
        return animator;

    }

    public class Params {
        float width;
        float height;

        public Params(float width, float height) {
            this.width = width;
            this.height = height;
        }
    }

    public class MyTypeEvaluator implements TypeEvaluator<Params> {

        @Override
        public Params evaluate(float fraction, Params startValue, Params endValue) {
            return new Params(
                    startValue.width + (endValue.width-startValue.width) *fraction,
                    startValue.height + (endValue.height-startValue.height) *fraction
            );
        }
    }
}
