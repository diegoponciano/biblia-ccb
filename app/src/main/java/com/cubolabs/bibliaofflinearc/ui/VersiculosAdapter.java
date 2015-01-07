package com.cubolabs.bibliaofflinearc.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cubolabs.bibliaofflinearc.R;

import java.util.ArrayList;

import greendao.Verse;

public class VersiculosAdapter extends ArrayAdapter<Verse> {
    private final ArrayList<Verse> verses;
    private final int capitulo;
    private final int versesSize;
    private final Context context;

    public VersiculosAdapter(Context context, ArrayList<Verse> verses, int capitulo) {
        super(context,
                (ViewUtils.AboveHoneycomb() ?
                R.layout.versiculo_hc_item_1:
                R.layout.versiculo_checked), //android.R.layout.simple_list_item_checked
                android.R.id.text1,
                verses);
        this.verses = verses;
        this.capitulo = capitulo;
        this.context = context;
        this.versesSize = verses.size();
    }

    @Override
    public int getViewTypeCount() {
        return 7;
    }

    @Override
    public int getItemViewType(int position) {
        //the result must be in the range 0 to getViewTypeCount() - 1.
        if (verses.get(position).getHeader() != null && verses.get(position).getSubheader() != null && position == 0)
            return 6;
        else if (verses.get(position).getHeader() != null && verses.get(position).getSubheader() != null)
            return 5;
        else if (verses.get(position).getHeader() != null && position == 0)
            return 4;
        else if (verses.get(position).getHeader() != null)
            return 3;
        else if(position == 0)
            return 0;
        else if (position < versesSize-1)
            return 1;
        else
            return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemType = getItemViewType(position);

        View view;
        if(itemType == 2) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(ViewUtils.AboveHoneycomb() ?
                R.layout.versiculo_hc_item_2:
                R.layout.versiculo_checked_2, parent, false);
            TextView hint = (TextView) view.findViewById(android.R.id.text2);
            hint.setText(R.string.swipe_to_change);
        }
        else if(itemType == 3 | itemType == 4) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(ViewUtils.AboveHoneycomb() ?
                    R.layout.versiculo_hc_item_3:
                    R.layout.versiculo_checked_2, parent, false);
            TextView header = (TextView) view.findViewById(android.R.id.text2);
            String headerText = HtmlUtils.SmallCapitalize(verses.get(position).getHeader());
            header.setText(Html.fromHtml(headerText), TextView.BufferType.SPANNABLE);
        }
        else if(itemType == 5 | itemType == 6) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(ViewUtils.AboveHoneycomb() ?
                    R.layout.versiculo_hc_item_4:
                    R.layout.versiculo_checked_2, parent, false);
            TextView header = (TextView) view.findViewById(R.id.text3);
            String headerText = HtmlUtils.SmallCapitalize(verses.get(position).getHeader());
            header.setText(Html.fromHtml(headerText), TextView.BufferType.SPANNABLE);
            TextView subheader = (TextView) view.findViewById(android.R.id.text2);
            subheader.setText(
                    HtmlUtils.Smallcaps(verses.get(position).getSubheader()),
                    TextView.BufferType.SPANNABLE);
        }
        else
           view = super.getView(position, convertView, parent);

        TextView versiculoText = (TextView) view.findViewById(android.R.id.text1);

        String indice;
        SpannableString spanText;
        Object indiceStyle;

        if(itemType == 0 || itemType == 4 || itemType == 6) {
            indice = String.valueOf(capitulo);
            indiceStyle = new RelativeSizeSpan(2f);
        }
        else {
            indice = String.valueOf(position + 1);
            indiceStyle = new StyleSpan(Typeface.BOLD);
        }

        String htmlText = HtmlUtils.SmallCapitalize(verses.get(position).getText());

        Spanned span = Html.fromHtml("&nbsp;"+indice+"&nbsp;&nbsp;"+htmlText);
        spanText = new SpannableString(span);
        spanText.setSpan(indiceStyle, 1, 1+indice.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        versiculoText.setText(spanText, TextView.BufferType.SPANNABLE);

        return view;
    }
}

