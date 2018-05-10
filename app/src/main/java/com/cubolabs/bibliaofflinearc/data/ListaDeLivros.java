package com.cubolabs.bibliaofflinearc.data;

import android.app.Activity;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class ListaDeLivros {
	private BibliaDatabase db;
	private BookDao bookDao;

    public ListaDeLivros(Activity activity) {
        db = BibliaDatabase.getInstance(activity);
        bookDao = BibliaDatabase.getSession(activity).getBookDao();
    }

    public List<Book> Todos() {
        return bookDao.queryBuilder().build().list();
    }

    public Book ByAbbreviation(String bookAbbrev)
    {
        return bookDao.queryBuilder()
                .where(BookDao.Properties.Abbreviation.eq(bookAbbrev))
                .unique();
    }

	public ArrayList<Integer> Capitulos(String bookName) {
        Book book = bookDao.queryBuilder()
						.where(BookDao.Properties.Name.eq(bookName))
                        .unique();
		
		String bookAbbreviation = book.getAbbreviation();
		
		Cursor cursor = db.getReadableDatabase().query(true, VerseDao.TABLENAME,
				new String[] { VerseDao.Properties.Chapter.columnName },
                VerseDao.Properties.Book.columnName + "=?",
				new String[] { bookAbbreviation },
                VerseDao.Properties.Chapter.columnName, null, null, null);
		
		ArrayList<Integer> chapterNumbers = new ArrayList<Integer>();
		
		for(int i=1; i<= cursor.getCount(); i++) {
            chapterNumbers.add(i);
		}
        cursor.close();
		
		return chapterNumbers;
	}

    public ArrayList<String> NomesVelhoTestamento() {
        List<Book> books = bookDao.queryBuilder()
                                .where(BookDao.Properties.Testament.eq("ot"))
                                .list();
        return this.ListaNomes(books);
    }

    public ArrayList<String> NomesNovoTestamento() {
        List<Book> books = bookDao.queryBuilder()
                .where(BookDao.Properties.Testament.eq("nt"))
                .list();
        return this.ListaNomes(books);
    }

    private ArrayList<String> NomesHorizontal(ArrayList<String> nomes){
        ArrayList<String> nomesHorizontal = new ArrayList<String>();
        int startIndex = nomes.size()%2==0
                ? nomes.size()/2
                : (nomes.size()/2) + 1;

        for (int i = 0; i < startIndex; i++) {
            nomesHorizontal.add(nomes.get(i));
        }
        int evenPosition = 1;
        for (int i = startIndex; i < nomes.size(); i++) {
            nomesHorizontal.add(evenPosition, nomes.get(i));
            evenPosition += 2;
        }
        return nomesHorizontal;
    }

    public ArrayList<String> NomesVelhoTestamentoHorizontal() {
        return this.NomesHorizontal(this.NomesVelhoTestamento());
    }

    public ArrayList<String> NomesNovoTestamentoHorizontal() {
        return this.NomesHorizontal(this.NomesNovoTestamento());
    }

	public ArrayList<String> TodosNomes() {
        return this.ListaNomes(this.Todos());
	}

    private ArrayList<String> ListaNomes(List<Book> books) {
        final ArrayList<String> nameList = new ArrayList<String>();

        for (int i = 0; i < books.size(); ++i) {
            nameList.add(books.get(i).getName());
        }

        return nameList;
    }
}
