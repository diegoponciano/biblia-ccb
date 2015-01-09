package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class BibliaDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(3, "greendao");

        Entity verses = schema.addEntity("Verse");
        verses.setTableName("verses");
        verses.addIdProperty().columnName("_rowid_");
        verses.addIntProperty("chapter");
        verses.addIntProperty("verse");
        verses.addStringProperty("book");
        verses.addStringProperty("header");
        verses.addStringProperty("subheader");
        verses.addStringProperty("text");

        Entity books = schema.addEntity("Book");
        books.addLongProperty("ordering").primaryKey().unique();
        books.setTableName("books");
        books.addStringProperty("name");
        books.addStringProperty("abbreviation");
        books.addStringProperty("testament");

        new DaoGenerator().generateAll(schema, args[0]);
    }
}
