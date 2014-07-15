package com.cubolabs.bibliaofflinearc.ui;

import com.cubolabs.bibliaofflinearc.R;
import com.cubolabs.bibliaofflinearc.data.ListaDeVersiculos;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
		
		// http://stackoverflow.com/questions/9239933/how-can-i-get-the-list-item-data-i-click-on
		/*List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();

	    HashMap<String, Object> map = new HashMap<String, Object>();
	    map.put(ITEMTITLE, getString(R.string.broadcast_street_1_text));
	    map.put(ITEMDESCR, getString(R.string.broadcast_request_1_text));
	    dataList.add(map);

	    map = new HashMap<String, Object>();
	    map.put(ITEMTITLE, getString(R.string.broascast_street_2_text)); 
	    map.put(ITEMDESCR, getString(R.string.broadcast_request_2_text));
	    dataList.add(map);*/

		
		/** Creating an array adapter to store the list of countries **/
		
        /*ArrayAdapter<String> adapter =
        		new ArrayAdapter<String>(
        				inflater.getContext(), 
        				android.R.layout.simple_list_item_1,
        				listaDeVersiculos.PorCapitulo(
        						getArguments().getString(ARG_BOOK),
        						getArguments().getInt(ARG_CHAPTER))
        				);*/
        final int capitulo = getArguments().getInt(ARG_CHAPTER);
        final String livro = getArguments().getString(ARG_BOOK);
        final ArrayList<String> versiculos = listaDeVersiculos.PorCapitulo(livro, capitulo);

        ArrayAdapter adapter = new ArrayAdapter(inflater.getContext(),
                                                R.layout.versiculo_item,
                                                R.id.versiculoText,
                                                versiculos
                                                ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView versiculoText = (TextView) view.findViewById(R.id.versiculoText);
                versiculoText.setText(versiculos.get(position));

                //if(convertView == null && versiculoText.getText().toString().startsWith("1  ")) {
                if(convertView == null && position == 0) {
                    TextView capituloText = (TextView) view.findViewById(R.id.capituloText);
                    capituloText.setText(String.valueOf(capitulo));
                }

                return view;
            }
        };

        /** Setting the list adapter for the ListFragment */
        setListAdapter(adapter);
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /** Activity is null if not attached **/
        listaDeVersiculos = new ListaDeVersiculos(activity);
    }

}
