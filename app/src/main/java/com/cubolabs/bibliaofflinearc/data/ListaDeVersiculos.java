package com.cubolabs.bibliaofflinearc.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;

import com.cubolabs.bibliaofflinearc.DaoMaster;
import com.cubolabs.bibliaofflinearc.DaoSession;
import com.cubolabs.bibliaofflinearc.Livro;
import com.cubolabs.bibliaofflinearc.LivroDao;
import com.cubolabs.bibliaofflinearc.Palavra;
import com.cubolabs.bibliaofflinearc.PalavraDao;

import de.greenrobot.dao.query.QueryBuilder;

public class ListaDeVersiculos {
	private BibliaDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private LivroDao livroDao;
	private PalavraDao palavraDao;
	
	public ListaDeVersiculos(Activity activity) {
		db = new BibliaDatabase(activity);
		
		daoMaster = new DaoMaster(db.getReadableDatabase());
        daoSession = daoMaster.newSession();
		livroDao = daoSession.getLivroDao();
		palavraDao = daoSession.getPalavraDao();
	}

    public ArrayList<Palavra> PorLivroECapitulo(String nomeDoLivro, int capitulo) {
        Livro livro = livroDao.queryBuilder()
                .where(LivroDao.Properties.Nome.eq(nomeDoLivro)).unique();

        QueryBuilder<Palavra> pqb = palavraDao.queryBuilder();
        List<Palavra> versiculos = pqb.where(pqb.and(
                        PalavraDao.Properties.Id_livro.eq(livro.getId()),
                        PalavraDao.Properties.Capitulo.eq(capitulo))
        )
                .orderAsc(PalavraDao.Properties.Versiculo)
                .list();

        return new ArrayList<Palavra>(versiculos);
    }

	public ArrayList<String> PorCapitulo(String nomeDoLivro, int capitulo) {
        ArrayList<Palavra> versiculos = this.PorLivroECapitulo(nomeDoLivro, capitulo);
		
		final ArrayList<String> listaDeNomes = new ArrayList<String>();
		
        for (int i = 0; i < versiculos.size(); ++i) {
            listaDeNomes.add(versiculos.get(i).getTexto());
        }
        
        return listaDeNomes;
	}
	
}
