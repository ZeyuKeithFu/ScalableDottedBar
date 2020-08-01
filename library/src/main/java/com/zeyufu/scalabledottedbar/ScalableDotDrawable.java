package com.zeyufu.scalabledottedbar;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ScalableDotDrawable extends Drawable {

    private Paint mPaint;
    private float mRadius;

    public ScalableDotDrawable(final int color, final float radius) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRadius = radius;
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), mRadius, mPaint);
    }

    @Override
    public void setAlpha(int i) {
        mPaint.setAlpha(i);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }



    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }
}
