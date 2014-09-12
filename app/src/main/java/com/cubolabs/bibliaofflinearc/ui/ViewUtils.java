package com.cubolabs.bibliaofflinearc.ui;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;

public class ViewUtils {
    public static <V extends View> Collection<V> findChildrenByClass(ViewGroup viewGroup, Class<V> clazz) {
        return gatherChildrenByClass(viewGroup, clazz, new ArrayList<V>());
    }

    private static <V extends View> Collection<V> gatherChildrenByClass(ViewGroup viewGroup, Class<V> clazz, Collection<V> childrenFound) {
        for (int i = 0; i < viewGroup.getChildCount(); i++)
        {
            final View child = viewGroup.getChildAt(i);
            if (clazz.isAssignableFrom(child.getClass())) {
                childrenFound.add((V)child);
            }
            if (child instanceof ViewGroup) {
                gatherChildrenByClass((ViewGroup) child, clazz, childrenFound);
            }
        }

        return childrenFound;
    }

    public static Boolean IsLastItem(ArrayList<String> arrayList, int position) {
        return position == arrayList.size()-1;
    }

    public static Boolean AboveHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

}
