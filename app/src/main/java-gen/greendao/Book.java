package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table books.
 */
public class Book {

    private Long indice;
    private String name;
    private String abbreviation;
    private String testament;

    public Book() {
    }

    public Book(Long indice) {
        this.indice = indice;
    }

    public Book(Long indice, String name, String abbreviation, String testament) {
        this.indice = indice;
        this.name = name;
        this.abbreviation = abbreviation;
        this.testament = testament;
    }

    public Long getIndice() {
        return indice;
    }

    public void setIndice(Long indice) {
        this.indice = indice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getTestament() {
        return testament;
    }

    public void setTestament(String testament) {
        this.testament = testament;
    }

}
