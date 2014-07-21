package com.cubolabs.bibliaofflinearc.ui;

import com.cubolabs.bibliaofflinearc.R;
import com.cubolabs.bibliaofflinearc.data.ListaDeVersiculos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.SparseBooleanArray;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.support.v4.view.MenuItemCompat.*;

public class VersiculosFragment extends ListFragment {
	private ListaDeVersiculos listaDeVersiculos;
	private static final String ARG_BOOK = "livro";
	private static final String ARG_CHAPTER = "capitulo";
    private ShareActionProvider mShareActionProvider;
    private ActionMode actionMode;

	public static VersiculosFragment newInstance(String livro, Integer capitulo) {
		VersiculosFragment fragment = new VersiculosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BOOK, livro);
        args.putInt(ARG_CHAPTER, capitulo);
        fragment.setArguments(args);
        return fragment;
    }
	
    public VersiculosFragment() {
    }
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int capitulo = getArguments().getInt(ARG_CHAPTER);
        final String livro = getArguments().getString(ARG_BOOK);
        final ArrayList<String> versiculos = listaDeVersiculos.PorCapitulo(livro, capitulo);

        ActionBar actionBar =
                ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(livro + ", " + String.valueOf(capitulo));

        ArrayAdapter adapter = new VersiculosAdapter(inflater, versiculos, capitulo);

        /** Setting the list adapter for the ListFragment */
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getActivity().getMenuInflater().inflate(R.menu.listcab_menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        // Return true to display menu
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setDivider(this.getResources().getDrawable(R.drawable.transparent_color));
        //getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            getListView().setMultiChoiceModeListener(new ListView.MultiChoiceModeListener() {

                private int nr = 0;
                private ShareActionProvider mShareActionProvider;

                private void setShareIntent(Intent shareIntent) {
                    if (mShareActionProvider != null) {
                        mShareActionProvider.setShareIntent(shareIntent);
                    }
                }

                @Override
                public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                    getActivity().getMenuInflater().inflate(R.menu.listcab_menu,
                            menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                    return false;
                }

                @SuppressLint("NewApi")
                @Override
                public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        /*case R.id.item1:
                            Toast.makeText(getActivity(), "Option1 clicked",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.item2:
                            Toast.makeText(getActivity(), "Option2 clicked",
                                    Toast.LENGTH_SHORT).show();
                            break;*/
                        case R.id.menu_item_share:
                            Toast.makeText(getActivity(), "SHAREZ IT!",
                                    Toast.LENGTH_SHORT).show();
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            String shareBody = "Here is the share content body";
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(sharingIntent, "Share via"));
                            break;
                        case R.id.edit_entry:
                            Toast.makeText(getActivity(), "Edit entry!",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.delete_entry:
                            Toast.makeText(getActivity(), "Delete entry!",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.finish_it:
                            nr = 0;
                            Toast.makeText(getActivity(), "Finish the CAB!",
                                    Toast.LENGTH_SHORT).show();
                            mode.finish();

                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(android.view.ActionMode mode) {
                    nr = 0;
                }

                @SuppressLint("NewApi")
                @Override
                public void onItemCheckedStateChanged(android.view.ActionMode mode,
                                                      int position, long id, boolean checked) {
                    if (checked) {
                        nr++;
                    } else {
                        nr--;
                    }
                    mode.setTitle(nr + " rows!");
                }

            });
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    if(getListView().getCheckedItemCount() == 0){
                        actionMode.finish();
                        return;
                    }

                    if(actionMode == null){
                        actionMode = ((ActionBarActivity)getActivity()).startSupportActionMode(new ContextualActionBar());
                    }

                }
            });
        }
        else {
            getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            registerForContextMenu(getListView());
        }
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /** Activity is null if not attached **/
        listaDeVersiculos = new ListaDeVersiculos(activity);
    }

    private class ContextualActionBar implements ActionMode.Callback{
        private ShareActionProvider mShareActionProvider;

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch(item.getItemId()){

                case R.id.menu_item_share :
                    mode.finish();
                    return true;

                default :
                    return false;
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.listcab_menu, menu);

            //Initialize the ShareActionProvider
            MenuItem shareMenuItem = menu.findItem(R.id.menu_item_share);
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "test message");
            mShareActionProvider.setShareIntent(shareIntent);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //Nullify the actionMode object
            //so that the onClickListener can identify whether the ActionMode is ON
            actionMode = null;

            //Uncheck all checked messages
            SparseBooleanArray selectedItems = getListView().getCheckedItemPositions();
            for(int i=0;i<selectedItems.size();i++){
                getListView().setItemChecked(selectedItems.keyAt(i), false);
            }
        }

        @Override
        public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
            // TODO Auto-generated method stub
            return false;
        }

    }

}
