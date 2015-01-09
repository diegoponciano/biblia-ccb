package com.cubolabs.bibliaofflinearc.data;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import greendao.Book;
import greendao.BookDao;
import greendao.Verse;
import greendao.VerseDao;

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
        List<Verse> verses = pqb.where(pqb.or(VerseDao.Properties.Text.like(s),
                                                    VerseDao.Properties.Header.like(s)))
                                       .orderAsc(VerseDao.Properties.Id)
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

    public Verse ProximoCapitulo(String nomeDoLivro, int capitulo) {
        Verse proximoCapitulo;

        Book book = bookDao.queryBuilder()
                .where(BookDao.Properties.Name.eq(nomeDoLivro)).unique();

        QueryBuilder<Verse> pqb = verseDao.queryBuilder();

        proximoCapitulo = pqb.where(pqb.and(
                VerseDao.Properties.Book.eq(book.getAbbreviation()),
                VerseDao.Properties.Chapter.eq(capitulo+1)),
                VerseDao.Properties.Verse.eq(1))
                .unique();
        if (proximoCapitulo == null) {
            Book nextBook = bookDao.queryBuilder()
                    .where(BookDao.Properties.Ordering.eq(capitulo-1)).unique();
            pqb = verseDao.queryBuilder();
            proximoCapitulo = pqb.where(pqb.and(
                    VerseDao.Properties.Book.eq(nextBook.getAbbreviation()),
                    VerseDao.Properties.Chapter.eq(1),
                    VerseDao.Properties.Verse.eq(1)))
                    .unique();
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
        if (capituloAnterior == null) {
            Book previousBook = bookDao.queryBuilder()
                    .where(BookDao.Properties.Ordering.eq(capitulo-1)).unique();
            pqb = verseDao.queryBuilder();
            capituloAnterior = pqb.where(pqb.and(
                    VerseDao.Properties.Book.eq(previousBook.getAbbreviation()),
                    VerseDao.Properties.Chapter.eq(1),
                    VerseDao.Properties.Verse.eq(1)))
                    .unique();
        }

        return capituloAnterior;
    }
}
