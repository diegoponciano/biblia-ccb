package com.cubolabs.bibliaofflinearc.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cubolabs.bibliaofflinearc.MainActivity;
import com.cubolabs.bibliaofflinearc.R;
import com.cubolabs.bibliaofflinearc.data.ListaDeVersiculos;

import java.util.ArrayList;

public class SearchResultsPopup {
    public static final String TAG = SearchResultsPopup.class.getSimpleName();
    private MainActivity activity;
    private String popUpContents[];
    private PopupWindow popupSearchResults;
    private SearchView mSearchView;
    private MenuItem searchItem;
    private ListaDeVersiculos listaDeVersiculos;

    public SearchResultsPopup(MainActivity activity, SearchView mSearchView, MenuItem searchItem){
        this.activity = activity;
        this.mSearchView = mSearchView;
        this.searchItem = searchItem;
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
                //Context mContext = v.getContext();
                //MainActivity mainActivity = ((MainActivity) mContext);

                // add some animation when a list item was clicked
                Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
                fadeInAnimation.setDuration(10);
                v.startAnimation(fadeInAnimation);

                while (hide()) {}

                String selectedItemTag = v.getTag().toString();
                //Toast.makeText(mContext, selectedItemTag, Toast.LENGTH_SHORT).show();

                String[] livroCapituloArr = selectedItemTag.split(",|:");
                String livro = livroCapituloArr[0];
                Integer capitulo = Integer.parseInt(livroCapituloArr[1].trim());
                //Integer versiculo = Integer.parseInt(livroCapituloArr[2]);

                Fragment newFragment =
                        VersiculosFragment.newInstance(livro, capitulo);
                FragmentTransaction transaction =
                        activity.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

                transaction.addToBackStack(TAG);
                transaction.replace(R.id.container, newFragment, TAG);

                transaction.commit();
            }
        });

        // some other visual settings
        popupWindow.setFocusable(true);
        //popupWindow.setTouchable(true);
        //popupWindow.setFocusable(true);
        //popupWindow.setWidth(250);

        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        // set the list view as pop up window content
        popupWindow.setContentView(listviewSearchResults);
        return popupWindow;
    }

    private ArrayAdapter<String> versesAdapter(String verses[]) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_2, verses) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // setting the ID and text for every items in the list
                String item = getItem(position);
                String[] itemArr = item.split("=>");
                String verseText = itemArr[0];
                String bookChapterVerse = itemArr[1];

                View row;
                if(convertView == null)
                {
                    Context ctx = activity.getApplicationContext();
                    LayoutInflater inflater =
                            (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = inflater.inflate(android.R.layout.simple_list_item_2, null);
                }
                else
                {
                    row = convertView;
                }
                //View row = super.getView(position, convertView, parent);
                row.setTag(bookChapterVerse);

                // visual settings for the list item
                TextView v = (TextView) row.findViewById(android.R.id.text1);
                v.setText(verseText);
                v.setTextSize(16);
                v.setPadding(10, 10, 10, 10);
                v.setTextColor(Color.WHITE);

                v = (TextView) row.findViewById(android.R.id.text2);
                v.setText(bookChapterVerse);

                return row;
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

        mSearchView.clearFocus();
        InputMethodManager imm =
                (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
        popupSearchResults.getContentView().requestFocus();
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
