package com.cubolabs.bibliaofflinearc.ui;

import com.cubolabs.bibliaofflinearc.Palavra;
import com.cubolabs.bibliaofflinearc.PalavraDao;
import com.cubolabs.bibliaofflinearc.R;
import com.cubolabs.bibliaofflinearc.data.ListaDeVersiculos;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /** Activity is null if not attached **/
        listaDeVersiculos = new ListaDeVersiculos(activity);
    }

    private static class VersiculosAdapter extends ArrayAdapter {
        private final ArrayList<String> versiculos;
        private final int capitulo;

        public VersiculosAdapter(LayoutInflater inflater, ArrayList<String> versiculos, int capitulo) {
            super(inflater.getContext(), R.layout.versiculo_item, R.id.versiculoText, versiculos);
            this.versiculos = versiculos;
            this.capitulo = capitulo;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            TextView capituloText = (TextView) view.findViewById(R.id.capituloText);
            TextView versiculoText = (TextView) view.findViewById(R.id.versiculoText);

            capituloText.setText("");

            if(position == 0) {
                capituloText.setText(String.valueOf(capitulo));
                versiculoText.setText("  " + versiculos.get(position));
            }
            else
                versiculoText.setText(" " + String.valueOf(position+1) + "  " + versiculos.get(position));

            return view;
        }
    }
}
