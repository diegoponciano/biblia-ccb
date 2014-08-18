package com.cubolabs.bibliaofflinearc.data;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class ListaDeVersiculos {
	private LivroDao livroDao;
	private PalavraDao palavraDao;
	
	public ListaDeVersiculos(Activity activity) {
        livroDao = DaoMaster.getSession(activity).getLivroDao();
        palavraDao = DaoMaster.getSession(activity).getPalavraDao();
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

    public ArrayList<String> Busca(String s) {
        s = "%" + s + "%";
        QueryBuilder<Palavra> pqb = palavraDao.queryBuilder();
        List<Palavra> versiculos = pqb.where(PalavraDao.Properties.Texto.like(s))
                .orderAsc(PalavraDao.Properties.Id_livro)
                .list();

        final ArrayList<String> listaDeVersos = new ArrayList<String>();

        for (int i = 0; i < versiculos.size(); ++i) {
            String fullVerse = versiculos.get(i).getTexto() + "=>";

            fullVerse += versiculos.get(i).getLivro().getNome();
            fullVerse += ", " + versiculos.get(i).getCapitulo();
            fullVerse += ":" + versiculos.get(i).getVersiculo();

            listaDeVersos.add(fullVerse);
        }

        return listaDeVersos;
    }

    public Palavra ProximoCapitulo(String nomeDoLivro, int capitulo) {
        Palavra proximoCapitulo = null;

        Livro livro = livroDao.queryBuilder()
                .where(LivroDao.Properties.Nome.eq(nomeDoLivro)).unique();

        QueryBuilder<Palavra> pqb = palavraDao.queryBuilder();

        proximoCapitulo = pqb.where(pqb.and(
                PalavraDao.Properties.Id_livro.eq(livro.getId()),
                PalavraDao.Properties.Capitulo.eq(capitulo+1)),
                PalavraDao.Properties.Versiculo.eq(1))
                .unique();
        if (proximoCapitulo == null) {
            pqb = palavraDao.queryBuilder();
            proximoCapitulo = pqb.where(pqb.and(
                    PalavraDao.Properties.Id_livro.eq(livro.getId()+1),
                    PalavraDao.Properties.Capitulo.eq(1),
                    PalavraDao.Properties.Versiculo.eq(1)))
                    .unique();
        }

        return proximoCapitulo;
    }

    public Palavra CapituloAnterior(String nomeDoLivro, int capitulo) {
        Palavra capituloAnterior = null;

        Livro livro = livroDao.queryBuilder()
                .where(LivroDao.Properties.Nome.eq(nomeDoLivro)).unique();

        QueryBuilder<Palavra> pqb = palavraDao.queryBuilder();

        capituloAnterior = pqb.where(pqb.and(
                        PalavraDao.Properties.Id_livro.eq(livro.getId()),
                        PalavraDao.Properties.Capitulo.eq(capitulo-1)),
                PalavraDao.Properties.Versiculo.eq(1))
                .unique();
        if (capituloAnterior == null) {
            pqb = palavraDao.queryBuilder();
            capituloAnterior = pqb.where(pqb.and(
                    PalavraDao.Properties.Id_livro.eq(livro.getId()-1),
                    PalavraDao.Properties.Capitulo.eq(1),
                    PalavraDao.Properties.Versiculo.eq(1)))
                    .unique();
        }

        return capituloAnterior;
    }
}
