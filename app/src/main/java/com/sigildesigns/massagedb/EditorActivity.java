package com.sigildesigns.massagedb;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sigildesigns.massagedb.data.ClientContract;

public class EditorActivity extends AppCompatActivity {

    String clientIDString;

    ImageView itWorksImageView, genderImageView, importantImageView;
    EditText firstNameEditText, lastNameEditText, lastServiceEditText, notesEditText,
            importantNotesEditText,
            emailEditText, phoneEditText, birthdayEditText, goalsEditText, referralEditText,
            massagedPreviouslyDateEditText, pregnantWeeksEditText, lastPregnancyEditText,
            medicationsEditText, recentSurgeriesEditText, healthIssuesEditText,
            emergencyContactNameEditText, emergencyContactPhoneEditText, derogatoryNotesEditText;
    CheckBox massagedPreviouslyCheckBox, pregnantCheckBox, numbnessCheckBox, swellingCheckBox,
            headachesCheckBox;

    Uri queryUri;
    Intent intent;
    Cursor clientCursor;
    Long clientIDLong;

    int mItWorks;
    int mGender;
    int mImportant;

    // Boolean for tracking whether or not the user has made any changes to the client.
    private boolean changesMade = false;

    // OnTouchListener that listens for any user touches on a View, implying that they are modifying
    // the view, and we change the itemHasChanged boolean to true.
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            changesMade = true;
            return false;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        itWorksImageView = (ImageView) findViewById(R.id.editor_it_works_iv);
        genderImageView = (ImageView) findViewById(R.id.editor_gender_iv);
        importantImageView = (ImageView) findViewById(R.id.editor_important_iv);
        firstNameEditText = (EditText) findViewById(R.id.editor_first_name_et);
        lastNameEditText = (EditText) findViewById(R.id.editor_last_name_et);
        lastServiceEditText = (EditText) findViewById(R.id.editor_last_service_et);
        notesEditText = (EditText) findViewById(R.id.editor_notes_et);
        importantNotesEditText = (EditText) findViewById(R.id.editor_important_notes_et);
        emailEditText = (EditText) findViewById(R.id.editor_email_et);
        phoneEditText = (EditText) findViewById(R.id.editor_phone_et);
        birthdayEditText = (EditText) findViewById(R.id.editor_birthday_et);
        goalsEditText = (EditText) findViewById(R.id.editor_goals_et);
        referralEditText = (EditText) findViewById(R.id.editor_referral_et);
        massagedPreviouslyCheckBox = (CheckBox) findViewById(R.id
                .editor_massaged_previously_checkbox);
        massagedPreviouslyDateEditText = (EditText) findViewById(R.id
                .editor_massaged_previously_date_et);
        pregnantCheckBox = (CheckBox) findViewById(R.id.editor_pregnant_checkbox);
        pregnantWeeksEditText = (EditText) findViewById(R.id.editor_pregnant_weeks_et);
        lastPregnancyEditText = (EditText) findViewById(R.id.editor_last_pregnancy_et);
        medicationsEditText = (EditText) findViewById(R.id.editor_medications_et);
        recentSurgeriesEditText = (EditText) findViewById(R.id.editor_recent_surgeries_et);
        healthIssuesEditText = (EditText) findViewById(R.id.editor_health_issues_et);
        numbnessCheckBox = (CheckBox) findViewById(R.id.editor_numbness_checkbox);
        swellingCheckBox = (CheckBox) findViewById(R.id.editor_swelling_checkbox);
        headachesCheckBox = (CheckBox) findViewById(R.id.editor_headaches_checkbox);
        emergencyContactNameEditText = (EditText) findViewById(R.id
                .editor_emergency_contact_name_et);
        emergencyContactPhoneEditText = (EditText) findViewById(R.id
                .editor_emergency_contact_phone_et);
        derogatoryNotesEditText = (EditText) findViewById(R.id.editor_derogatory_notes_et);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user has
        // touched or modified them. This will let us know if there are unsaved changes or not,
        // if the user tries to leave the editor without saving.
        itWorksImageView.setOnTouchListener(touchListener);
        genderImageView.setOnTouchListener(touchListener);
        importantImageView.setOnTouchListener(touchListener);
        firstNameEditText.setOnTouchListener(touchListener);
        lastNameEditText.setOnTouchListener(touchListener);
        lastServiceEditText.setOnTouchListener(touchListener);
        notesEditText.setOnTouchListener(touchListener);
        importantNotesEditText.setOnTouchListener(touchListener);
        emailEditText.setOnTouchListener(touchListener);
        phoneEditText.setOnTouchListener(touchListener);
        birthdayEditText.setOnTouchListener(touchListener);
        goalsEditText.setOnTouchListener(touchListener);
        referralEditText.setOnTouchListener(touchListener);
        massagedPreviouslyCheckBox.setOnTouchListener(touchListener);
        massagedPreviouslyDateEditText.setOnTouchListener(touchListener);
        pregnantCheckBox.setOnTouchListener(touchListener);
        pregnantWeeksEditText.setOnTouchListener(touchListener);
        lastPregnancyEditText.setOnTouchListener(touchListener);
        medicationsEditText.setOnTouchListener(touchListener);
        recentSurgeriesEditText.setOnTouchListener(touchListener);
        healthIssuesEditText.setOnTouchListener(touchListener);
        numbnessCheckBox.setOnTouchListener(touchListener);
        swellingCheckBox.setOnTouchListener(touchListener);
        headachesCheckBox.setOnTouchListener(touchListener);
        emergencyContactPhoneEditText.setOnTouchListener(touchListener);
        emergencyContactNameEditText.setOnTouchListener(touchListener);
        derogatoryNotesEditText.setOnTouchListener(touchListener);

