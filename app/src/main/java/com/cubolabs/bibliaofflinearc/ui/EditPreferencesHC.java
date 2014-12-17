package com.cubolabs.bibliaofflinearc.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.cubolabs.bibliaofflinearc.MainActivity;
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
    public static class BiblePreferencesFragment extends PreferenceFragment  implements
            SharedPreferences.OnSharedPreferenceChangeListener{
        private final static String TAG = BiblePreferencesFragment.class.getSimpleName();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d(TAG, "BiblePreferencesFragment.onCreate");
            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

        private void restartActivity() {
            getActivity().finish();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("themes_preference")) {
                restartActivity();
            }
        }

    }
}
