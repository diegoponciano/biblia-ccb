package com.cubolabs.bibliaofflinearc.ui;

import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cubolabs.bibliaofflinearc.R;

import java.util.ArrayList;


public class VersiculosAdapter extends ArrayAdapter {
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

        String versiculoCompleto, indice;
        SpannableString spanText;
        Object indiceStyle;

        if(position == 0) {
            indice = String.valueOf(capitulo);
            indiceStyle = new RelativeSizeSpan(2f);
        }
        else {
            indice = String.valueOf(position + 1);
            indiceStyle = new StyleSpan(Typeface.BOLD);
        }
        versiculoCompleto = " "+indice+"  "+versiculos.get(position);
        spanText = new SpannableString(versiculoCompleto);
        spanText.setSpan(indiceStyle, 1, 1+indice.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        versiculoText.setText(spanText, TextView.BufferType.SPANNABLE);

        return view;
    }
}

