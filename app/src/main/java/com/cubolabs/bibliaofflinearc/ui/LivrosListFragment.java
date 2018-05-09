package com.cubolabs.bibliaofflinearc.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.cubolabs.bibliaofflinearc.R;
import com.cubolabs.bibliaofflinearc.data.ListaDeLivros;

public class LivrosListFragment extends Fragment  {
    public static final String TAG = LivrosListFragment.class.getSimpleName();
	private ListaDeLivros listaDeLivros;
	
	public static LivrosListFragment newInstance() {
        return new LivrosListFragment();
    }
	
    public LivrosListFragment() {
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ActionBar actionBar = 
				((AppCompatActivity) getActivity()).getSupportActionBar();
		actionBar.setTitle(R.string.app_title);

        View v = inflater.inflate(R.layout.livros_list, container, false);

		/** Creating an array adapter to store the list of countries **/
        ArrayAdapter<String> oldAdapter = new ArrayAdapter<String>(
                        inflater.getContext(),
                        R.layout.livro_list_item_1,
                        listaDeLivros.NomesVelhoTestamentoHorizontal());

        GridView oldTestamentList = (GridView) v.findViewById(R.id.oldTestamentGrid);
        oldTestamentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClick(parent, view, position, id);
            }
        });
        oldTestamentList.setAdapter(oldAdapter);

        ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(
                        inflater.getContext(),
                        R.layout.livro_list_item_1,
                        listaDeLivros.NomesNovoTestamentoHorizontal());
        GridView newTestamentList = (GridView) v.findViewById(R.id.newTestamentGrid);
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
		
		transaction.replace(R.id.container, newFragment, TAG);
		transaction.addToBackStack(TAG);
		transaction.commit();
	}
}
