package com.cubolabs.bibliaofflinearc.ui;

import android.app.Activity;
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

    public VersiculosAdapter(Context context, ArrayList<String> versiculos, int capitulo) {
        super(context,
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                R.layout.versiculo_hc_item_1:
                R.layout.versiculo_checked), //android.R.layout.simple_list_item_checked
                android.R.id.text1,
                versiculos);
        this.versiculos = versiculos;
        this.capitulo = capitulo;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(ViewUtils.IsLastItem(versiculos, position)) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(ViewUtils.AboveHoneycomb() ?
                    R.layout.versiculo_hc_item_2:
                    R.layout.versiculo_checked_2, parent, false);
            TextView hint = (TextView) view.findViewById(android.R.id.text2);
            hint.setText(R.string.swipe_to_change);
        }
        else
            view = super.getView(position, convertView, parent);

        TextView versiculoText = (TextView) view.findViewById(android.R.id.text1);

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

        return view;
    }
}

