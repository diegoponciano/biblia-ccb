package com.cubolabs.bibliaofflinearc;

import com.cubolabs.bibliaofflinearc.data.BibliaDatabase;
import com.cubolabs.bibliaofflinearc.data.DaoMaster;
import com.cubolabs.bibliaofflinearc.data.DaoSession;
import com.cubolabs.bibliaofflinearc.ui.LivrosListFragment;
import com.cubolabs.bibliaofflinearc.ui.MyMessageBox;
import com.cubolabs.bibliaofflinearc.ui.NavigationDrawerFragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {
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
    private BibliaDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
	 	try {
            db = BibliaDatabase.getInstance(this);
            daoMaster = new DaoMaster(db.getReadableDatabase());
            daoSession = daoMaster.newSession();

            mTitle = getTitle();

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
    protected void onDestroy() {
        db.close();
        super.onDestroy();
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
