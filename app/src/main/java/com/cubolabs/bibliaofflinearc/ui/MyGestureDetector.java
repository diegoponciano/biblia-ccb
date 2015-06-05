package com.cubolabs.bibliaofflinearc.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.cubolabs.bibliaofflinearc.R;
import com.cubolabs.bibliaofflinearc.data.ListaDeLivros;
import com.cubolabs.bibliaofflinearc.data.ListaDeVersiculos;

import greendao.Book;
import greendao.Verse;

public class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private VersiculosFragment versiculosFragment;
    private ListaDeVersiculos listaDeVersiculos;
    private ListaDeLivros listaDeLivros;

    public MyGestureDetector(VersiculosFragment versiculosFragment,
                             ListaDeVersiculos listaDeVersiculos,
                             ListaDeLivros listaDeLivros) {
        this.versiculosFragment = versiculosFragment;
        this.listaDeVersiculos = listaDeVersiculos;
        this.listaDeLivros = listaDeLivros;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > VersiculosFragment.SWIPE_MAX_OFF_PATH)
                return false;
            // right to left swipe

            final int capitulo = versiculosFragment.getArguments().getInt(VersiculosFragment.ARG_CHAPTER);
            final String livro = versiculosFragment.getArguments().getString(VersiculosFragment.ARG_BOOK);

            if(e1.getX() - e2.getX() > VersiculosFragment.SWIPE_MIN_DISTANCE && Math.abs(velocityX) > VersiculosFragment.SWIPE_THRESHOLD_VELOCITY) {
                //Toast.makeText(getActivity(), "Left Swipe", Toast.LENGTH_SHORT).show();
                Verse proximo = listaDeVersiculos.ProximoCapitulo(livro, capitulo);
                if (proximo != null) {
                    Book book = listaDeLivros.ByAbbreviation(proximo.getBook());
                    Fragment newFragment = VersiculosFragment.newInstance(
                            book.getName(),
                            proximo.getChapter()
                    );
                    FragmentTransaction transaction = versiculosFragment.getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(
                            R.anim.enter,
                            R.anim.exit,
                            R.anim.pop_enter,
                            R.anim.pop_exit);

                    transaction.addToBackStack(null);
                    FragmentUtils.clearBackStack(versiculosFragment.getActivity());
                    transaction.replace(R.id.container, newFragment, VersiculosFragment.TAG);
                    transaction.commit();
                }
            }  else if (e2.getX() - e1.getX() > VersiculosFragment.SWIPE_MIN_DISTANCE && Math.abs(velocityX) > VersiculosFragment.SWIPE_THRESHOLD_VELOCITY) {
                //Toast.makeText(getActivity(), "Right Swipe", Toast.LENGTH_SHORT).show();
                Verse anterior = listaDeVersiculos.CapituloAnterior(livro, capitulo);
                if (anterior != null) {
                    Book book = listaDeLivros.ByAbbreviation(anterior.getBook());
                    Fragment newFragment = VersiculosFragment.newInstance(
                            book.getName(),
                            anterior.getChapter()
                    );
                    FragmentTransaction transaction = versiculosFragment.getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(
                            R.anim.enterleft,
                            R.anim.exitleft,
                            R.anim.pop_enterleft,
                            R.anim.pop_exitleft);

                    transaction.addToBackStack(null);
                    FragmentUtils.clearBackStack(versiculosFragment.getActivity());
                    transaction.replace(R.id.container, newFragment, VersiculosFragment.TAG);
                    transaction.commit();
                }
            }
        } catch (Exception e) {
            Log.d("GestureDetector.onFling", e.getLocalizedMessage());
            // nothing
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }
}
