package com.cubolabs.bibliaofflinearc.data;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table PALAVRA.
 */
public class Palavra {

    private Long id;
    private long id_livro;
    private short capitulo;
    private short versiculo;
    private String texto;
    private String cabecalho;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient PalavraDao myDao;

    private Livro livro;
    private Long livro__resolvedKey;


    public Palavra() {
    }

    public Palavra(Long id) {
        this.id = id;
    }

    public Palavra(Long id, long id_livro, short capitulo, short versiculo, String texto, String cabecalho) {
        this.id = id;
        this.id_livro = id_livro;
        this.capitulo = capitulo;
        this.versiculo = versiculo;
        this.texto = texto;
        this.cabecalho = cabecalho;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPalavraDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getId_livro() {
        return id_livro;
    }

    public void setId_livro(long id_livro) {
        this.id_livro = id_livro;
    }

    public short getCapitulo() {
        return capitulo;
    }

    public void setCapitulo(short capitulo) {
        this.capitulo = capitulo;
    }

    public short getVersiculo() {
        return versiculo;
    }

    public void setVersiculo(short versiculo) {
        this.versiculo = versiculo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getCabecalho() {
        return cabecalho;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    /** To-one relationship, resolved on first access. */
    public Livro getLivro() {
        long __key = this.id_livro;
        if (livro__resolvedKey == null || !livro__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LivroDao targetDao = daoSession.getLivroDao();
            Livro livroNew = targetDao.load(__key);
            synchronized (this) {
                livro = livroNew;
            	livro__resolvedKey = __key;
            }
        }
        return livro;
    }

    public void setLivro(Livro livro) {
        if (livro == null) {
            throw new DaoException("To-one property 'id_livro' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.livro = livro;
            id_livro = livro.getId();
            livro__resolvedKey = id_livro;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
