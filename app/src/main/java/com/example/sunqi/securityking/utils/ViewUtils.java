package com.example.sunqi.securityking.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;

public class ViewUtils {

    public static final int SCREEN_WIDTH_480 = 480;

    public static boolean isMinHeightScreen(Context context) {
        return getScreenHeight(context) <= 900;
    }

    /**
     * 获取屏幕宽度(px)
     *
     * @author Wangzhengyi
     */
    public static int getScreenWidth(Context context) {
        if (context == null || context.getResources() == null || context.getResources().getDisplayMetrics() == null) {
            return SCREEN_WIDTH_480;
        }
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度(px)
     *
     * @author Wangzhengyi
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getStatusBarHeight2(Context context) {
        int statusBarHeight2 = -1;
        if (statusBarHeight2 == -1) {
            try {
                Class<?> cl = Class.forName("com.android.internal.R$dimen");
                Object obj = cl.newInstance();
                Field field = cl.getField("status_bar_height");

                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight2 = context.getResources()
                        .getDimensionPixelSize(x); // 状态栏的
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight2;
    }

    /**
     * 获取屏幕实际显示区域高度(不包含状态栏)
     *
     * @return
     */
    public static int getDisplayAreaHeight(Context context) {
        return getScreenHeight(context) - getStatusBarHeight2(context);
    }

    /**
     * dip 转 px
     *
     * @author Wangzhengyi
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void setText(TextView v, String text) {
        if (TextUtils.isEmpty(text)) {
            setViewVisibility(v, View.GONE);
        } else {
            setViewVisibility(v, View.VISIBLE);
            v.setText(text);
        }
    }

    public static void setViewVisibility(View view, int visibility) {
        if (null != view && view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    /**
     * 修复使用9图作背景重置Padding问题
     *
     * @param view
     * @param resId
     */
    public static void setBackgroundAndKeepPadding(View view, int resId) {
        if (view == null)
            return;
        int left = view.getPaddingLeft();
        int top = view.getPaddingTop();
        int right = view.getPaddingRight();
        int bottom = view.getPaddingBottom();

        view.setBackgroundResource(resId);
        view.setPadding(left, top, right, bottom);
    }

    public static Bitmap getBitmap(View view, int width, int height) {
        Bitmap result = null;
        try {
            result = Bitmap
                    .createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            view.draw(canvas);

        } catch (Exception e) {
        }

        return result;
    }

    /**
     * 获取view的截图bitmap
     *
     * @param view
     * @return
     */
    public static Bitmap getBitmap(View view) {
        Bitmap result = null;
        try {
            int width = view.getWidth();
            int height = view.getHeight();

            result = Bitmap
                    .createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            view.draw(canvas);

        } catch (Exception e) {
        }

        return result;
    }

    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

}
