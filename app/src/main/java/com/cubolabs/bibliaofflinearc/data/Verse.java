package com.cubolabs.bibliaofflinearc.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

@Entity(nameInDb = "verses", indexes = { @Index(value = "chapter,verse", unique = true)})
public class Verse {
    private Integer chapter;
    private Integer verse;
    private String book;
    private String header;
    private String subheader;
    private String text;


    @Generated(hash = 1740588619)
    public Verse(Integer chapter, Integer verse, String book, String header, String subheader,
            String text) {
        this.chapter = chapter;
        this.verse = verse;
        this.book = book;
        this.header = header;
        this.subheader = subheader;
        this.text = text;
    }
    @Generated(hash = 1505125539)
    public Verse() {
    }


    public Integer getChapter() {
        return this.chapter;
    }
    public void setChapter(Integer chapter) {
        this.chapter = chapter;
    }
    public Integer getVerse() {
        return this.verse;
    }
    public void setVerse(Integer verse) {
        this.verse = verse;
    }
    public String getBook() {
        return this.book;
    }
    public void setBook(String book) {
        this.book = book;
    }
    public String getHeader() {
        return this.header;
    }
    public void setHeader(String header) {
        this.header = header;
    }
    public String getSubheader() {
        return this.subheader;
    }
    public void setSubheader(String subheader) {
        this.subheader = subheader;
    }
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }

}
