package com.cubolabs.bibliaofflinearc.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "books")
public class Book {
    @Id
    @Property(nameInDb = "indice")
    private Long indice;
    private String name;
    private String abbreviation;
    private String testament;

    @Generated(hash = 109619910)
    public Book(Long indice, String name, String abbreviation, String testament) {
        this.indice = indice;
        this.name = name;
        this.abbreviation = abbreviation;
        this.testament = testament;
    }

    @Generated(hash = 1839243756)
    public Book() {
    }
    public Long getIndice() {
        return this.indice;
    }
    public void setIndice(Long indice) {
        this.indice = indice;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAbbreviation() {
        return this.abbreviation;
    }
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
    public String getTestament() {
        return this.testament;
    }
    public void setTestament(String testament) {
        this.testament = testament;
    }

}