package com.cubolabs.bibliaofflinearc.data;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class BibliaDatabase extends SQLiteAssetHelper {
	//private static final String TAG = SQLiteAssetHelper.class.getSimpleName();
	private static final String DATABASE_NAME = "biblia_arc.db";
	private static final int DATABASE_VERSION = 1;

    private static BibliaDatabase instance;
    public synchronized static BibliaDatabase getInstance(Context context) {
        if(instance == null) {
            instance = new BibliaDatabase(context.getApplicationContext());
        }
        return instance;
    }

    private BibliaDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
