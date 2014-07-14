package com.cubolabs.bibliaofflinearc.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cubolabs.bibliaofflinearc.R;
import com.cubolabs.bibliaofflinearc.data.ListaDeLivros;

public class LivrosListFragment extends Fragment  {
	private ListaDeLivros listaDeLivros;
	
	public static LivrosListFragment newInstance() {
		LivrosListFragment fragment = new LivrosListFragment();
        return fragment;
    }
	
    public LivrosListFragment() {
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ActionBar actionBar = 
				((ActionBarActivity) getActivity()).getSupportActionBar();
		actionBar.setTitle(R.string.app_title);

        View v = inflater.inflate(R.layout.livros_list, container, false);

		/** Creating an array adapter to store the list of countries **/
        ArrayAdapter<String> oldAdapter = new ArrayAdapter<String>(
                        inflater.getContext(),
                        R.layout.livro_list_item_1,
                        listaDeLivros.NomesVelhoTestamento());

        ListView oldTestamentList = (ListView) v.findViewById(R.id.oldTestamentList);
        oldTestamentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClick(parent, view, position, id);
            }
        });
        oldTestamentList.setAdapter(oldAdapter);

        ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(
                        inflater.getContext(),
                        R.layout.livro_list_item_1,
                        listaDeLivros.NomesNovoTestamento());
        ListView newTestamentList = (ListView) v.findViewById(R.id.newTestamentList);
        newTestamentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClick(parent, view, position, id);
            }
        });
        newTestamentList.setAdapter(newAdapter);

        return v;

        /** Setting the list adapter for the ListFragment */
        //setListAdapter(adapter);

        //return super.onCreateView(inflater, container, savedInstanceState);
    }

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /** Activity is null if not attached **/
        listaDeLivros = new ListaDeLivros(activity);
    }
	
	//@Override
	public void onListItemClick(AdapterView<?> l, View v, int position, long id) {
		//super.onListItemClick(l, v, position, id);
		
		String nomeLivro = l.getItemAtPosition(position).toString();
		
		Fragment newFragment = CapitulosFragment.newInstance(nomeLivro);
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
		
		transaction.replace(R.id.container, newFragment);
		transaction.addToBackStack(null);
		
		transaction.commit();
	}
}
