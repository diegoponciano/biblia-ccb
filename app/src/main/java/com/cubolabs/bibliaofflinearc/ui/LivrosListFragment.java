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
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

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
                                            android.R.layout.simple_list_item_1,
                                            listaDeLivros.NomesVelhoTestamento());

        ListView oldTestamentList = (ListView) v.findViewById(R.id.oldTestamentList);
        oldTestamentList.setAdapter(oldAdapter);
        setListViewHeightBasedOnChildren(oldTestamentList);

        ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(
                        inflater.getContext(),
                        android.R.layout.simple_list_item_1,
                        listaDeLivros.NomesNovoTestamento());
        ListView newTestamentList = (ListView) v.findViewById(R.id.newTestamentList);
        newTestamentList.setAdapter(newAdapter);
        setListViewHeightBasedOnChildren(newTestamentList);




        return v;

        /** Setting the list adapter for the ListFragment */
        //setListAdapter(adapter);

        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        /*for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }*/

        View lastItem = listAdapter.getView(listAdapter.getCount()-1, null, listView);
        lastItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);


        ViewGroup.LayoutParams params = listView.getLayoutParams();
        //params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height = lastItem.getHeight() * (listAdapter.getCount()-1);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /** Activity is null if not attached **/
        listaDeLivros = new ListaDeLivros(activity);
    }
	
	//@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
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
