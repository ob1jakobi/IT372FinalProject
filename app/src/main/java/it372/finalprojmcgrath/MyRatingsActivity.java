package it372.finalprojmcgrath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Name:    Jake McGrath
 * Proj:    Final Project
 * Due:     08 June 2022
 */
public class MyRatingsActivity extends AppCompatActivity {
    private TextView txtRatings;
    private TextView txtNumRatings;
    private Button btnRateAgain;
    private EditText edtISBN;
    private int index;
    // Database Fields
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ratings);

        txtRatings = findViewById(R.id.txt_rating_display);
        txtNumRatings = findViewById(R.id.txt_num_of_books_rated);
        btnRateAgain = findViewById(R.id.btn_rate_another_book);
        edtISBN = findViewById(R.id.edt_delete_by_isbn);
        index = 1;

        try {
            SQLiteOpenHelper dbh = new RatingsDBHelper(this);
            db = dbh.getReadableDatabase();
            String[] projection = {
                    RatingsDBHelper.TITLE_COL,
                    RatingsDBHelper.AUTHOR_COL,
                    RatingsDBHelper.ISBN_COL,
                    RatingsDBHelper.BOOK_FORMAT_COL,
                    RatingsDBHelper.READING_REASON_COL,
                    RatingsDBHelper.GENRE_COL,
                    RatingsDBHelper.RATING_COL,
                    RatingsDBHelper.THOUGHTS_COL
            };
            // Instantiate the cursor
            cursor = db.query(
                    RatingsDBHelper.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            // Display the number of books rated:
            String numBooks = String.format("Total # of Books Rated: %d", cursor.getCount());
            txtNumRatings.setText(numBooks);

            // Display all of the books that have been rated (stable)
            boolean flag = true;        // Flag variable to start off strong
            cursor.moveToFirst();       // ensure the cursor is on the first record
            RecordList<String> records = new RecordList<>();    // stores/keeps iterated cursor records
            while (flag) {
                String record = String.format("%s: %s | %s | %s | %s | %s | %s | %.1f | %s%n",
                        index,
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getDouble(6),
                        cursor.getString(7));
                records.add(record);
                txtRatings.setText(records.toString());     // displays the records held in tempRecord
                index++;
                cursor.moveToNext();
                if (cursor.isAfterLast()) {
                    flag = false;
                    records.clear();    // save space since the RecordList is no longer needed
                }
            }
        } catch (SQLiteException e) {
            String msg = "something went wrong..." + e.toString();
            Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            toast.show();
        } catch (NullPointerException e) {
            String msg = "Something went wrong..." + e.toString();
            Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            toast.show();
        } catch (CursorIndexOutOfBoundsException e) {
            String msg = "You don't have any ratings. Please click 'Rate Another Book'.";
            Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            toast.show();
        }

        // Rate Again Button functionality
        btnRateAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RatingActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Function that deletes a record from the database based on the ISBN provided in the field
     * <code>edtISBN</code>.
     * @param view
     */
    public void deleteRating(View view) {
        String delRating = edtISBN.getText().toString();
        try {
            RatingsDBHelper.deleteRating(db, delRating);
            Toast toast = Toast.makeText(this, "Rating deleted successfully", Toast.LENGTH_LONG);
            toast.show();
            index--;
        } catch (SQLiteException e) {
            String msg = "something went wrong..." + e.toString();
            Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            toast.show();
        }

    }

    /**
     * "Refreshes" the page to reflect any changes due to new additions/deletions.
     * @param view
     */
    public void refresh(View view) {
        Intent intent = new Intent(this, MyRatingsActivity.class);
        startActivity(intent);
    }

    /**
     * Private class to store records iterated over by the cursor.
     * @param <E>
     */
    private class RecordList<E> extends ArrayList {

        /**
         * Overridden so the TextView that displays the records can be easily iterated over in a
         * format that is simple and easy to recognize.
         * @return a <code>String</code> representation of each record from <cod>db</cod> storing
         * the user's book ratings/reviews.
         */
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (Object item: this) {
                result.append(item);
            }
            return result.toString();
        }
    }
}