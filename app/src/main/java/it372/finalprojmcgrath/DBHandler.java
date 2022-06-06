package it372.finalprojmcgrath;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "BOOK_RATINGS";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "MY_RATINGS";
    private static final String TITLE_COL = "TITLE";
    private static final String AUTHOR_COL = "AUTHOR";
    private static final String ISBN_COL = "ISBN";
    private static final String BOOK_FORMAT_COL = "BOOK_FORMAT";
    private static final String READING_REASON_COL = "READING_REASON";
    private static final String GENRE_COL = "GENRE";
    private static final String RATING_COL = "RATING";
    private static final String THOUGHTS_COL = "THOUGHTS";


    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                TITLE_COL + "TEXT NOT NULL," +
                AUTHOR_COL + "TEXT NOT NULL," +
                ISBN_COL + "INTEGER PRIMARY KEY," +
                BOOK_FORMAT_COL + "TEXT NOT NULL," +
                READING_REASON_COL + "TEXT NOT NULL," +
                GENRE_COL + "TEXT NOT NULL," +
                RATING_COL + "INTEGER NOT NULL," +
                THOUGHTS_COL + "TEXT DEFAULT NULL);";
        // execute the
        db.execSQL(createTable);
    }

    /**
     * Inserts a new record into the MY_RATINGS table.
     * @param bookTitle the <code>String</code> title of the book
     * @param bookAuthor the <code>String</code> author of the book
     * @param bookISBN the <code>int</code> 10-digit ISBN of the book
     * @param bookFormat the <code>String</code> format of the book
     * @param readingReasons the <code>String</code> reason(s) for reading the book
     * @param bookGenre the <code>String</code> genre of the book
     * @param bookRating the <code>int</code> user's rating of the book
     * @param thoughts the <code>String</code> thoughts/remarks the user has for the book
     */
    public void addNewRating(String bookTitle, String bookAuthor, int bookISBN, String bookFormat,
                             String readingReasons, String bookGenre, int bookRating, String thoughts) {
        // Create variable for SQLite database
        SQLiteDatabase db = this.getWritableDatabase();
        // Store values
        ContentValues values = new ContentValues();
        values.put(TITLE_COL, bookTitle);
        values.put(AUTHOR_COL, bookAuthor);
        values.put(ISBN_COL, bookISBN);
        values.put(BOOK_FORMAT_COL, bookFormat);
        values.put(READING_REASON_COL, readingReasons);
        values.put(GENRE_COL, bookGenre);
        values.put(RATING_COL, bookRating);
        values.put(THOUGHTS_COL, thoughts);
        // Insert record into database
        db.insert(TABLE_NAME, null, values);
        // Close the database after inserting a record
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
