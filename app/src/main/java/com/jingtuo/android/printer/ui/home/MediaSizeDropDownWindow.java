package com.jingtuo.android.printer.ui.home;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;

/**
 *
 */
public class MediaSizeDropDownWindow extends ListPopupWindow {

    public MediaSizeDropDownWindow(@NonNull Context context) {
        super(context);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, metrics);
        setHeight(height);
    }
}
