package it372.finalprojmcgrath;

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

            // Display all of the books that have been rated
            boolean flag = true;
            cursor.moveToFirst();
            StringBuilder tempRecord = new StringBuilder();
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
                tempRecord.append(record);
                txtRatings.setText(tempRecord.toString());
                index++;
                cursor.moveToNext();
                if (cursor.isAfterLast()) {
                    flag = false;
                }
            }
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, e.toString(), Toast.LENGTH_LONG);
            toast.show();
        } catch (NullPointerException e) {
            Toast toast = Toast.makeText(this, e.toString(), Toast.LENGTH_LONG);
            toast.show();
        } catch (CursorIndexOutOfBoundsException e) {
            String prompt = "You don't have any ratings. Please click 'Rate Another Book'.";
            Toast toast = Toast.makeText(this, prompt, Toast.LENGTH_LONG);
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
            Toast toast = Toast.makeText(this, "something went wrong...", Toast.LENGTH_LONG);
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
}