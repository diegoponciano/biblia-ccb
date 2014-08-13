package com.cubolabs.bibliaofflinearc;

import com.cubolabs.bibliaofflinearc.data.ListaDeVersiculos;
import com.cubolabs.bibliaofflinearc.ui.LivrosListFragment;
import com.cubolabs.bibliaofflinearc.ui.MyMessageBox;
import com.cubolabs.bibliaofflinearc.ui.NavigationDrawerFragment;

import android.content.Context;
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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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
    private String popUpContents[];
    private PopupWindow popupSearchResults;
    private ArrayAdapter<String> searchResultsAdapter;
    private Menu actionBarMenu;
    private ListaDeVersiculos listaDeVersiculos;

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

            popUpContents = new String[]{};
            popupSearchResults = popupSearchResults();
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

    public PopupWindow popupSearchResults() {
        PopupWindow popupWindow = new PopupWindow(this);
        ListView listviewSearchResults = new ListView(this) {
            //TODO: popupwindow 90% parent's width
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
                int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
                this.setMeasuredDimension((int)Math.round(parentWidth*0.9), parentHeight);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        };
        listviewSearchResults.setAdapter(versesAdapter(popUpContents));

        listviewSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
                Context mContext = v.getContext();
                MainActivity mainActivity = ((MainActivity) mContext);

                // add some animation when a list item was clicked
                Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
                fadeInAnimation.setDuration(10);
                v.startAnimation(fadeInAnimation);

                mainActivity.popupSearchResults.dismiss();

                String selectedItemTag = v.getTag().toString();
                Toast.makeText(mContext, selectedItemTag, Toast.LENGTH_SHORT).show();
            }
        });

        // some other visual settings
        popupWindow.setFocusable(false);
        //popupWindow.setFocusable(true);
        //popupWindow.setWidth(250);

        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        // set the list view as pop up window content
        popupWindow.setContentView(listviewSearchResults);
        return popupWindow;
    }

    private ArrayAdapter<String> versesAdapter(String dogsArray[]) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dogsArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // setting the ID and text for every items in the list
                String item = getItem(position);
                String[] itemArr = item.split("::");
                String text = itemArr[0];
                String id = itemArr[1];

                // visual settings for the list item
                TextView listItem = new TextView(MainActivity.this);

                listItem.setText(text);
                listItem.setTag(id);
                listItem.setTextSize(16);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);

                return listItem;
            }
        };

        return adapter;
    }

    @Override
    public void onBackPressed() {
        if (popupSearchResults.isShowing())
            popupSearchResults.dismiss();
        else {
            MenuItem searchItem = actionBarMenu.findItem(R.id.action_search);
            mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            if (!mSearchView.isIconified())
                mSearchView.setIconified(true);
            else
                super.onBackPressed();
        }
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

        actionBarMenu = menu;

        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (listaDeVersiculos == null)
            listaDeVersiculos = new ListaDeVersiculos(this);

        ArrayList<String> versiculos = listaDeVersiculos.Busca(s);

        popUpContents = versiculos.toArray(new String[versiculos.size()]);
        if (popupSearchResults.isShowing()) {
            popupSearchResults.dismiss();
        }
        popupSearchResults = popupSearchResults();

        popupSearchResults.showAsDropDown(findViewById(R.id.action_search), -5, 0);
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
