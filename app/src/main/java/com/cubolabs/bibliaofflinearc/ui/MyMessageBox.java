package com.cubolabs.bibliaofflinearc.ui;

import android.app.AlertDialog;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class MyMessageBox {
	public static void ShowDialog(FragmentActivity mainActivity, String method, String message) {
		Log.d("EXCEPTION: " + method,  message);

	    AlertDialog.Builder messageBox = new AlertDialog.Builder(mainActivity);
	    messageBox.setTitle(method);
	    messageBox.setMessage(message);
	    messageBox.setCancelable(false);
	    messageBox.setNeutralButton("OK", null);
	    messageBox.show();
	}
}
