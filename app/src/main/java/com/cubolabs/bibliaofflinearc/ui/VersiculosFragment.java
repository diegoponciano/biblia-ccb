package com.cubolabs.bibliaofflinearc.ui;

import com.cubolabs.bibliaofflinearc.MainActivity;
import com.cubolabs.bibliaofflinearc.R;
import com.cubolabs.bibliaofflinearc.data.ListaDeVersiculos;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.view.ActionMode;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class VersiculosFragment extends ListFragment {
    private static final String TAG = VersiculosFragment.class.getSimpleName();
	private ListaDeVersiculos listaDeVersiculos;
	private static final String ARG_BOOK = "livro";
	private static final String ARG_CHAPTER = "capitulo";
    private ShareActionProvider mShareActionProvider;
    private ActionMode actionMode;

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

    private String currentChapter(){
        final int capitulo = getArguments().getInt(ARG_CHAPTER);
        final String livro = getArguments().getString(ARG_BOOK);
        return livro + ", " + String.valueOf(capitulo);
    }
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int capitulo = getArguments().getInt(ARG_CHAPTER);
        final String livro = getArguments().getString(ARG_BOOK);
        final ArrayList<String> versiculos = listaDeVersiculos.PorCapitulo(livro, capitulo);

        ActionBar actionBar =
                ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(this.currentChapter());

        ArrayAdapter adapter = new VersiculosAdapter(inflater, versiculos, capitulo);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // Notice how the ListView api is lame
        // You can use mListView.getCheckedItemIds() if the adapter
        // has stable ids, e.g you're using a CursorAdaptor
        SparseBooleanArray checked = getListView().getCheckedItemPositions();
        boolean hasCheckedElement = false;
        for (int i = 0 ; i < checked.size() && ! hasCheckedElement ; i++) {
            hasCheckedElement = checked.valueAt(i);
        }

        if(l.isItemChecked(position)) {
            l.setItemChecked(position, false);
            getListView().setItemChecked(position, true);
        }
        else {
            l.setItemChecked(position, true);
            getListView().setItemChecked(position, false);
        }

        int nr = checked.size();
        if (actionMode != null)
            actionMode.setTitle(nr + (nr == 1 ? " versículo!" : " versículos!"));

        if (hasCheckedElement) {
            if (actionMode == null) {
                actionMode = ((ActionBarActivity) getActivity())
                        .startSupportActionMode(new ContextualActionBar());
            }
        } else {
            if (actionMode != null) {
                actionMode.finish();
            }
        }
    };

    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setDivider(this.getResources().getDrawable(R.drawable.transparent_color));
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /** Activity is null if not attached **/
        listaDeVersiculos = new ListaDeVersiculos(activity);
    }

    private class ContextualActionBar implements ActionMode.Callback {
        private ShareActionProvider mShareActionProvider;
        private int nr = 0;

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            StringBuilder versesToShare = new StringBuilder();
            StringBuilder versesToShareHtml = new StringBuilder();
            int len = getListView().getCount();
            SparseBooleanArray checked = getListView().getCheckedItemPositions();
            for (int i = 0; i < len; i++){
                if (checked.get(i)) {
                    versesToShare.append(String.valueOf(i+1) + ". ");
                    versesToShare.append(getListAdapter().getItem(i).toString());
                    versesToShare.append("\n");

                    versesToShareHtml.append("<b>"+String.valueOf(i+1) + ". </b>");
                    versesToShareHtml.append(getListAdapter().getItem(i).toString());
                    versesToShareHtml.append("\n");
                }
            }
            switch (item.getItemId()) {
                case R.id.menu_item_share:
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/html");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, currentChapter());
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, versesToShare.toString());
                    sharingIntent.putExtra(Intent.EXTRA_HTML_TEXT,
                            Html.fromHtml(versesToShareHtml.toString())
                    );
                    startActivity(Intent.createChooser(sharingIntent, "Enviar via"));
                    break;
                case R.id.menu_item_copy:
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ) {
                        final android.text.ClipboardManager clipboard =
                                (android.text.ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                        clipboard.setText(versesToShare.toString());
                    }
                    else {
                        ClipboardManager clipboard = (ClipboardManager)
                                getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newHtmlText("Verses clipboard",
                                versesToShare.toString(),
                                versesToShareHtml.toString());
                        clipboard.setPrimaryClip(clip);
                    }
                    Toast.makeText(getActivity(),
                            "Copiado com sucesso!",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.d(TAG, "onActionItemClicked: Unknown Menu Item Received!");
                    mode.finish();
                    break;
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getActivity().getMenuInflater()
                    .inflate(R.menu.listcab_menu, menu);
            return true;

            //Initialize the ShareActionProvider
            /*MenuItem shareMenuItem = menu.findItem(R.id.menu_item_share);
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "test message");
            mShareActionProvider.setShareIntent(shareIntent);
            return true;*/
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //Nullify the actionMode object
            //so that the onClickListener can identify whether the ActionMode is ON
            actionMode = null;

            //Uncheck all checked messages
            SparseBooleanArray selectedItems = getListView().getCheckedItemPositions();
            for(int i=0;i<selectedItems.size();i++){
                getListView().setItemChecked(selectedItems.keyAt(i), false);
            }
            nr = 0;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu arg1) {
            // TODO Auto-generated method stub
            return false;
        }
    }
}
