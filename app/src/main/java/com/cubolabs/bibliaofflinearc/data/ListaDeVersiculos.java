package com.cubolabs.bibliaofflinearc.data;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.query.QueryBuilder;

public class ListaDeVersiculos {
    private VerseDao verseDao;
    private BookDao bookDao;

	public ListaDeVersiculos(Activity activity) {
        bookDao = BibliaDatabase.getSession(activity).getBookDao();
        verseDao = BibliaDatabase.getSession(activity).getVerseDao();
    }

    public ArrayList<Verse> PorLivroECapitulo(String nomeDoLivro, int capitulo) {
        Book book = bookDao.queryBuilder()
                .where(BookDao.Properties.Name.eq(nomeDoLivro)).unique();

        QueryBuilder<Verse> pqb = verseDao.queryBuilder();
        List<Verse> verses = pqb.where(pqb.and(
                                           VerseDao.Properties.Book.eq(book.getAbbreviation()),
                                           VerseDao.Properties.Chapter.eq(capitulo))
                                       )
                                       .orderAsc(VerseDao.Properties.Verse)
                                       .list();

        return new ArrayList<>(verses);
    }

	public ArrayList<String> PorCapitulo(String nomeDoLivro, int capitulo) {
        ArrayList<Verse> verses = this.PorLivroECapitulo(nomeDoLivro, capitulo);
		
		final ArrayList<String> listaDeNomes = new ArrayList<String>();
		
        for (int i = 0; i < verses.size(); ++i) {
            listaDeNomes.add(verses.get(i).getText());
        }
        
        return listaDeNomes;
	}

    public ArrayList<String> Busca(String s) {
        s = "%" + s + "%";
        QueryBuilder<Verse> pqb = verseDao.queryBuilder();
        pqb.join(Book.class, BookDao.Properties.Abbreviation);

        List<Verse> verses = pqb.where(pqb.or(VerseDao.Properties.Text.like(s),
                                                    VerseDao.Properties.Header.like(s)))
                                       .orderAsc(BookDao.Properties.Indice, VerseDao.Properties.Verse)
                                       .list();

        final ArrayList<String> listaDeVersos = new ArrayList<String>();

        for (int i = 0; i < verses.size(); ++i) {
            String fullVerse = verses.get(i).getText() + "=>";

            Book book = bookDao.queryBuilder()
                    .where(BookDao.Properties.Abbreviation.eq(verses.get(i).getBook())).unique();
            fullVerse += book.getName();
            fullVerse += ", " + verses.get(i).getChapter();
            fullVerse += ":" + verses.get(i).getVerse();

            listaDeVersos.add(fullVerse);
        }

        return listaDeVersos;
    }

    public Verse UltimoCapitulo(String livroSigla) {
        QueryBuilder<Verse> pqb = verseDao.queryBuilder();
        return pqb
                .where(VerseDao.Properties.Book.eq(livroSigla))
                .orderDesc(VerseDao.Properties.Chapter).limit(1).unique();
    }

    public Verse ProximoCapitulo(String nomeDoLivro, int capitulo) {
        Verse proximoCapitulo = null;

        Book book = bookDao.queryBuilder()
                .where(BookDao.Properties.Name.eq(nomeDoLivro)).unique();

        Verse ultimo = UltimoCapitulo(book.getAbbreviation());

        QueryBuilder<Verse> pqb = verseDao.queryBuilder();
        if (capitulo < ultimo.getChapter()) {
            proximoCapitulo = pqb.where(pqb.and(
                                        VerseDao.Properties.Book.eq(book.getAbbreviation()),
                                        VerseDao.Properties.Chapter.eq(capitulo+1)),
                                        VerseDao.Properties.Verse.eq(1))
                                        .unique();
        }
        else {
            Book nextBook = bookDao.queryBuilder()
                    .where(BookDao.Properties.Indice.eq(book.getIndice()+1)).unique();
            pqb = verseDao.queryBuilder();
            if (nextBook != null) {
                proximoCapitulo = pqb.where(pqb.and(
                        VerseDao.Properties.Book.eq(nextBook.getAbbreviation()),
                        VerseDao.Properties.Chapter.eq(1),
                        VerseDao.Properties.Verse.eq(1)))
                        .unique();
            }
        }

        return proximoCapitulo;
    }

    public Verse CapituloAnterior(String nomeDoLivro, int capitulo) {
        Verse capituloAnterior;

        Book book = bookDao.queryBuilder()
                .where(BookDao.Properties.Name.eq(nomeDoLivro)).unique();

        QueryBuilder<Verse> pqb = verseDao.queryBuilder();

        capituloAnterior = pqb.where(pqb.and(
                        VerseDao.Properties.Book.eq(book.getAbbreviation()),
                        VerseDao.Properties.Chapter.eq(capitulo-1)),
                        VerseDao.Properties.Verse.eq(1))
                        .unique();

        if (capituloAnterior == null && book.getIndice() > 1) {
            Book previousBook = bookDao.queryBuilder()
                    .where(BookDao.Properties.Indice.eq(book.getIndice()-1)).unique();

            pqb = verseDao.queryBuilder();
            Verse ultimo = UltimoCapitulo(previousBook.getAbbreviation());

            capituloAnterior = pqb.where(pqb.and(
                    VerseDao.Properties.Book.eq(previousBook.getAbbreviation()),
                    VerseDao.Properties.Chapter.eq(ultimo.getChapter()),
                    VerseDao.Properties.Verse.eq(1)))
                    .unique();
        }

        return capituloAnterior;
    }
}
