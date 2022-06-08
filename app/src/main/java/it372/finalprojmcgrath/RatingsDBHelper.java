package it372.finalprojmcgrath;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Name:    Jake McGrath
 * Proj:    Final Project
 * Due:     08 June 2022
 */
public class RatingsDBHelper extends SQLiteOpenHelper {

    protected static final String DB_NAME = "BOOK_RATINGS.db";
    protected static final int DB_VERSION = 1;
    protected static final String TABLE_NAME = "MY_RATINGS";
    protected static final String TITLE_COL = "TITLE";
    protected static final String AUTHOR_COL = "AUTHOR";
    protected static final String ISBN_COL = "ISBN";
    protected static final String BOOK_FORMAT_COL = "BOOK_FORMAT";
    protected static final String READING_REASON_COL = "READING_REASON";
    protected static final String GENRE_COL = "GENRE";
    protected static final String RATING_COL = "RATING";
    protected static final String THOUGHTS_COL = "THOUGHTS";


    public RatingsDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Table-text schema for the MY_RATINGS table is as follows:
     * MY_RATINGS(TITLE, AUTHOR, ISBN, BOOK_FORMAT, READING_REASON, GENRE, RATING, THOUGHTS), where
     * ISBN is the primary key for the table.
     * @param db the <code>SQLiteDatabase</code> that will be used for creating a new table.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                TITLE_COL + " TEXT NOT NULL," +
                AUTHOR_COL + " TEXT NOT NULL," +
                ISBN_COL + " TEXT PRIMARY KEY," +
                BOOK_FORMAT_COL + " TEXT NOT NULL," +
                READING_REASON_COL + " TEXT NOT NULL," +
                GENRE_COL + " TEXT NOT NULL," +
                RATING_COL + " REAL NOT NULL," +
                THOUGHTS_COL + " TEXT DEFAULT NULL);";
        // execute the
        db.execSQL(createTable);
    }

    /**
     * Inserts a new record into the MY_RATINGS table.
     * @param db the <code>database</code> that will have a new rating inserted into it
     * @param bookTitle the <code>String</code> title of the book
     * @param bookAuthor the <code>String</code> author of the book
     * @param bookISBN the <code>String</code> 10-digit ISBN of the book (primary key)
     * @param bookFormat the <code>String</code> format of the book
     * @param readingReasons the <code>String</code> reason(s) for reading the book
     * @param bookGenre the <code>String</code> genre of the book
     * @param bookRating the <code>double</code> user's rating of the book
     * @param thoughts the <code>String</code> thoughts/remarks the user has for the book
     */
    public static void insertNewRating(@NonNull SQLiteDatabase db, @NonNull String bookTitle, @NonNull String bookAuthor,
                                       @NonNull String bookISBN, @NonNull String bookFormat,
                                       @NonNull String readingReasons, @NonNull String bookGenre,
                                       double bookRating, @Nullable String thoughts) {
        // Ensures the entries that need info are not empty strings
        if (bookTitle.isEmpty() || bookAuthor.isEmpty() || bookISBN.isEmpty() || bookFormat.isEmpty() ||
        readingReasons.isEmpty() || bookGenre.isEmpty()) {
            return;
        }
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
    }

    /**
     * Deletes/removes a record from the MY_RATINGS table that has a matching ISBN.
     * @param db the <code>database</code> that will have the record removed from it.
     * @param ISBN the <code>String</code> representation of the 10-digit ISBN number.
     */
    public static void deleteRating(@NonNull SQLiteDatabase db, @NonNull String ISBN) {
        // Prevent deletion of all records in the db
        if (ISBN.isEmpty()) {
            return;
        }
        db.delete(TABLE_NAME, ISBN_COL + " LIKE ?", new String[] {ISBN});
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
