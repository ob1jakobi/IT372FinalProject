package it372.finalprojmcgrath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Name:    Jake McGrath
 * Proj:    Final Project
 * Due:     08 June 2022
 */
public class RatingActivity extends AppCompatActivity {
    // Widget fields
    private EditText edtTitle;
    private EditText edtAuthor;
    private EditText edtISBN;
    private RadioGroup radGroup;
    private RadioButton radBook;
    private RadioButton radEbook;
    private RadioButton radAudiobook;
    private CheckBox chkSelfImprovement;
    private CheckBox chkStressReduction;
    private CheckBox chkKnowledgeEnhancement;
    private CheckBox chkEntertainment;
    private CheckBox chkRequiredReading;
    private Spinner spnGenre;
    private RatingBar ratingBar;
    private EditText edtUsersThoughts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        // Initialize first group of EditText fields:
        edtTitle = findViewById(R.id.edt_title_entry);
        edtAuthor = findViewById(R.id.edt_author_entry);
        edtISBN = findViewById(R.id.edt_isbn_entry);
        // Initialize the RadioGroup and RadioButtons:
        radGroup = findViewById(R.id.radGroup_book_format);
        radBook = findViewById(R.id.rad_physical_format);
        radEbook = findViewById(R.id.rad_ebook_format);
        radAudiobook = findViewById(R.id.rad_audiobook_format);
        // Initialize the CheckBoxes:
        chkSelfImprovement = findViewById(R.id.chk_self_improvement);
        chkStressReduction = findViewById(R.id.chk_reduce_stress);
        chkKnowledgeEnhancement = findViewById(R.id.chk_knowledge_enhancement);
        chkEntertainment = findViewById(R.id.chk_entertainment);
        chkRequiredReading = findViewById(R.id.chk_required_reading);
        // Initialize the Spinner
        spnGenre = findViewById(R.id.spn_genre);
        // Initialize the RatingBar:
        ratingBar = findViewById(R.id.ratingBar_overall_rating);
        // Initialize the EditText for the user's thoughts:
        edtUsersThoughts = findViewById(R.id.edt_user_thoughts);

