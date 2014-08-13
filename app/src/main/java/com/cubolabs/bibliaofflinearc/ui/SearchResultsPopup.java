package com.cubolabs.bibliaofflinearc.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cubolabs.bibliaofflinearc.MainActivity;
import com.cubolabs.bibliaofflinearc.R;
import com.cubolabs.bibliaofflinearc.data.ListaDeVersiculos;

import java.util.ArrayList;

public class SearchResultsPopup {
    private MainActivity activity;
    private String popUpContents[];
    private PopupWindow popupSearchResults;
    private SearchView mSearchView;
    private ListaDeVersiculos listaDeVersiculos;

    public SearchResultsPopup(MainActivity activity, SearchView mSearchView){
        this.activity = activity;
        this.mSearchView = mSearchView;
        this.popUpContents = new String[]{};
        this.popupSearchResults = create();
    }

    public PopupWindow create(){
        PopupWindow popupWindow = new PopupWindow(activity);
        ListView listviewSearchResults = new ListView(activity) {
            //TODO: popupwindow 90% parent's width
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
                int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
                this.setMeasuredDimension((int)Math.round(parentWidth*0.9), parentHeight);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        };
        listviewSearchResults.setAdapter(versesAdapter(popUpContents));

        listviewSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
                Context mContext = v.getContext();
                MainActivity mainActivity = ((MainActivity) mContext);

                // add some animation when a list item was clicked
                Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
                fadeInAnimation.setDuration(10);
                v.startAnimation(fadeInAnimation);

                popupSearchResults.dismiss();

                String selectedItemTag = v.getTag().toString();
                Toast.makeText(mContext, selectedItemTag, Toast.LENGTH_SHORT).show();
            }
        });

        // some other visual settings
        popupWindow.setFocusable(false);
        //popupWindow.setFocusable(true);
        //popupWindow.setWidth(250);

        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        // set the list view as pop up window content
        popupWindow.setContentView(listviewSearchResults);
        return popupWindow;
    }

    private ArrayAdapter<String> versesAdapter(String dogsArray[]) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, dogsArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // setting the ID and text for every items in the list
                String item = getItem(position);
                String[] itemArr = item.split("::");
                String text = itemArr[0];
                String id = itemArr[1];

                // visual settings for the list item
                TextView listItem = new TextView(activity);

                listItem.setText(text);
                listItem.setTag(id);
                listItem.setTextSize(16);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);

                return listItem;
            }
        };
        return adapter;
    }

    public boolean hide(){
        if (popupSearchResults.isShowing())
            popupSearchResults.dismiss();
        else {
            if (!mSearchView.isIconified())
                mSearchView.setIconified(true);
            else
                return false;
        }
        return true;
    }

    public void submit(String s) {
        if (listaDeVersiculos == null)
            listaDeVersiculos = new ListaDeVersiculos(activity);

        ArrayList<String> versiculos = listaDeVersiculos.Busca(s.trim());
        update(versiculos);
    }

    private void update(ArrayList<String> versiculos) {
        popUpContents = versiculos.toArray(new String[versiculos.size()]);
        if (popupSearchResults.isShowing()) {
            popupSearchResults.dismiss();
        }
        popupSearchResults = create();

        popupSearchResults.showAsDropDown(activity.findViewById(R.id.action_search), -5, 0);
    }
}
