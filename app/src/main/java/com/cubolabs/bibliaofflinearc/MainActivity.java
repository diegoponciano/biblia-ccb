package com.cubolabs.bibliaofflinearc;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cubolabs.bibliaofflinearc.ui.LivrosListFragment;
import com.cubolabs.bibliaofflinearc.ui.MyMessageBox;
import com.cubolabs.bibliaofflinearc.ui.NavigationDrawerFragment;
import com.cubolabs.bibliaofflinearc.ui.SearchResultsPopup;

import java.util.ArrayList;
import java.util.Collection;

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
    private SearchView mSearchView;
    private SearchResultsPopup searchResultsPopup;

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
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);

        for (TextView textView : findChildrenByClass(mSearchView, TextView.class)) {
            textView.setTextColor(Color.WHITE);
        }

        searchResultsPopup = new SearchResultsPopup(this, mSearchView, searchItem);

        return true;
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_search:
                mSearchView.setIconified(false);
                return true;
            case R.id.action_settings:
                return true;
        }
        return false;
    }

}
