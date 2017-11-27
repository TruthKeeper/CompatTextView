package com.tk.compattextview;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.util.Arrays;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/06/07
 *     desc   : 支持shape的solid、stroke、gradient、radius的配置；
 *              支持enabled、pressed、selected、unenabled共计4种状态的配置；
 *              不再需要写大量的shape、selector文件配置；
 *              支持上下左右的drawable大小配置，SVG支持、Tint着色支持；
 *              支持上下左右的drawable的对齐方式配置；
 *              5.0+配置pressed时点击涟漪效果；
 * </pre>
 */
public class CompatTextView extends AppCompatTextView {
    public static final int[][] STATES = new int[][]{{android.R.attr.state_selected},
            {android.R.attr.state_pressed},
            {-android.R.attr.state_enabled},
            {}};
    public static final int DEFAULT_Z_DURING = 120;
    public static final int DEFAULT_Z_MAX_LIFT = 8;
    private static final int NULL = -2;
    /**
     * topLeft , topRight , bottomRight , bottomLeft
     */
    private float[] mCornerRadius;
    private int mStrokeWidth;
    /**
     * enabled , pressed , selected , disabled
     */
    private int[] mSolidColor;
    private int[] mStrokeColor;
    /**
     * left , top , right , bottom
     */
    private int[] mTint;
    private Drawable[] mTintDrawable;
    /**
     * left , top , right , bottom
     */
    private int[] mDrawableAlign;
    /**
     * enabled , pressed , selected , disabled
     */
    private int[] mGradientStartColor;
    private int[] mGradientCenterColor;
    private int[] mGradientEndColor;
    /**
     * gradient directions
     * enabled , pressed , selected , disabled
     */
    private int[] mGradientDirection;
    /**
     * SelectDrawable FadeDuration
     */
    private int mFadeDuring = 0;
    /**
     * in pressed config
     */
    private boolean mRipple = true;
    private boolean mZ = false;
    private int mZDuring = DEFAULT_Z_DURING;
    private int mZMaxLift = DEFAULT_Z_MAX_LIFT;

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
        mCornerRadius = new float[4];
        mSolidColor = new int[4];
        mStrokeColor = new int[4];
        mTint = new int[4];
        mTintDrawable = new Drawable[4];
        mDrawableAlign = new int[4];
        mGradientStartColor = new int[4];
        mGradientCenterColor = new int[4];
        mGradientEndColor = new int[4];
        mGradientDirection = new int[4];

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CompatTextView);
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
        initAlign(array);
        //初始化渐变参数
        initGradient(array);
        //初始化5.0+参数
        initLollipop(array);
        mFadeDuring = array.getInt(R.styleable.CompatTextView_ctv_fadeDuring, 0);

        array.recycle();

        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawables(null == mTintDrawable[0] ? drawables[0] : mTintDrawable[0],
                null == mTintDrawable[1] ? drawables[1] : mTintDrawable[1],
                null == mTintDrawable[2] ? drawables[2] : mTintDrawable[2],
                null == mTintDrawable[3] ? drawables[3] : mTintDrawable[3]);

        Drawable drawable = processBackgroundDrawable();
        if (null != drawable) {
            setBackgroundDrawable(drawable);
        }
        processZ();
    }

    private void processZ() {
        if (mZ && Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator animator = new StateListAnimator();
            ObjectAnimator lift = ObjectAnimator.ofFloat(this, "translationZ", 0, mZMaxLift)
                    .setDuration(mZDuring);
            ObjectAnimator drop = ObjectAnimator.ofFloat(this, "translationZ", 0)
                    .setDuration(mZDuring);

            animator.addState(new int[]{STATES[1][0], -STATES[2][0]}, lift);
            animator.addState(STATES[3], drop);
            setStateListAnimator(animator);
        }
    }


    /**
     * init radius
     *
     * @param array
     */
    private void initRadius(TypedArray array) {
        float r = array.getDimension(R.styleable.CompatTextView_ctv_radius, 0);
        if (r > 0) {
            Arrays.fill(mCornerRadius, r);
            return;
        }
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
    private void initStroke(TypedArray array) {
        mStrokeWidth = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_strokeWidth, 0);
        // default color
        mStrokeColor[0] = array.getColor(R.styleable.CompatTextView_ctv_strokeColor, Color.GRAY);
        mStrokeColor[1] = array.getColor(R.styleable.CompatTextView_ctv_strokePressedColor, mStrokeColor[0]);
        mStrokeColor[2] = array.getColor(R.styleable.CompatTextView_ctv_strokeSelectedColor, mStrokeColor[0]);
        mStrokeColor[3] = array.getColor(R.styleable.CompatTextView_ctv_strokeDisabledColor, mStrokeColor[0]);
    }

    /**
     * init solid color
     *
     * @param array
     */
    private void initSolidColor(TypedArray array) {
        mSolidColor[0] = array.getColor(R.styleable.CompatTextView_ctv_solidColor, NULL);
        mSolidColor[1] = array.getColor(R.styleable.CompatTextView_ctv_solidPressedColor, NULL);
        mSolidColor[2] = array.getColor(R.styleable.CompatTextView_ctv_solidSelectedColor, NULL);
        mSolidColor[3] = array.getColor(R.styleable.CompatTextView_ctv_solidDisabledColor, NULL);
    }

    /**
     * init text color
     *
     * @param array
     */
    private void initTextColor(TypedArray array) {
        int[] colors = new int[4];
        colors[3] = array.getColor(R.styleable.CompatTextView_ctv_textColor, getTextColors().getDefaultColor());
        colors[1] = array.getColor(R.styleable.CompatTextView_ctv_textPressedColor, colors[3]);
        colors[0] = array.getColor(R.styleable.CompatTextView_ctv_textSelectedColor, colors[3]);
        colors[2] = array.getColor(R.styleable.CompatTextView_ctv_textDisabledColor, colors[3]);
        setTextColor(new ColorStateList(STATES, colors));
    }

    /**
     * init tint color
     *
     * @param array
     */
    private void initTint(TypedArray array) {
        mTint[0] = array.getColor(R.styleable.CompatTextView_ctv_tintLeft, NULL);
        mTint[1] = array.getColor(R.styleable.CompatTextView_ctv_tintTop, NULL);
        mTint[2] = array.getColor(R.styleable.CompatTextView_ctv_tintRight, NULL);
        mTint[3] = array.getColor(R.styleable.CompatTextView_ctv_tintBottom, NULL);
    }

    /**
     * init tint drawable
     *
     * @param array
     */
    private void initTintDrawable(TypedArray array) {
        mTintDrawable[0] = TintUtils.getTintDrawable(getContext(), array, R.styleable.CompatTextView_ctv_tintDrawableLeft);
        if (null != mTintDrawable[0]) {
            int width = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableLeftWidth, mTintDrawable[0].getIntrinsicWidth());
            int height = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableLeftHeight, mTintDrawable[0].getIntrinsicHeight());
            mTintDrawable[0] = wrapperTintDrawable(mTint[0], mTintDrawable[0], width, height);
        }
        mTintDrawable[1] = TintUtils.getTintDrawable(getContext(), array, R.styleable.CompatTextView_ctv_tintDrawableTop);
        if (null != mTintDrawable[1]) {
            int width = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableTopWidth, mTintDrawable[1].getIntrinsicWidth());
            int height = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableTopHeight, mTintDrawable[1].getIntrinsicHeight());
            mTintDrawable[1] = wrapperTintDrawable(mTint[1], mTintDrawable[1], width, height);
        }
        mTintDrawable[2] = TintUtils.getTintDrawable(getContext(), array, R.styleable.CompatTextView_ctv_tintDrawableRight);
        if (null != mTintDrawable[2]) {
            int width = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableRightWidth, mTintDrawable[2].getIntrinsicWidth());
            int height = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableRightHeight, mTintDrawable[2].getIntrinsicHeight());
            mTintDrawable[2] = wrapperTintDrawable(mTint[2], mTintDrawable[2], width, height);
        }
        mTintDrawable[3] = TintUtils.getTintDrawable(getContext(), array, R.styleable.CompatTextView_ctv_tintDrawableBottom);
        if (null != mTintDrawable[3]) {
            int width = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableBottomWidth, mTintDrawable[3].getIntrinsicWidth());
            int height = array.getDimensionPixelOffset(R.styleable.CompatTextView_ctv_tintDrawableBottomHeight, mTintDrawable[3].getIntrinsicHeight());
            mTintDrawable[3] = wrapperTintDrawable(mTint[3], mTintDrawable[3], width, height);
        }
    }


    /**
     * tint drawable
     *
     * @param color
     * @param drawable
     * @param width
     * @param height
     */
    private Drawable wrapperTintDrawable(int color, @NonNull Drawable drawable, int width, int height) {
        if (color != NULL) {
            drawable = TintUtils.tint(drawable, color);
        }
        drawable.setBounds(0, 0, width, height);
        return drawable;
    }

    /**
     * init align of drawable
     *
     * @param array
     */
    private void initAlign(TypedArray array) {
        mDrawableAlign[0] = array.getInt(R.styleable.CompatTextView_ctv_drawableLeftAlign, 1);
        mDrawableAlign[1] = array.getInt(R.styleable.CompatTextView_ctv_drawableTopAlign, 1);
        mDrawableAlign[2] = array.getInt(R.styleable.CompatTextView_ctv_drawableRightAlign, 1);
        mDrawableAlign[3] = array.getInt(R.styleable.CompatTextView_ctv_drawableBottomAlign, 1);
    }

    @Override
    public void setCompoundDrawables(@Nullable final Drawable left, @Nullable final Drawable top,
                                     @Nullable final Drawable right, @Nullable final Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        post(new Runnable() {
            @Override
            public void run() {
                //由于对齐方式需要测量结果
                offsetDrawable(left, top, right, bottom);
            }
        });
    }

    /**
     * 根据对其方式偏移Drawable
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    private void offsetDrawable(final Drawable left, final Drawable top,
                                final Drawable right, final Drawable bottom) {
        //drawable size must be smaller than total space
        if (null != left && left.getBounds().height() < getLineCount() * getLineHeight()) {
            int drawableTop = 0;
            switch (mDrawableAlign[0]) {
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
            switch (mDrawableAlign[1]) {
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
            switch (mDrawableAlign[2]) {
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
            switch (mDrawableAlign[3]) {
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
    }

    /**
     * init gradient
     *
     * @param array
     */
    private void initGradient(TypedArray array) {
        mGradientStartColor[0] = array.getColor(R.styleable.CompatTextView_ctv_gradientStartColor, NULL);
        mGradientStartColor[1] = array.getColor(R.styleable.CompatTextView_ctv_gradientStartPressedColor, NULL);
        mGradientStartColor[2] = array.getColor(R.styleable.CompatTextView_ctv_gradientStartSelectedColor, NULL);
        mGradientStartColor[3] = array.getColor(R.styleable.CompatTextView_ctv_gradientStartDisabledColor, NULL);

        mGradientCenterColor[0] = array.getColor(R.styleable.CompatTextView_ctv_gradientCenterColor, NULL);
        mGradientCenterColor[1] = array.getColor(R.styleable.CompatTextView_ctv_gradientCenterPressedColor, NULL);
        mGradientCenterColor[2] = array.getColor(R.styleable.CompatTextView_ctv_gradientCenterSelectedColor, NULL);
        mGradientCenterColor[3] = array.getColor(R.styleable.CompatTextView_ctv_gradientCenterDisabledColor, NULL);

        mGradientEndColor[0] = array.getColor(R.styleable.CompatTextView_ctv_gradientEndColor, NULL);
        mGradientEndColor[1] = array.getColor(R.styleable.CompatTextView_ctv_gradientEndPressedColor, NULL);
        mGradientEndColor[2] = array.getColor(R.styleable.CompatTextView_ctv_gradientEndSelectedColor, NULL);
        mGradientEndColor[3] = array.getColor(R.styleable.CompatTextView_ctv_gradientEndDisabledColor, NULL);

        mGradientDirection[0] = array.getInt(R.styleable.CompatTextView_ctv_gradientDirection, 0);
        mGradientDirection[1] = array.getInt(R.styleable.CompatTextView_ctv_gradientDirectionPressed, 0);
        mGradientDirection[2] = array.getInt(R.styleable.CompatTextView_ctv_gradientDirectionSelected, 0);
        mGradientDirection[3] = array.getInt(R.styleable.CompatTextView_ctv_gradientDirectionDisabled, 0);
    }

    /**
     * @param array
     */
    private void initLollipop(TypedArray array) {
        mRipple = array.getBoolean(R.styleable.CompatTextView_ctv_ripple, true);

        mZ = array.getBoolean(R.styleable.CompatTextView_ctv_z, false);
        mZDuring = array.getInt(R.styleable.CompatTextView_ctv_z_during, DEFAULT_Z_DURING);
        mZMaxLift = array.getInt(R.styleable.CompatTextView_ctv_z_max_lift, DEFAULT_Z_MAX_LIFT);
    }

    /**
     * process drawable
     *
     * @return StateListDrawable or null
     */
    private Drawable processBackgroundDrawable() {
        if (null == mCornerRadius) {
            //// TODO: 2017/6/20 setEnabled和setSelected会比构造函数先执行？
            return null;
        }
        GradientDrawable enable = generatePartDrawable(0);

        if (isEnabled()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP
                    && mRipple
                    && (!isSelected())
                    && (NULL != mSolidColor[1])
                    && (null != enable)) {
                return new RippleDrawable(ColorStateList.valueOf(mSolidColor[1]), enable, null);
            }
        }
        GradientDrawable pressed = generatePartDrawable(1);
        GradientDrawable selected = generatePartDrawable(2);
        GradientDrawable disabled = generatePartDrawable(3);
        StateListDrawable stateListDrawable = new StateListDrawable();
        if (null != selected) {
            stateListDrawable.addState(STATES[0], selected);
        }
        if (null != pressed) {
            stateListDrawable.addState(STATES[1], pressed);
        }
        if (null != disabled) {
            stateListDrawable.addState(STATES[2], disabled);
        }
        if (null != enable) {
            stateListDrawable.addState(STATES[3], enable);
        }
        DrawableContainer.DrawableContainerState state = ((DrawableContainer.DrawableContainerState) stateListDrawable.getConstantState());
        if (null == state || 0 == state.getChildCount()) {
            //no CompatTextView config
            return null;
        }
        stateListDrawable.setEnterFadeDuration(mFadeDuring);
        stateListDrawable.setExitFadeDuration(mFadeDuring);

        return stateListDrawable;
    }

    /**
     * 生成每种模式下的Drawable
     *
     * @param index
     * @return
     */
    private GradientDrawable generatePartDrawable(int index) {
        GradientDrawable drawable = null;
        float[] f = new float[]{mCornerRadius[0], mCornerRadius[0],
                mCornerRadius[1], mCornerRadius[1],
                mCornerRadius[2], mCornerRadius[2],
                mCornerRadius[3], mCornerRadius[3]};
        if (NULL != mGradientStartColor[index] || NULL != mGradientCenterColor[index] || NULL != mGradientEndColor[index]) {
            //Gradient
            int[] colors = null;
            if (NULL == mGradientCenterColor[index]) {
                colors = new int[]{NULL == mGradientStartColor[index] ? Color.TRANSPARENT : mGradientStartColor[index],
                        NULL == mGradientEndColor[index] ? Color.TRANSPARENT : mGradientEndColor[index]};
            } else {
                colors = new int[]{NULL == mGradientStartColor[index] ? Color.TRANSPARENT : mGradientStartColor[index],
                        NULL == mGradientCenterColor[index] ? Color.TRANSPARENT : mGradientCenterColor[index],
                        NULL == mGradientEndColor[index] ? Color.TRANSPARENT : mGradientEndColor[index]};
            }
            drawable = new GradientDrawable(processOrientation(mGradientDirection[index]), colors);
            drawable.setCornerRadii(f);
            drawable.setStroke(mStrokeWidth, mStrokeColor[index]);
        } else if (NULL != mSolidColor[index]) {
            //Solid
            drawable = new GradientDrawable();
            drawable.setColor(mSolidColor[index]);
            drawable.setCornerRadii(f);
            drawable.setStroke(mStrokeWidth, mStrokeColor[index]);
        }
        return drawable;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP
                && mRipple
                && (!isSelected())) {
            //涟漪模式下的适配
            Drawable drawable = processBackgroundDrawable();
            if (null != drawable) {
                setBackgroundDrawable(drawable);
            }
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP
                && mRipple) {
            //涟漪模式下的适配
            Drawable drawable = processBackgroundDrawable();
            if (null != drawable) {
                setBackgroundDrawable(drawable);
            }
        }
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
