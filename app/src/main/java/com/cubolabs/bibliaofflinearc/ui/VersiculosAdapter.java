package com.cubolabs.bibliaofflinearc.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class VersiculosAdapter extends ArrayAdapter<String> {
    private final ArrayList<String> versiculos;
    private final int capitulo;
    private final Context context;

    public VersiculosAdapter(LayoutInflater inflater, ArrayList<String> versiculos, int capitulo) {
        super(inflater.getContext(),
                android.R.layout.simple_list_item_activated_1,
                //R.layout.versiculo_list_item,
                android.R.id.text1,
                versiculos);
        this.versiculos = versiculos;
        this.capitulo = capitulo;
        this.context = inflater.getContext();
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
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

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            versiculoText.setTextIsSelectable(true);
        }
        else {
            versiculoText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(versiculoText.getText());
                    Toast.makeText(context, "Versiculo copiado!", Toast.LENGTH_SHORT).show();
                }
            });
        }*/

        return view;
    }
}

