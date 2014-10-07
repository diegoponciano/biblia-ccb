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
import com.cubolabs.bibliaofflinearc.ui.NavigationDrawerFragment;
import com.cubolabs.bibliaofflinearc.ui.SearchResultsPopup;
import com.cubolabs.bibliaofflinearc.ui.ViewUtils;

public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
        // TODO: restore navigationDrawer
        //implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private SearchView searchView;
    private SearchResultsPopup searchResultsPopup;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	 	try {
            //mTitle = getTitle();

            // TODO: restore navigationDrawer
            //mNavigationDrawerFragment = (NavigationDrawerFragment)
	        //       getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

	        // Set up the drawer.
	        //mNavigationDrawerFragment.setUp(
	        //        R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
            preferences = PreferenceManager.getDefaultSharedPreferences(this);

	        ActionBar actionBar = getSupportActionBar();
			actionBar.setTitle(R.string.app_title);
			
	        getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, LivrosListFragment.newInstance())
            .commit();
	 	}
	 	catch(Exception e) {
	 		MyMessageBox.ShowDialog(this, "MainActivity.onCreate", e.getMessage());
	 	}
        
        //listview.setOnItemClickListener(listener);
        
        /*listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("NewApi")
			@Override
            public void onItemClick(AdapterView<?> parent, final View view,
                int position, long id) {
              final String item = (String) parent.getItemAtPosition(position);
              view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                    @SuppressLint("NewApi")
					@Override
                    public void run() {
                      list.remove(item);
                      adapter.notifyDataSetChanged();
                      view.setAlpha(1);
                    }
                  });
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        if (!searchResultsPopup.hide())
            super.onBackPressed();
    }

    // TODO: restore navigationDrawer
    //@Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction()
        //        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
        //        .commit();
    }
    
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
        actionBar.setTitle(R.string.app_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            //getMenuInflater().inflate(R.menu.main, menu);
            //restoreActionBar();
        //    return true;
        //}
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

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
