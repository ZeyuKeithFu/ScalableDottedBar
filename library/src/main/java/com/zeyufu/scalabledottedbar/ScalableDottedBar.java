package com.zeyufu.scalabledottedbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ScalableDottedBar extends View {

    private List<ScalableDotDrawable> mDots;

    private int mColorSelected;
    private int mColorUnselected;
    private int mMaxInitial;
    private int mNumDots;
    private int mLevel;
    private int mCenterX;
    private int mCenterY;
    private float mRadius;
    private float mSpacing;
    private float mScaleMultiplier;

    // Current selected dot in the list
    private int currDot;
    // Current dots index range (from 0 to mNumDots - 1) that represented by the bar
    // Index is NOT the index in mDots
    private int currLeft;
    private int currRight;
    // Current left and right borders of initial sized dot in the list
    // Index is the index in mDots
    private int initialSizeLeft;
    private int initialSizeRight;
    private boolean needRescale;

    public ScalableDottedBar(Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ScalableDottedBar(Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@NonNull AttributeSet attrs) {
        // Init styles
        Context context = getContext();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScalableDottedBar);
        try {
            mColorSelected = ta.getColor(R.styleable.ScalableDottedBar_colorSelected, Color.BLUE);
            mColorUnselected = ta.getColor(R.styleable.ScalableDottedBar_colorUnselected, Color.DKGRAY);
            mNumDots = ta.getInteger(R.styleable.ScalableDottedBar_numDots, 5);
            mMaxInitial = ta.getInteger(R.styleable.ScalableDottedBar_maxInitial, 5);
            // For convenience, mLevel is the levels of scale except original scale
            mLevel = ta.getInteger(R.styleable.ScalableDottedBar_scaleLevel, 3) - 1;
            mRadius = ta.getDimension(R.styleable.ScalableDottedBar_android_radius, 8);
            mSpacing = ta.getDimension(R.styleable.ScalableDottedBar_android_spacing, 32);
            mScaleMultiplier = ta.getFloat(R.styleable.ScalableDottedBar_scaleMultiplier, 0.7f);
        } finally {
            ta.recycle();
        }
        mDots = new ArrayList<>();
        initDots();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (ScalableDotDrawable dot : mDots) {
            dot.draw(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;
        setDotBounds();
    }

    private void initDots() {
        // Initialize dots list
        //
        // Rescale strategy: always keep (mMaxInitial - mLevel) dots as original size
        // Max number of dots shown in bar: (mMaxInitial + mLevel)
        //
        // If rescale needed,
        // Number of dots, which is in original size is always (mMaxInitial - mLevel)
        // After every rescale move, need to update:
        // currDot, currLeft, currRight, initialSizeLeft, initialSizeRight
        mDots.clear();
        needRescale = mNumDots > mMaxInitial;
        float r = mRadius;
        for (int i = 0; i < Math.min(mNumDots, mMaxInitial); i++) {
            // Rescale dots if needed
            if (needRescale && i >= mMaxInitial - mLevel) {
                r *= mScaleMultiplier;
            }
            ScalableDotDrawable dot = new ScalableDotDrawable(mColorUnselected, r);
            mDots.add(dot);
        }
        currDot = -1;
        // Allow empty bar
        if (mNumDots > 0) {
            currDot = 0;
            currLeft = 0;
            currRight = mDots.size() - 1;
            initialSizeLeft = 0;
            initialSizeRight = needRescale ? mMaxInitial - mLevel - 1 : mNumDots - 1;
            selectDot(currDot);
        }
    }

    private void setDotBounds() {
        // Set dot bounds
        for (int i = 0; i < mDots.size(); i++) {
            mDots.get(i).setBounds(getBounds(i, initialSizeLeft, initialSizeRight, mDots.get(i).getRadius()));
        }
    }

    private Rect getBounds(int i, int l, int r, float radius) {
        // Get bounds based on relative position
        // i: dot index in dots list
        // l, r: left, right border of original sized dot
        int center = (l + r) / 2;
        int left = (int) (mCenterX + (i - center) * mSpacing - radius);
        int top = (int) (mCenterY - radius);
        int right = (int) (mCenterX + (i - center) * mSpacing + radius);
        int bottom = (int) (mCenterY + radius);
        if ((l + r) % 2 != 0) {
            left -= (int) mSpacing / 2;
            right -= (int) mSpacing / 2;
        }
        return new Rect(left, top, right, bottom);
    }

    private void updateRadius() {
        float r = mRadius;
        for (int i = 0; i < mDots.size(); i++) {
            if (i > initialSizeRight) {
                r *= mScaleMultiplier;
            }
            mDots.get(i).setRadius(r);
        }
        r = mRadius;
        for (int j = initialSizeLeft - 1; j >= 0; j--) {
            r *= mScaleMultiplier;
            mDots.get(j).setRadius(r);
        }
    }

    private void selectDot(int i) {
        mDots.get(i).setColor(mColorSelected);
    }

    private void unselectDot(int i) {
        mDots.get(i).setColor(mColorUnselected);
    }

    public int getNumberOfDots() {
        return mNumDots;
    }

    public int getMaxInitialDots() {
        return mMaxInitial;
    }

    public int getLevelOfScale() {
        return mLevel + 1;
    }

    public float getSpacing() {
        return mSpacing;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setNumDots(int n) {
        this.mNumDots = n;
    }

    public void setMaxInitial(int max) {
        mMaxInitial = max;
    }

    public void setScaleLevel(int level) {
        mLevel = level - 1;
    }

    public void setSpacing(float sp) {
        mSpacing = sp;
    }

    public void setRadius(float r) {
        mRadius = r;
    }

    public void setScaleMultiplier(int multiplier) {
        mScaleMultiplier = multiplier;
    }

    public void setColorSelected(int color) {
        mColorSelected = color;
    }

    public void setColorUnselected(int color) {
        mColorUnselected = color;
    }

    public void refresh() {
        /*
         * Refresh bar
         */
        initDots();
        setDotBounds();
        invalidate();
    }

    public void next() {
        /*
         * Select next dot and rescale if needed
         */
        if (currDot > -1 && currDot < mDots.size()-1) {
            unselectDot(currDot);
            if (needRescale && currDot >= initialSizeRight) {
                // When to add a new dot to right?
                // currRight has not reached end
                if (currRight < mNumDots - 1) {
                    mDots.add(new ScalableDotDrawable(mColorUnselected, mRadius));
                    currRight++;
                }
                // When to remove a dot at left?
                // There have been already mLevel dots on left
                if (initialSizeLeft >= mLevel) {
                    mDots.remove(0);
                    currDot--;
                    currLeft++;
                } else {
                    initialSizeLeft++;
                    initialSizeRight++;
                }
                updateRadius();
                setDotBounds();
            }
            currDot++;
            selectDot(currDot);
            invalidate();
        }
    }

    public void back() {
        /*
         * Select previous dot and rescale if needed
         */
        if (currDot > 0) {
            unselectDot(currDot);
            if (needRescale && currDot <= initialSizeLeft) {
                // When to add a new dot to left?
                // currLeft has not reached 0
                if (currLeft > 0) {
                    mDots.add(0, new ScalableDotDrawable(mColorUnselected, mRadius));
                    currDot++;
                    currLeft--;
                } else {
                    initialSizeLeft--;
                    initialSizeRight--;
                }
                // When to remove a dot at right?
                // There have been already mLevel dots on right
                if (mDots.size() - 1 - initialSizeRight > mLevel) {
                    mDots.remove(mDots.size()-1);
                    currRight--;
                }
                updateRadius();
                setDotBounds();
            }
            currDot--;
            selectDot(currDot);
            invalidate();
        }
    }
}
