package com.sam.lib.widget.listener;

import android.animation.TimeInterpolator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * 通过设置view的setOnTouchListener，
 * 实现点击时变小一下松开时还原的效果
 * @author SamWang
 * @date 2015/12/25	14:39
 */
public class OnClickAnimTouchListener implements View.OnTouchListener {

    /**
     * 先快后慢的动画效果,可自行替换其它效果
     */
    private TimeInterpolator interpolator= new DecelerateInterpolator();
    /**
     * 点击时缩小的比例
     */
    private static final float scale = 0.7f;
    /**
     * 点击动画持续时间
     */
    private static final int duration = 150;

    public void setInterpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.animate().scaleX(scale).scaleY(scale).setDuration(duration).setInterpolator(interpolator);
                v.setPressed(true);
                break;

            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                boolean isInside = (x > 0 && x < v.getWidth() && y > 0 && y < v.getHeight());
                if (v.isPressed() != isInside) {
                    v.setPressed(isInside);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                v.setPressed(false);
                v.animate().scaleX(1).scaleY(1).setInterpolator(interpolator);
                break;
            case MotionEvent.ACTION_UP:
                v.animate().scaleX(1).scaleY(1).setInterpolator(interpolator);
                if (v.isPressed()) {
                    v.performClick();
                    v.setPressed(false);
                }
                break;
        }
        return true;
    }
}
