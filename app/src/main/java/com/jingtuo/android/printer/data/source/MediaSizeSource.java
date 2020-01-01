package com.jingtuo.android.printer.data.source;

import android.print.PrintAttributes;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * 通过反射获取系统自带的MediaSize
 *
 * @author JingTuo
 */
public class MediaSizeSource implements SingleOnSubscribe<List<PrintAttributes.MediaSize>> {

    private static final String TAG = MediaSizeSource.class.getSimpleName();

    @Override
    public void subscribe(SingleEmitter<List<PrintAttributes.MediaSize>> emitter) throws Exception {
        Field[] fields = PrintAttributes.MediaSize.class.getDeclaredFields();
        List<PrintAttributes.MediaSize> mediaSizes = new ArrayList<>();
        int length = fields.length;
        for (int i = 0; i < length; i++) {
            Field field = fields[i];
            int modifiers = field.getModifiers();
            if (Modifier.isPublic(modifiers)
                    && Modifier.isStatic(modifiers)
                    && Modifier.isFinal(modifiers)) {
                String name = field.getName();
                if (!name.contains("UNKNOWN")) {
                    if (field.getDeclaringClass().equals(PrintAttributes.MediaSize.class)) {
                        try {
                            PrintAttributes.MediaSize size = (PrintAttributes.MediaSize) field.get(null);
                            mediaSizes.add(size);
                        } catch (IllegalAccessException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }
            }
        }
        Collections.sort(mediaSizes, new Comparator<PrintAttributes.MediaSize>() {
            @Override
            public int compare(PrintAttributes.MediaSize o1, PrintAttributes.MediaSize o2) {
                if (o1.getWidthMils() > o2.getWidthMils()) {
                    return 1;
                }
                if (o1.getWidthMils() < o2.getWidthMils()) {
                    return -1;
                }
                return 0;
            }
        });
        emitter.onSuccess(mediaSizes);
    }
}
