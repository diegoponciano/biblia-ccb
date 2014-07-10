package com.cubolabs.bibliaofflinearc.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cubolabs.bibliaofflinearc.R;
import com.cubolabs.bibliaofflinearc.data.ListaDeLivros;

public class CapitulosFragment extends ListFragment {
	private ListaDeLivros listaDeLivros;
	private static final String ARG_BOOK = "livro";
	
	public static CapitulosFragment newInstance(String livro) {
		CapitulosFragment fragment = new CapitulosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BOOK, livro);
        fragment.setArguments(args);
        return fragment;
    }
	
    public CapitulosFragment() {
    }
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ActionBar actionBar = 
				((ActionBarActivity) getActivity()).getSupportActionBar();
		actionBar.setTitle(getArguments().getString(ARG_BOOK));
		
		/** Creating an array adapter to store the list of countries **/
        ArrayAdapter<Integer> adapter = 
        		new ArrayAdapter<Integer>(
        				inflater.getContext(), 
        				android.R.layout.simple_list_item_1,
        				listaDeLivros.Capitulos(getArguments().getString(ARG_BOOK))
        				);
        
        /** Setting the list adapter for the ListFragment */
        setListAdapter(adapter);
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /** Activity is null if not attached **/
        listaDeLivros = new ListaDeLivros(activity);
    }
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Integer capitulo = (Integer)l.getItemAtPosition(position);
		
		Fragment newFragment = 
				VersiculosFragment.newInstance(getArguments().getString(ARG_BOOK), capitulo);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
		
		transaction.replace(R.id.container, newFragment);
		transaction.addToBackStack(null);
		
		transaction.commit();
	}
}
