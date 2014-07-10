package com.cubolabs.bibliaofflinearc.data;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;

import com.cubolabs.bibliaofflinearc.DaoMaster;
import com.cubolabs.bibliaofflinearc.DaoSession;
import com.cubolabs.bibliaofflinearc.Livro;
import com.cubolabs.bibliaofflinearc.LivroDao;
import com.cubolabs.bibliaofflinearc.PalavraDao;

public class ListaDeLivros {
	private BibliaDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private LivroDao livroDao;
	
	public ListaDeLivros(Activity activity) {
		db = new BibliaDatabase(activity);
		
		daoMaster = new DaoMaster(db.getReadableDatabase());
        daoSession = daoMaster.newSession();
		livroDao = daoSession.getLivroDao();
	}
	
	public List<Livro> Todos() {
		return livroDao.queryBuilder().build().list();
	}
	
	public ArrayList<Integer> Capitulos(String nomeDoLivro) {
		Livro livro = livroDao.queryBuilder()
						.where(LivroDao.Properties.Nome.eq(nomeDoLivro)).unique();
		
		String idLivro = livro.getId().toString();
		
		Cursor cursor = db.getReadableDatabase().query(true, PalavraDao.TABLENAME, 
				new String[] { PalavraDao.Properties.Capitulo.columnName }, 
				PalavraDao.Properties.Id_livro.columnName + "=?",
				new String[] { idLivro }, 
				PalavraDao.Properties.Capitulo.columnName, null, null, null);
		
		ArrayList<Integer> numerosCapitulos = new ArrayList<Integer>();
		
		for(int i=1; i<= cursor.getCount(); i++) {
			numerosCapitulos.add(i);
		}
		
		return numerosCapitulos;
	}
	
	public ArrayList<String> TodosNomes() {
		final ArrayList<String> listaDeNomes = new ArrayList<String>();
		
        for (int i = 0; i < this.Todos().size(); ++i) {
        	listaDeNomes.add(this.Todos().get(i).getNome());
        }
        
        return listaDeNomes;
	}
}
