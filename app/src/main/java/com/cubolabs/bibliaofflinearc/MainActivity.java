package com.cubolabs.bibliaofflinearc;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cubolabs.bibliaofflinearc.ui.EditPreferences;
import com.cubolabs.bibliaofflinearc.ui.EditPreferencesHC;
import com.cubolabs.bibliaofflinearc.ui.LivrosListFragment;
import com.cubolabs.bibliaofflinearc.ui.MyMessageBox;
import com.cubolabs.bibliaofflinearc.ui.SearchResultsPopup;
import com.cubolabs.bibliaofflinearc.ui.ViewUtils;

public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
    private SearchView searchView;
    private SearchResultsPopup searchResultsPopup;
    private SharedPreferences preferences;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void changeTheme() {
        String themePreference = preferences.getString("themes_preference", "AppTheme");
        switch(themePreference) {
            case "AppThemeDark":
                setTheme(R.style.AppThemeDark);
                break;
            default:
                setTheme(R.style.AppTheme);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	 	try {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);

            changeTheme();

	        ActionBar actionBar = getSupportActionBar();
			actionBar.setTitle(R.string.app_title);
			
	        getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, LivrosListFragment.newInstance())
            .commit();
	 	}
	 	catch(Exception e) {
	 		MyMessageBox.ShowDialog(this, "MainActivity.onCreate", e.getMessage());
	 	}
    }

    @Override
    public void onBackPressed() {
        if (!searchResultsPopup.hide())
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }
        else {
            searchView = new SearchView(this);
            MenuItemCompat.setShowAsAction(searchItem,
                    MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW|MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
            MenuItemCompat.setActionView(searchItem, searchView);
        }

        for (TextView textView : ViewUtils.findChildrenByClass(searchView, TextView.class)) {
            textView.setTextColor(Color.WHITE);
        }

        searchResultsPopup = new SearchResultsPopup(this, searchView, searchItem);

        Boolean fullscreenPreference = preferences.getBoolean("fullscreen_preference", false);
        if(fullscreenPreference) {
            MenuItem fullScreenItem = menu.findItem(R.id.action_full_screen);
            goesFullScreen(fullScreenItem);
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchResultsPopup.submit(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        //Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        return false;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void goesFullScreen(MenuItem item) {
        item.setIcon(R.drawable.ic_action_return_from_full_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        if (ViewUtils.AboveKitkat()) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        preferences.edit().putBoolean("fullscreen_preference", true).commit();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void returnFromFullScreen(MenuItem item) {
        item.setIcon(R.drawable.ic_action_full_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (ViewUtils.AboveKitkat()) {
            getWindow().getDecorView().setSystemUiVisibility(0);
        }
        preferences.edit().putBoolean("fullscreen_preference", false).commit();
    }

    private void toggleFullScreen(MenuItem item) {
        boolean fullScreen =
                (getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
        if(fullScreen) {
            returnFromFullScreen(item);
        }
        else {
            goesFullScreen(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_search:
                searchView.setIconified(false);
                return true;
            case R.id.action_full_screen:
                toggleFullScreen(item);
                return true;
            case R.id.action_settings:
                if (Build.VERSION.SDK_INT< Build.VERSION_CODES.HONEYCOMB) {
                    startActivity(new Intent(this, EditPreferences.class));
                }
                else {
                    startActivity(new Intent(this, EditPreferencesHC.class));
                }
                return true;
        }
        return false;
    }

}
