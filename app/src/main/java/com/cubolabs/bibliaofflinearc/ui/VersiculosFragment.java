package com.cubolabs.bibliaofflinearc.ui;

import com.cubolabs.bibliaofflinearc.R;
import com.cubolabs.bibliaofflinearc.data.ListaDeVersiculos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class VersiculosFragment extends ListFragment {
	private ListaDeVersiculos listaDeVersiculos;
	private static final String ARG_BOOK = "livro";
	private static final String ARG_CHAPTER = "capitulo";
	
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

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    getActivity().getMenuInflater().inflate(R.menu.listcab_menu,
                            menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @SuppressLint("NewApi")
                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        /*case R.id.item1:
                            Toast.makeText(getActivity(), "Option1 clicked",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.item2:
                            Toast.makeText(getActivity(), "Option2 clicked",
                                    Toast.LENGTH_SHORT).show();
                            break;*/
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
                public void onDestroyActionMode(ActionMode mode) {
                    nr = 0;
                }

                @SuppressLint("NewApi")
                @Override
                public void onItemCheckedStateChanged(ActionMode mode,
                                                      int position, long id, boolean checked) {
                    if (checked) {
                        nr++;
                    } else {
                        nr--;
                    }
                    mode.setTitle(nr + " rows selected!");
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

}
