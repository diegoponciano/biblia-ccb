package com.cubolabs.bibliaofflinearc.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.cubolabs.bibliaofflinearc.R;

public class EditPreferences extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        //addPreferencesFromResource(R.xml.preferences2);
    }
}