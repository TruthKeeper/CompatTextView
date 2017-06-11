package com.tk.compattextview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/06/07
 *     desc   : 减少shape、selector的xml维护成本；
 *              扩展drawableLeft大小、对齐方式、Tint着色、SVG支持，优化布局层次；
 * </pre>
 */
public class CompatTextView extends AppCompatTextView {
    public static final int[][] STATES = new int[][]{{android.R.attr.state_selected},
            {android.R.attr.state_pressed},
            {-android.R.attr.enabled},
            {}};
    /**
     * topLeft , topRight , bottomRight , bottomLeft
     */
    private float[] mCornerRadius = new float[4];
    private int mStrokeWidth;
    /**
     * enabled , pressed , selected , unenabled
     */
    private int[] mSolidColor = new int[4];
    private int[] mStrokeColor = new int[4];
    /**
     * left , top , right , bottom
     */
    private int[] mTint = new int[4];
    private Drawable[] mTintDrawable = new Drawable[4];
    /**
     * left , top , right , bottom
     */
    private int[] mDrawableAligh = new int[4];
    /**
     * enabled , pressed , selected , unenabled
     */
    private int[] mGradientStartColor = new int[4];
    private int[] mGradientEndColor = new int[4];
    private int[] mGradientDircetion = new int[4];
    private int mFadeDuring = 0;

    public CompatTextView(Context context) {
        super(context);
    }

