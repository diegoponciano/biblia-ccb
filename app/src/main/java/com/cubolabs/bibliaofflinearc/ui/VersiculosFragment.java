package com.cubolabs.bibliaofflinearc.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cubolabs.bibliaofflinearc.R;
import com.cubolabs.bibliaofflinearc.data.ListaDeVersiculos;

import java.util.ArrayList;
import java.util.Collections;

public class VersiculosFragment extends ListFragment {
    public static final String TAG = VersiculosFragment.class.getSimpleName();
	private ListaDeVersiculos listaDeVersiculos;
    public static final String ARG_BOOK = "livro";
    public static final String ARG_CHAPTER = "capitulo";
    public ActionMode actionMode;
    private SharedPreferences prefs;
    public static final int SWIPE_MIN_DISTANCE = 120;
    public static final int SWIPE_MAX_OFF_PATH = 250;
    public static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

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

    private int capitulo() {
        return getArguments().getInt(ARG_CHAPTER);
    }
    private String livro() {
        return getArguments().getString(ARG_BOOK);
    }

    public String currentChapter() {
        return livro() + ", " + String.valueOf(capitulo());
    }

    public String selectedVersesTitle() {
        String title = livro() + ", " + String.valueOf(capitulo())+ ":";

        ArrayList<Integer> versesIdcs = new ArrayList<Integer>();
        int len = getListView().getCount();
        SparseBooleanArray checked = getListView().getCheckedItemPositions();
        String versesIdcsText = "";
        for (int i = 0; i < len; i++){
            if (checked.get(i)) {
                versesIdcs.add(i + 1);
                versesIdcsText += String.valueOf(i+1) + ",";
            }
        }
        if (versesIdcsText.endsWith(","))
            versesIdcsText = versesIdcsText.substring(0, versesIdcsText.length()-1);

        if(Collections.max(versesIdcs)-Collections.min(versesIdcs) > versesIdcs.size()-1) {
            title += versesIdcsText;
        }
        else if (versesIdcs.size() == 1) {
            title += String.valueOf(versesIdcs.get(0));
        }
        else {
            title += Collections.min(versesIdcs) + "-" + Collections.max(versesIdcs);
        }

        return title;
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ActionBar actionBar =
                ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(this.currentChapter());

        prefs = getActivity().getSharedPreferences("com.cubolabs.bibliaofflinearc", Context.MODE_PRIVATE);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @SuppressLint("NewApi")
    private int checkedItemsCount(ListView listview) {
        if (Build.VERSION.SDK_INT >= 11) {
            return listview.getCheckedItemCount();
        }
        else {
            int checkedCount = 0;
            for (int i = 0 ; i < listview.getCheckedItemPositions().size(); i++) {
                if(listview.getCheckedItemPositions().valueAt(i))
                    checkedCount++;
            }
            return checkedCount;
        }
    }

    private void actionBarVersesCount(ListView lv) {
        int nr = checkedItemsCount(lv);
        if (actionMode != null)
            actionMode.setTitle(nr + (nr == 1 ? " versículo!" : " versículos!"));
    }

    private void listItemAfterClick(ListView lv, ArrayAdapter adapter) {
        if (checkedItemsCount(lv) == 0) {
            if (actionMode != null) {
                actionMode.finish();
            }
        }

        actionBarVersesCount(lv);
        adapter.notifyDataSetChanged();
    };

    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final int capitulo = getArguments().getInt(ARG_CHAPTER);
        final String livro = getArguments().getString(ARG_BOOK);
        final ArrayList<String> versiculos = listaDeVersiculos.PorCapitulo(livro, capitulo);

        final ArrayAdapter adapter = new VersiculosAdapter(getActivity(), versiculos, capitulo);
        setListAdapter(adapter);

        final ListView lv = getListView();

        lv.setDivider(this.getResources().getDrawable(R.drawable.transparent_color));
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (checkedItemsCount(lv) <= 1 || !lv.isItemChecked(position)) {
                    lv.setItemChecked(position, false);
                }

                listItemAfterClick(lv, adapter);
            }
        });
        lv.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(lv.isItemChecked(position)) {
                    lv.setItemChecked(position, false);
                }
                else {
                    lv.setItemChecked(position, true);
                }

                if (checkedItemsCount(lv) > 0) {
                    if (actionMode == null) {
                        actionMode = ((ActionBarActivity) getActivity())
                                .startSupportActionMode(new ContextualActionBar(VersiculosFragment.this));
                    }
                }
                listItemAfterClick(lv, adapter);
                return true;
            }
        });
        gestureDetector = new GestureDetector(getActivity(), new MyGestureDetector(this, listaDeVersiculos));
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        lv.setOnTouchListener(gestureListener);
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /** Activity is null if not attached **/
        listaDeVersiculos = new ListaDeVersiculos(activity);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (FragmentUtils.sDisableFragmentAnimations) {
            Animation a = new Animation() {};
            a.setDuration(0);
            return a;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

}
