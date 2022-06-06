package it372.finalprojmcgrath;

import androidx.appcompat.app.AppCompatActivity;
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
    private Button btnSubmit;
    private Button btnClear;

    // Fields for SQL handling
    private String title;
    private String author;
    private int ISBN;
    private String format;
    private ArrayList<String> readingReasons;
    private String genre;
    private int rating;
    private String thoughts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        // Initialize the fields
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
        // Initialize the two Buttons:
        btnSubmit = findViewById(R.id.btn_submit);
        btnClear = findViewById(R.id.btn_clear);

        // Ensure rotation doesn't destroy the information entered by the user:
        if (savedInstanceState != null) {
            edtTitle.setText(savedInstanceState.getString("title"));
            edtAuthor.setText(savedInstanceState.getString("author"));
            int test = savedInstanceState.getInt("isbn");
            edtISBN.setText(Integer.toString(test));
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
            int tempRating = savedInstanceState.getInt("rating");
            ratingBar.setRating(tempRating);
            // restore user's thoughts
            edtUsersThoughts.setText(savedInstanceState.getString("thoughts"));
        }

        // Submit button functionality:
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allFieldsComplete()) {
                    // TODO: enter information into SQLite database
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO clear all entries
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // save EditText entries for Title, Author and ISBN
        savedInstanceState.putString("title", edtTitle.getText().toString());
        savedInstanceState.putString("author", edtAuthor.getText().toString());
        savedInstanceState.putInt("isbn", Integer.parseInt(edtISBN.getText().toString()));

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
        savedInstanceState.putInt("rating", ratingBar.getNumStars());

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
                && !chkStressReduction.isChecked() && !chkStressReduction.isChecked()) {
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
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
        return flag;
    }

}