        // Ensure rotation doesn't destroy the information entered by the user:
        if (savedInstanceState != null) {
            edtTitle.setText(savedInstanceState.getString("title"));
            edtAuthor.setText(savedInstanceState.getString("author"));
            edtISBN.setText(savedInstanceState.getString("isbn"));
            // restore RadioButton
            String tempFormat = savedInstanceState.getString("format");
            switch (tempFormat) {
                case "eBook":
                    radEbook.toggle();
                    break;
                case "Audiobook":
                    radAudiobook.toggle();
                    break;
                default:
                    radBook.toggle();
                    break;
            }

            // restore reading reasons CheckBoxes
            ArrayList<String> tempReasons = savedInstanceState.getStringArrayList("readingReasons");
            for (String reason: tempReasons) {
                if (reason.equalsIgnoreCase("required reading")) {
                    chkRequiredReading.toggle();
                } else if (reason.equalsIgnoreCase("self improvement")) {
                    chkSelfImprovement.toggle();
                } else if (reason.equalsIgnoreCase("knowledge enhancement")) {
                    chkKnowledgeEnhancement.toggle();
                } else if (reason.equalsIgnoreCase("entertainment")) {
                    chkEntertainment.toggle();
                } else if (reason.equalsIgnoreCase("stress reduction")) {
                    chkStressReduction.toggle();
                } else {
                    chkStressReduction.toggle();
                }
            }

            // restore genre Spinner
            String compareValue = savedInstanceState.getString("genre");
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.book_genre, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnGenre.setAdapter(adapter);
            if (compareValue != null) {
                int spinnerPosition = adapter.getPosition(compareValue);
                spnGenre.setSelection(spinnerPosition);
            }
            // restore rating
            double tempRating = savedInstanceState.getDouble("rating");
            ratingBar.setRating((float) tempRating);
            // restore user's thoughts
            edtUsersThoughts.setText(savedInstanceState.getString("thoughts"));
        }
    }

    /**
     * Function that clears all of the entries/fields on the rating activity.
     * @param view
     */
    public void clearEntries(View view) {
        // clear title, author, and isbn
        edtTitle.getText().clear();
        edtAuthor.getText().clear();
        edtISBN.getText().clear();
        // clear RadioGroup
        radGroup.clearCheck();
        // clear reasons for reading CheckBoxes
        if (chkSelfImprovement.isChecked()) {
            chkSelfImprovement.toggle();
        } if (chkStressReduction.isChecked()) {
            chkStressReduction.toggle();
        } if (chkKnowledgeEnhancement.isChecked()) {
            chkKnowledgeEnhancement.toggle();
        } if (chkEntertainment.isChecked()) {
            chkEntertainment.toggle();
        } if (chkRequiredReading.isChecked()) {
            chkRequiredReading.toggle();
        }
        // reset genre Spinner
        spnGenre.setSelection(0);
        // reset RatingBar
        ratingBar.setRating(0);
        // reset thoughts EditText
        edtUsersThoughts.getText().clear();
    }

    /**
     * Function that saves the new rating as a record in a database.
     * @param view
     */
    public void submitRating(View view) {
        if (allFieldsComplete()) {
            // Get Title, Author, ISBN
            String title = edtTitle.getText().toString();
            String author = edtAuthor.getText().toString();
            String ISBN = edtISBN.getText().toString();
            // Get the book's format
            String format = "";
            if (radBook.isChecked()) {
                format = "Book";
            } else if (radEbook.isChecked()) {
                format = "eBook";
            } else {
                format = "Audiobook";
            }
            // Get the reasons for reading
            ArrayList<String> rReasons = new ArrayList<>();
            if (chkSelfImprovement.isChecked()) {
                rReasons.add("Self Improvement");
            } if (chkStressReduction.isChecked()) {
                rReasons.add("Stress Reduction");
            } if (chkKnowledgeEnhancement.isChecked()) {
                rReasons.add("Knowledge Enhancement");
            } if (chkEntertainment.isChecked()) {
                rReasons.add("Entertainment");
            } if (chkRequiredReading.isChecked()) {
                rReasons.add("Required Reading");
            }
            StringBuilder allReadingReasons = new StringBuilder();
            for (int i = 0; i < rReasons.size(); i++) {
                if (i == rReasons.size() - 1) {
                    allReadingReasons.append(rReasons.get(i));
                } else {
                    allReadingReasons.append(rReasons.get(i)).append(", ");
                }
            }
            // Get the genre
            String genre = spnGenre.getSelectedItem().toString();
            // Get the rating
            double rating = ratingBar.getRating();
            // Get the user's thoughts
            String thoughts = edtUsersThoughts.getText().toString();

            // Instantiate database and insert the new rating into the database
            SQLiteOpenHelper dbh = new RatingsDBHelper(this);
            try {
                SQLiteDatabase db = dbh.getWritableDatabase();
                RatingsDBHelper.insertNewRating(db, title, author, ISBN, format,
                        allReadingReasons.toString(), genre, rating, thoughts);
            } catch (SQLiteException e) {
                Toast toast = Toast.makeText(this, e.toString(), Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // save EditText entries for Title, Author and ISBN
        savedInstanceState.putString("title", edtTitle.getText().toString());
        savedInstanceState.putString("author", edtAuthor.getText().toString());
        savedInstanceState.putString("isbn", edtISBN.getText().toString());

        // Logic for capturing RadioButton contents
        String formatTemp;
        if (radBook.isChecked()) {
            formatTemp = "Book";
        } else if (radEbook.isChecked()) {
            formatTemp = "eBook";
        } else {
            formatTemp = "Audiobook";
        }
        savedInstanceState.putString("format", formatTemp);

        // Logic for capturing reading reasons CheckBox content(s)
        ArrayList<String> reasonsTemp = new ArrayList<>();
        if (chkRequiredReading.isChecked()) {
            reasonsTemp.add("Required Reading");
        } if (chkSelfImprovement.isChecked()) {
            reasonsTemp.add("Self Improvement");
        } if (chkEntertainment.isChecked()) {
            reasonsTemp.add("Entertainment");
        } if (chkStressReduction.isChecked()) {
            reasonsTemp.add("Reduce Stress");
        } if (chkKnowledgeEnhancement.isChecked()) {
            reasonsTemp.add("Knowledge Enhancement");
        }
        savedInstanceState.putStringArrayList("readingReasons", reasonsTemp);

        // save Spinner selection
        savedInstanceState.putString("genre", spnGenre.getSelectedItem().toString());

        // save RatingBar rating
        savedInstanceState.putDouble("rating", ratingBar.getRating());

        // save EditText entry for user's thoughts
        savedInstanceState.putString("thoughts", edtUsersThoughts.getText().toString());
    }

    /**
     * Helper function to ensure all fields are entered by the user (except for the user's thoughts
     * EditText entry).  If an entry is blank and the user tries to submit the form, then a Toast
     * message will display informing the user to complete all fields, informing them of which
     * entry field(s) must be changed.  If all entries successfully made, then a Toast message will
     * display informing the user that the entry submission has been successful.
     * @return <code>true</code> iff all fields are entered correctly, otherwise false.
     */
    private boolean allFieldsComplete() {
        boolean flag = false;
        String text = "Please enter/choose a valid ";
        String title = edtTitle.getText().toString();
        String author = edtAuthor.getText().toString();
        String ISBN = edtISBN.getText().toString();
        if (title.isEmpty()) {
            text += "Title";
        } if (author.isEmpty()) {
            text += text.length() == 28 ? "Author's name." : ", Author's name";
        } if (ISBN.isEmpty()) {
            text += text.length() == 28 ? "ISBN." : ", ISBN";
        } if (ISBN.length() != 10) {
            text += text.length() == 28 ? "ISBN (must be 10-digits)." : ", ISBN (must be 10-digits)";
        } if (!radBook.isChecked() && !radEbook.isChecked() && !radAudiobook.isChecked()) {
            text += text.length() == 28 ? "book format." : ", book format";
        } if (!chkEntertainment.isChecked() && !chkKnowledgeEnhancement.isChecked()
                && !chkRequiredReading.isChecked() && !chkStressReduction.isChecked() &&
                !chkSelfImprovement.isChecked()) {
            text += text.length() == 28 ? "reason for reading the book (at least one)" : ", reason for" +
                    " reading the book (at least one)";
        } if (spnGenre.getSelectedItem().toString().equalsIgnoreCase("select a genre")) {
            text += text.length() == 28 ? "genre." : ", genre";
        } if (ratingBar.getRating() == 0.0) {
            text += text.length() == 28 ? "rating (at least 0.5 stars)." : ", rating (at least 0.5 stars).";
        } if (text.length() == 28) {    // no unfilled/select entries because text hasn't changed
            text = "Book Reviewed Submitted Successfully";
            flag = true;
        }
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
        return flag;
    }

    /**
     * Navigates to the activity where the user can see their ratings.
     * @param view
     */
    public void seeRatings(View view) {
        Intent intent = new Intent(this, MyRatingsActivity.class);
        startActivity(intent);
    }

}