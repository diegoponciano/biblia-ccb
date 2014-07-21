package com.cubolabs.bibliaofflinearc.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cubolabs.bibliaofflinearc.R;

import java.util.ArrayList;

public class VersiculosAdapter extends ArrayAdapter<String> {
    private final ArrayList<String> versiculos;
    private final int capitulo;
    private final Context context;

    public VersiculosAdapter(LayoutInflater inflater, ArrayList<String> versiculos, int capitulo) {
        super(inflater.getContext(),
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 :
                R.layout.versiculo_checked), //android.R.layout.simple_list_item_checked
                android.R.id.text1,
                versiculos);
        this.versiculos = versiculos;
        this.capitulo = capitulo;
        this.context = inflater.getContext();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        final TextView versiculoText = (TextView) view.findViewById(android.R.id.text1);

        String indice;
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

        spanText = new SpannableString(" "+indice+"  "+versiculos.get(position));
        spanText.setSpan(indiceStyle, 1, 1+indice.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        versiculoText.setText(spanText, TextView.BufferType.SPANNABLE);
        versiculoText.setPadding(8, 0, 2, 4);

        return view;
    }
}