        // Get the intent, pull the clientID from it, and query the database for that client.
        intent = getIntent();
        loadData();

        // Sets OnClickListener for the itWorks
        itWorksImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itWorksImageView.getAlpha() == 0.15f) {
                    // if alpha is 0.15 then itWorks is not enabled, enable it
                    setAlpha(itWorksImageView, 1);
                    mItWorks = 1;
                } else {
                    // if alpha is not 0.15 then itWorks is enabled, disable it.
                    setAlpha(itWorksImageView, 0.15f);
                    mItWorks = 0;
                }
            }
        });

        // Sets OnClickListener for gender
        genderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayGenderDialog();
            }
        });

        // Sets OnClickListener for important
        importantImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (importantImageView.getAlpha() == 0.15f) {
                    // if alpha is 0.15 then important is not enabled, enable it.
                    setAlpha(importantImageView, 1);
                    mImportant = 1;
                } else {
                    // if alpha is not 0.15, then important is enabled, disable it.
                    setAlpha(importantImageView, 0.15f);
                    mImportant = 0;
                }
            }
        });
    }

    public void displayGenderDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Please select gender.");
        alert.setTitle("Edit Gender");
        alert.setPositiveButton("Male", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                genderImageView.setImageResource(R.drawable.male);
                mGender = 1;
            }
        });
        alert.setNegativeButton("Female", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                genderImageView.setImageResource(R.drawable.female);
                mGender = 0;
            }
        });
        alert.show();
    }

    // Helper method to set alpha on the imageviews
    private void setAlpha(View v, float alphaValue) {
        v.setAlpha(alphaValue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // If changesMade is not true, continue with handling back button press
        if (!changesMade) {
            setResult(1);
            super.onBackPressed();
            return;
        }
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface
                .OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked "Discard" button, close the current activity.
                setResult(1);
                finish();
            }
        };
        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    // Show a dialog that warns the user they are about to leave the activity with unsaved changes.
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners for the
        // positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_dialog_discard_changes);
        builder.setPositiveButton(R.string.alert_dialog_positive_button,
                discardButtonClickListener);
        builder.setNegativeButton(R.string.alert_dialog_negative_button, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog and continue
                // editing the item.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Shows a dialog that warns the user they are about to delete the client
    private void showDeleteClientDialog() {
        // Create an AlertDialog.Builder and set the message and click listeners for the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_this_client_question);
        builder.setPositiveButton(R.string.delete_client, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int rowsDeleted = getContentResolver().delete(queryUri, null, null);
                if (rowsDeleted != 0) {
                    Toast.makeText(EditorActivity.this, "Client Deleted", Toast.LENGTH_SHORT)
                            .show();
                    setResult(0);
                    finish();
                } else {
                    Toast.makeText(EditorActivity.this, "Error Deleting client", Toast
                            .LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.editor_action_save:
                saveChanges();
                return true;
            case R.id.editor_action_cancel:
                setResult(1);
                finish();
                return true;
            case R.id.editor_action_delete:
                showDeleteClientDialog();
                return true;
            case android.R.id.home:
                // If the item hasn't changed, continue with navigating up to parent activity
                // which is the {@link ListActivity}.
                if (!changesMade) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean intToBoolean(int input) {
        if (input == 1) {
            return true;
        } else {
            return false;
        }
    }

    private void loadData() {

        clientIDString = intent.getStringExtra("clientID");
        clientIDLong = Long.valueOf(clientIDString);
        String queryString = ClientContract.ClientEntry.CONTENT_URI.toString();
        queryString = queryString + "/" + clientIDString;
        queryUri = Uri.parse(queryString);
        clientCursor = getContentResolver().query(queryUri, null, null, null, null);
        clientCursor.moveToFirst();

        // Pull information from the cursor and fill the views
        boolean itWorks = intToBoolean(clientCursor.getInt(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_IT_WORKS)));
        if (itWorks) {
            setAlpha(itWorksImageView, 1);
            mItWorks = 1;

        } else {
            setAlpha(itWorksImageView, 0.15f);
            mItWorks = 0;
        }

        boolean important = intToBoolean(clientCursor.getInt(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_IMPORTANT)));
        if (important) {
            setAlpha(importantImageView, 1);
            mImportant = 1;
        } else {
            setAlpha(importantImageView, 0.15f);
            mImportant = 0;
        }

        boolean gender = intToBoolean(clientCursor.getInt(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_GENDER)));
        if (gender) {
            genderImageView.setImageResource(R.drawable.male);
            mGender = 1;
        } else {
            genderImageView.setImageResource(R.drawable.female);
            mGender = 0;
        }

        String firstName = clientCursor.getString(clientCursor.getColumnIndex(ClientContract
                .ClientEntry.COLUMN_FIRST_NAME));
        firstNameEditText.setText(firstName);

        String lastName = clientCursor.getString(clientCursor.getColumnIndex(ClientContract
                .ClientEntry.COLUMN_LAST_NAME));
        lastNameEditText.setText(lastName);

        lastServiceEditText.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract
                        .ClientEntry.COLUMN_LAST_SERVICE)));

        notesEditText.setText(clientCursor.getString(clientCursor.getColumnIndex(ClientContract
                .ClientEntry.COLUMN_NOTES)));

        importantNotesEditText.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_IMPORTANT_NOTES)));

        String email = clientCursor.getString(clientCursor.getColumnIndex(ClientContract
                .ClientEntry.COLUMN_EMAIL));
        emailEditText.setText(email);

        String phoneNumber = clientCursor.getString(clientCursor.getColumnIndex(ClientContract
                .ClientEntry.COLUMN_PHONE_NUMBER));
        phoneEditText.setText(phoneNumber);

        birthdayEditText.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_BIRTHDAY)));

        goalsEditText.setText(clientCursor.getString(clientCursor.getColumnIndex(ClientContract
                .ClientEntry.COLUMN_GOALS)));

        referralEditText.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_REFERRAL)));

        boolean massagedPreviously = intToBoolean(clientCursor.getInt(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_HAD_MASSAGE_PREVIOUSLY)));
        if (massagedPreviously) {
            massagedPreviouslyCheckBox.setChecked(true);
        }

        String massagedPreviouslyDate = clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_HAD_MASSAGE_PREVIOUSLY_DATE));
        massagedPreviouslyDateEditText.setText(massagedPreviouslyDate);

        boolean pregnant = intToBoolean(clientCursor.getInt(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_PREGNANT)));
        if (pregnant) {
            pregnantCheckBox.setChecked(true);
        }

        String weeksPregnantString = clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_PREGNANT_WEEKS));
        pregnantWeeksEditText.setText(weeksPregnantString);

        String lastPregnancyDateString = clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_LAST_PREGNANCY_DATE));
        if (!lastPregnancyDateString.equals("")) {
            String setText = lastPregnancyDateString;
            lastPregnancyEditText.setText(setText);
        }

        medicationsEditText.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_MEDICATIONS)));

        recentSurgeriesEditText.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_RECENT_SURGERIES)));

        healthIssuesEditText.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_HEALTH_ISSUES)));

        boolean numbness = intToBoolean(clientCursor.getInt(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_NUMBNESS)));
        numbnessCheckBox.setChecked(numbness);

        boolean swelling = intToBoolean(clientCursor.getInt(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_SWELLING)));
        swellingCheckBox.setChecked(swelling);

        boolean headaches = intToBoolean(clientCursor.getInt(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_HEADACHES)));
        headachesCheckBox.setChecked(headaches);

        emergencyContactNameEditText.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_EMERGENCY_CONTACT_NAME)));

        final String emergencyPhone = clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract
                        .ClientEntry.COLUMN_EMERGENCY_CONTACT_PHONE_NUMBER));
        emergencyContactPhoneEditText.setText(emergencyPhone);

        derogatoryNotesEditText.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_DEROGATORY_NOTES)));
    }

    private void saveChanges() {
        // Read from input fields, use trim to eliminate leading or trailing white space
        String firstNameString = firstNameEditText.getText().toString().trim();
        String lastNameString = lastNameEditText.getText().toString().trim();
        String lastServiceString = lastServiceEditText.getText().toString().trim();
        String emailString = emailEditText.getText().toString().trim();
        String phoneNumberString = phoneEditText.getText().toString().trim();
        String birthdayString = birthdayEditText.getText().toString().trim();
        String referralString = referralEditText.getText().toString().trim();
        String emergencyNameString = emergencyContactNameEditText.getText().toString().trim();
        String emergencyPhoneString = emergencyContactPhoneEditText.getText().toString().trim();
        int massagedPreviouslyInt;
        if (massagedPreviouslyCheckBox.isChecked()) {
            massagedPreviouslyInt = 1;
        } else {
            massagedPreviouslyInt = 0;
        }
        String massagedPreviouslyDateString = massagedPreviouslyDateEditText.getText().toString()
                .trim();
        int currentlyPregnantInt;
        String pregnantWeeksString = pregnantWeeksEditText.getText().toString().trim();
        if (pregnantCheckBox.isChecked() && pregnantWeeksString != "") {
            currentlyPregnantInt = 1;
        } else {
            currentlyPregnantInt = 0;
        }
        String lastPregnancyDateString = lastPregnancyEditText.getText().toString().trim();
        String medicationsString = medicationsEditText.getText().toString().trim();
        String recentSurgeriesString = recentSurgeriesEditText.getText().toString().trim();
        String healthIssuesString = healthIssuesEditText.getText().toString().trim();
        int numbnessInt;
        if (numbnessCheckBox.isChecked()) {
            numbnessInt = 1;
        } else {
            numbnessInt = 0;
        }
        int swellingInt;
        if (swellingCheckBox.isChecked()) {
            swellingInt = 1;
        } else {
            swellingInt = 0;
        }
        int headachesInt;
        if (headachesCheckBox.isChecked()) {
            headachesInt = 1;
        } else {
            headachesInt = 0;
        }
        String goalsString = goalsEditText.getText().toString().trim();
        String importantNotesString = importantNotesEditText.getText().toString().trim();
        String derogatoryNotesString = derogatoryNotesEditText.getText().toString().trim();
        String notesString = notesEditText.getText().toString().trim();

        // Create a ContentValues object where column names are the keys and the client attributes
        // are the values.
        ContentValues values = new ContentValues();
        values.put(ClientContract.ClientEntry.COLUMN_FIRST_NAME, firstNameString);
        values.put(ClientContract.ClientEntry.COLUMN_LAST_NAME, lastNameString);
        values.put(ClientContract.ClientEntry.COLUMN_LAST_SERVICE, lastServiceString);
        values.put(ClientContract.ClientEntry.COLUMN_IMPORTANT, mImportant);
        values.put(ClientContract.ClientEntry.COLUMN_IMPORTANT_NOTES, importantNotesString);
        values.put(ClientContract.ClientEntry.COLUMN_IT_WORKS, mItWorks);
        values.put(ClientContract.ClientEntry.COLUMN_GENDER, mGender);
        values.put(ClientContract.ClientEntry.COLUMN_EMAIL, emailString);
        values.put(ClientContract.ClientEntry.COLUMN_PHONE_NUMBER, phoneNumberString);
        values.put(ClientContract.ClientEntry.COLUMN_BIRTHDAY, birthdayString);
        values.put(ClientContract.ClientEntry.COLUMN_REFERRAL, referralString);
        values.put(ClientContract.ClientEntry.COLUMN_EMERGENCY_CONTACT_NAME, emergencyNameString);
        values.put(ClientContract.ClientEntry.COLUMN_EMERGENCY_CONTACT_PHONE_NUMBER,
                emergencyPhoneString);
        values.put(ClientContract.ClientEntry.COLUMN_HAD_MASSAGE_PREVIOUSLY, massagedPreviouslyInt);
        values.put(ClientContract.ClientEntry.COLUMN_HAD_MASSAGE_PREVIOUSLY_DATE,
                massagedPreviouslyDateString);
        values.put(ClientContract.ClientEntry.COLUMN_PREGNANT, currentlyPregnantInt);
        values.put(ClientContract.ClientEntry.COLUMN_PREGNANT_WEEKS, pregnantWeeksString);
        values.put(ClientContract.ClientEntry.COLUMN_LAST_PREGNANCY_DATE, lastPregnancyDateString);
        values.put(ClientContract.ClientEntry.COLUMN_MEDICATIONS, medicationsString);
        values.put(ClientContract.ClientEntry.COLUMN_GOALS, goalsString);
        values.put(ClientContract.ClientEntry.COLUMN_RECENT_SURGERIES, recentSurgeriesString);
        values.put(ClientContract.ClientEntry.COLUMN_HEALTH_ISSUES, healthIssuesString);
        values.put(ClientContract.ClientEntry.COLUMN_NUMBNESS, numbnessInt);
        values.put(ClientContract.ClientEntry.COLUMN_SWELLING, swellingInt);
        values.put(ClientContract.ClientEntry.COLUMN_HEADACHES, headachesInt);
        values.put(ClientContract.ClientEntry.COLUMN_DEROGATORY_NOTES, derogatoryNotesString);
        values.put(ClientContract.ClientEntry.COLUMN_NOTES, notesString);
        int rowsUpdated = getContentResolver().update(queryUri, values, null, null);
        if (rowsUpdated == 0) {
            // If no rows were updated, then there was an error with the update, show toast.
            Toast.makeText(EditorActivity.this, "Error with saving client", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // Otherwise the update was successful, show toast and finish activity
            Toast.makeText(EditorActivity.this, "Client updated", Toast.LENGTH_SHORT).show();
            setResult(1);
            finish();
        }
    }
}
