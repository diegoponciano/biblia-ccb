package com.cubolabs.bibliaofflinearc.ui;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

public class FragmentUtils {
    public static boolean sDisableFragmentAnimations = false;

    public static void clearBackStack(Activity activity) {
        FragmentUtils.sDisableFragmentAnimations = true;
        ((AppCompatActivity)activity).getSupportFragmentManager().popBackStackImmediate(null, 0);
        FragmentUtils.sDisableFragmentAnimations = false;
    }
}