    public CompatTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CompatTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TintTypedArray array = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.CompatTextView);
        //初始化圆角参数
        initRadius(array);
        //初始化边框参数
        initStroke(array);
        //初始化实心颜色参数
        initSolidColor(array);
        //初始化文本颜色参数
        initTextColor(array);
        //初始化Tint参数
        initTint(array);
        //初始化Tint Drawable参数
        initTintDrawable(array);
        //初始化Drawable对其方式参数
        initAligh(array);
        //初始化渐变参数
        initGradient(array);
        mFadeDuring = array.getInt(R.styleable.CompatTextView_ctv_fadeDuring, 0);
        array.recycle();

        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawables(null == mTintDrawable[0] ? drawables[0] : mTintDrawable[0],
                null == mTintDrawable[1] ? drawables[1] : mTintDrawable[1],
                null == mTintDrawable[2] ? drawables[2] : mTintDrawable[2],
                null == mTintDrawable[3] ? drawables[3] : mTintDrawable[3]);

        Drawable drawable = processDrawable();
        if (null != drawable) {
            setBackgroundDrawable(drawable);
        }
    }


    /**
     * init radius
     *
     * @param array
     */
    private void initRadius(TintTypedArray array) {
        mCornerRadius[0] = array.getDimension(R.styleable.CompatTextView_ctv_topLeftRadius, 0);
        mCornerRadius[1] = array.getDimension(R.styleable.CompatTextView_ctv_topRightRadius, 0);
        mCornerRadius[2] = array.getDimension(R.styleable.CompatTextView_ctv_bottomRightRadius, 0);
        mCornerRadius[3] = array.getDimension(R.styleable.CompatTextView_ctv_bottomLeftRadius, 0);
    }

    /**
     * init stroke
     *
     * @param array
     */
    private void initStroke(TintTypedArray array) {
        mStrokeWidth = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_strokeWidth, 0);
        mStrokeColor[0] = array.getColor(R.styleable.CompatTextView_ctv_strokeColor, Color.GRAY);
        mStrokeColor[1] = array.getColor(R.styleable.CompatTextView_ctv_strokePressedColor, Color.GRAY);
        mStrokeColor[2] = array.getColor(R.styleable.CompatTextView_ctv_strokeSelectedColor, Color.GRAY);
        mStrokeColor[3] = array.getColor(R.styleable.CompatTextView_ctv_strokeUnenabledColor, Color.GRAY);
    }

    /**
     * init solid color
     *
     * @param array
     */
    private void initSolidColor(TintTypedArray array) {
        mSolidColor[0] = array.getColor(R.styleable.CompatTextView_ctv_solidColor, 0);
        mSolidColor[1] = array.getColor(R.styleable.CompatTextView_ctv_solidPressedColor, 0);
        mSolidColor[2] = array.getColor(R.styleable.CompatTextView_ctv_solidSelectedColor, 0);
        mSolidColor[3] = array.getColor(R.styleable.CompatTextView_ctv_solidUnenabledColor, 0);
    }

    /**
     * init text color
     *
     * @param array
     */
    private void initTextColor(TintTypedArray array) {
        int[] colors = new int[4];
        colors[0] = array.getColor(R.styleable.CompatTextView_ctv_textColor, getTextColors().getDefaultColor());
        colors[1] = array.getColor(R.styleable.CompatTextView_ctv_textPressedColor, colors[0]);
        colors[2] = array.getColor(R.styleable.CompatTextView_ctv_textSelectedColor, colors[0]);
        colors[3] = array.getColor(R.styleable.CompatTextView_ctv_textUnenabledColor, colors[0]);
        setTextColor(new ColorStateList(STATES, colors));
    }

    /**
     * init tint color
     *
     * @param array
     */
    private void initTint(TintTypedArray array) {
        mTint[0] = array.getColor(R.styleable.CompatTextView_ctv_tintLeft, 0);
        mTint[1] = array.getColor(R.styleable.CompatTextView_ctv_tintTop, 0);
        mTint[2] = array.getColor(R.styleable.CompatTextView_ctv_tintRight, 0);
        mTint[3] = array.getColor(R.styleable.CompatTextView_ctv_tintBottom, 0);
    }

    /**
     * init tint drawable
     *
     * @param array
     */
    private void initTintDrawable(TintTypedArray array) {
        mTintDrawable[0] = array.getDrawable(R.styleable.CompatTextView_ctv_tintDrawableLeft);
        if (mTintDrawable[0] != null) {
            int width = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableLeftWidth, mTintDrawable[0].getIntrinsicWidth());
            int height = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableLeftHeight, mTintDrawable[0].getIntrinsicHeight());
            tintDrawable(mTint[0], mTintDrawable[0], width, height);
        }
        mTintDrawable[1] = array.getDrawable(R.styleable.CompatTextView_ctv_tintDrawableTop);
        if (mTintDrawable[1] != null) {
            int width = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableTopWidth, mTintDrawable[1].getIntrinsicWidth());
            int height = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableTopHeight, mTintDrawable[1].getIntrinsicHeight());
            tintDrawable(mTint[1], mTintDrawable[1], width, height);
        }
        mTintDrawable[2] = array.getDrawable(R.styleable.CompatTextView_ctv_tintDrawableRight);
        if (mTintDrawable[2] != null) {
            int width = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableRightWidth, mTintDrawable[2].getIntrinsicWidth());
            int height = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableRightHeight, mTintDrawable[2].getIntrinsicHeight());
            tintDrawable(mTint[2], mTintDrawable[2], width, height);
        }
        mTintDrawable[3] = array.getDrawable(R.styleable.CompatTextView_ctv_tintDrawableBottom);
        if (mTintDrawable[3] != null) {
            int width = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableBottomWidth, mTintDrawable[3].getIntrinsicWidth());
            int height = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableBottomHeight, mTintDrawable[3].getIntrinsicHeight());
            tintDrawable(mTint[3], mTintDrawable[3], width, height);
        }
    }

    /**
     * tint drawable
     *
     * @param tint
     * @param drawable
     * @param width
     * @param height
     */
    private static void tintDrawable(int tint, @NonNull Drawable drawable, int width, int height) {
        if (0 != tint) {
            drawable = DrawableCompat.wrap(drawable.mutate());
            DrawableCompat.setTint(drawable, tint);
        }
        drawable.setBounds(0, 0, width, height);
    }

    /**
     * init align of drawable
     *
     * @param array
     */
    private void initAligh(TintTypedArray array) {
        mDrawableAligh[0] = array.getInt(R.styleable.CompatTextView_ctv_drawableLeftAlign, 1);
        mDrawableAligh[1] = array.getInt(R.styleable.CompatTextView_ctv_drawableTopAlign, 1);
        mDrawableAligh[2] = array.getInt(R.styleable.CompatTextView_ctv_drawableRightAlign, 1);
        mDrawableAligh[3] = array.getInt(R.styleable.CompatTextView_ctv_drawableBottomAlign, 1);
    }

    @Override
    public void setCompoundDrawables(@Nullable final Drawable left, @Nullable final Drawable top,
                                     @Nullable final Drawable right, @Nullable final Drawable bottom) {
        //由于测量的值会有变动，所以先触发一次重绘
        super.setCompoundDrawables(left, top, right, bottom);
        post(new Runnable() {
            @Override
            public void run() {
                setInnerDrawable(left, top, right, bottom);
            }
        });
    }

    private void setInnerDrawable(final Drawable left, final Drawable top,
                                  final Drawable right, final Drawable bottom) {
        //drawable size must be smaller than total space
        if (null != left && left.getBounds().height() < getLineCount() * getLineHeight()) {
            int drawableTop = 0;
            switch (mDrawableAligh[0]) {
                case 0:
                    drawableTop = getLineHeight() - getLineCount() * getLineHeight() >> 1;
                    break;
                case 2:
                    drawableTop = -(getLineHeight() - getLineCount() * getLineHeight()) >> 1;
                    break;
                default:
                    break;

            }
            left.getBounds().bottom = left.getBounds().height() + drawableTop;
            left.getBounds().top = drawableTop;
        }
        if (null != top && top.getBounds().width() < getMeasuredWidth()) {
            int drawableLeft = 0;
            switch (mDrawableAligh[1]) {
                case 0:
                    drawableLeft = top.getBounds().width() - getMeasuredWidth() >> 1;
                    break;
                case 2:
                    drawableLeft = -(top.getBounds().width() - getMeasuredWidth()) >> 1;
                    break;
                default:
                    break;

            }
            top.getBounds().right = top.getBounds().width() + drawableLeft;
            top.getBounds().left = drawableLeft;
        }
        if (null != right && right.getBounds().height() < getLineCount() * getLineHeight()) {
            int drawableTop = 0;
            switch (mDrawableAligh[2]) {
                case 0:
                    drawableTop = getLineHeight() - getLineCount() * getLineHeight() >> 1;
                    break;
                case 2:
                    drawableTop = -(getLineHeight() - getLineCount() * getLineHeight()) >> 1;
                    break;
                default:
                    break;

            }
            right.getBounds().bottom = right.getBounds().height() + drawableTop;
            right.getBounds().top = drawableTop;
        }
        if (null != bottom && bottom.getBounds().width() < getMeasuredWidth()) {
            int drawableLeft = 0;
            switch (mDrawableAligh[3]) {
                case 0:
                    drawableLeft = bottom.getBounds().width() - getMeasuredWidth() >> 1;
                    break;
                case 2:
                    drawableLeft = -(bottom.getBounds().width() - getMeasuredWidth()) >> 1;
                    break;
                default:
                    break;

            }
            bottom.getBounds().right = bottom.getBounds().width() + drawableLeft;
            bottom.getBounds().left = drawableLeft;
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    /**
     * init gradient
     *
     * @param array
     */
    private void initGradient(TintTypedArray array) {
        mGradientStartColor[0] = array.getColor(R.styleable.CompatTextView_ctv_gradientStartColor, 0);
        mGradientStartColor[1] = array.getColor(R.styleable.CompatTextView_ctv_gradientStartPressedColor, 0);
        mGradientStartColor[2] = array.getColor(R.styleable.CompatTextView_ctv_gradientStartSelectedColor, 0);
        mGradientStartColor[3] = array.getColor(R.styleable.CompatTextView_ctv_gradientStartUnenabledColor, 0);

        mGradientEndColor[0] = array.getColor(R.styleable.CompatTextView_ctv_gradientEndColor, 0);
        mGradientEndColor[1] = array.getColor(R.styleable.CompatTextView_ctv_gradientEndPressedColor, 0);
        mGradientEndColor[2] = array.getColor(R.styleable.CompatTextView_ctv_gradientEndSelectedColor, 0);
        mGradientEndColor[3] = array.getColor(R.styleable.CompatTextView_ctv_gradientEndUnenabledColor, 0);

        mGradientDircetion[0] = array.getInt(R.styleable.CompatTextView_ctv_gradientDirection, 0);
        mGradientDircetion[1] = array.getInt(R.styleable.CompatTextView_ctv_gradientDirectionPressed, 0);
        mGradientDircetion[2] = array.getInt(R.styleable.CompatTextView_ctv_gradientDirectionSelected, 0);
        mGradientDircetion[3] = array.getInt(R.styleable.CompatTextView_ctv_gradientDirectionUnenabled, 0);
    }

    /**
     * process drawable
     *
     * @return StateListDrawable or null
     */
    private Drawable processDrawable() {
        StateListDrawable drawable = new StateListDrawable();

        float[] f = new float[]{mCornerRadius[0], mCornerRadius[0],
                mCornerRadius[1], mCornerRadius[1],
                mCornerRadius[2], mCornerRadius[2],
                mCornerRadius[3], mCornerRadius[3]};
        GradientDrawable enable = null;
        if (0 != mGradientStartColor[0] && 0 != mGradientEndColor[0]) {
            enable = new GradientDrawable(processOrientation(mGradientDircetion[0]),
                    new int[]{mGradientStartColor[0], mGradientEndColor[0]});
            enable.setCornerRadii(f);
            enable.setStroke(mStrokeWidth, mStrokeColor[0]);
        } else if (0 != mSolidColor[0]) {
            enable = new GradientDrawable();
            enable.setColor(mSolidColor[0]);
            enable.setCornerRadii(f);
            enable.setStroke(mStrokeWidth, mStrokeColor[0]);
        }

        GradientDrawable pressed = null;
        if (0 != mGradientStartColor[1] && 0 != mGradientEndColor[1]) {
            pressed = new GradientDrawable(processOrientation(mGradientDircetion[1]),
                    new int[]{mGradientStartColor[1], mGradientEndColor[1]});
            pressed.setCornerRadii(f);
            pressed.setStroke(mStrokeWidth, mStrokeColor[1]);
        } else if (0 != mSolidColor[1]) {
            pressed = new GradientDrawable();
            pressed.setColor(mSolidColor[1]);
            pressed.setCornerRadii(f);
            pressed.setStroke(mStrokeWidth, mStrokeColor[1]);
        }

        GradientDrawable selected = null;
        if (0 != mGradientStartColor[2] && 0 != mGradientEndColor[2]) {
            selected = new GradientDrawable(processOrientation(mGradientDircetion[2]),
                    new int[]{mGradientStartColor[2], mGradientEndColor[2]});
            selected.setCornerRadii(f);
            selected.setStroke(mStrokeWidth, mStrokeColor[2]);
        } else if (0 != mSolidColor[2]) {
            selected = new GradientDrawable();
            selected.setColor(mSolidColor[2]);
            selected.setCornerRadii(f);
            selected.setStroke(mStrokeWidth, mStrokeColor[2]);
        }
        GradientDrawable unenabled = null;
        if (0 != mGradientStartColor[3] && 0 != mGradientEndColor[3]) {
            unenabled = new GradientDrawable(processOrientation(mGradientDircetion[3]),
                    new int[]{mGradientStartColor[3], mGradientEndColor[3]});
            unenabled.setCornerRadii(f);
            unenabled.setStroke(mStrokeWidth, mStrokeColor[3]);
        } else if (0 != mSolidColor[3]) {
            unenabled = new GradientDrawable();
            unenabled.setColor(mSolidColor[3]);
            unenabled.setCornerRadii(f);
            unenabled.setStroke(mStrokeWidth, mStrokeColor[3]);
        }

        if (null != selected) {
            drawable.addState(STATES[0], selected);
        }
        if (null != pressed) {
            drawable.addState(STATES[1], pressed);
        }
        if (null != unenabled) {
            drawable.addState(STATES[2], unenabled);
        }
        if (null != enable) {
            drawable.addState(STATES[3], enable);
        }
        DrawableContainer.DrawableContainerState state = ((DrawableContainer.DrawableContainerState) drawable.getConstantState());
        if (state == null || 0 == state.getChildCount()) {
            return null;
        }
        drawable.setEnterFadeDuration(mFadeDuring);
        drawable.setExitFadeDuration(mFadeDuring);
        return drawable;
    }

    /**
     * process orientation
     *
     * @param direction
     * @return
     */
    private static GradientDrawable.Orientation processOrientation(int direction) {
        switch (direction) {
            case 0:
                return GradientDrawable.Orientation.TOP_BOTTOM;
            case 1:
                return GradientDrawable.Orientation.LEFT_RIGHT;
            case 2:
                return GradientDrawable.Orientation.TL_BR;
            case 3:
                return GradientDrawable.Orientation.BL_TR;
            default:
                return GradientDrawable.Orientation.TOP_BOTTOM;
        }
    }

}
