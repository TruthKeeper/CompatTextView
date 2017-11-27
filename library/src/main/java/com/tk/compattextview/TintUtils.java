package com.tk.compattextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/11/27
 *     desc   : 着色工具类
 * </pre>
 */
public final class TintUtils {
    private TintUtils() {
        throw new IllegalStateException();
    }

    /**
     * 获取Drawable , SVG的兼容处理 , 代替{@link android.support.v7.widget.TintTypedArray}
     *
     * @param context
     * @param array
     * @param index
     * @return
     */
    @Nullable
    public static Drawable getTintDrawable(@NonNull Context context, @NonNull TypedArray array, @StyleableRes int index) {
        int resourceId = array.getResourceId(index, -1);
        return resourceId == -1 ? null : ContextCompat.getDrawable(context, resourceId);
    }

    /**
     * 着色
     *
     * @param drawable
     * @param color
     */
    public static Drawable tint(@NonNull Drawable drawable, @ColorInt int color) {
        Drawable d = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTint(d, color);
        return d;
    }
}