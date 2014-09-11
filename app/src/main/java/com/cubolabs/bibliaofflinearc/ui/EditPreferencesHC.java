package com.cubolabs.bibliaofflinearc.ui;

import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.cubolabs.bibliaofflinearc.R;

public class EditPreferencesHC extends PreferenceActivity {
    public static final String TAG = EditPreferencesHC.class.getSimpleName();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "EditPreferencesHC.onCreate");
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new BiblePreferencesFragment()).commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class BiblePreferencesFragment extends PreferenceFragment {
        private final static String TAG = BiblePreferencesFragment.class.getSimpleName();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d(TAG, "BiblePreferencesFragment.onCreate");
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
