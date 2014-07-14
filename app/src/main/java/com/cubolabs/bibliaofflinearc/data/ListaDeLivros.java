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
import com.cubolabs.bibliaofflinearc.R;
import com.cubolabs.bibliaofflinearc.TestamentoDao;

public class ListaDeLivros {
	private BibliaDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
    private TestamentoDao testamentoDao;
	private LivroDao livroDao;

    public ListaDeLivros(Activity activity) {
        db = new BibliaDatabase(activity);

        daoMaster = new DaoMaster(db.getReadableDatabase());
        daoSession = daoMaster.newSession();
        testamentoDao = daoSession.getTestamentoDao();
        livroDao = daoSession.getLivroDao();
    }

    public List<Livro> Todos() {
        return livroDao.queryBuilder().build().list();
    }

	public ArrayList<Integer> Capitulos(String nomeDoLivro) {
		Livro livro = livroDao.queryBuilder()
						.where(LivroDao.Properties.Nome.eq(nomeDoLivro))
                        .unique();
		
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

    public ArrayList<String> NomesVelhoTestamento() {
        Long velhoTestamentoId = testamentoDao.queryBuilder()
                                    .where(TestamentoDao.Properties.Nome.eq("Velho Testamento"))
                                    .unique()
                                    .getId();
        List<Livro> livros = livroDao.queryBuilder()
                                .where(LivroDao.Properties.Id_testamento.eq(velhoTestamentoId))
                                .list();

        return this.ListaNomes(livros);
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

    public ArrayList<String> NomesNovoTestamento() {
        Long novoTestamentoId = testamentoDao.queryBuilder()
                .where(TestamentoDao.Properties.Nome.eq("Novo Testamento"))
                .unique()
                .getId();
        List<Livro> livros = livroDao.queryBuilder()
                .where(LivroDao.Properties.Id_testamento.eq(novoTestamentoId))
                .list();

        return this.ListaNomes(livros);
    }

    public ArrayList<String> NomesNovoTestamentoHorizontal() {
        return this.NomesHorizontal(this.NomesNovoTestamento());
    }

	public ArrayList<String> TodosNomes() {
        return this.ListaNomes(this.Todos());
	}

    private ArrayList<String> ListaNomes(List<Livro> livros) {
        final ArrayList<String> listaDeNomes = new ArrayList<String>();

        for (int i = 0; i < livros.size(); ++i) {
            listaDeNomes.add(livros.get(i).getNome());
        }

        return listaDeNomes;
    }
}
