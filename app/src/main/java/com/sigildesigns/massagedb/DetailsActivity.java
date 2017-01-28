package com.sigildesigns.massagedb;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sigildesigns.massagedb.data.ClientContract;

import static android.view.View.GONE;

public class DetailsActivity extends AppCompatActivity {

    String clientIDString;
    ImageView itWorksImageView;
    ImageView genderImageView;
    ImageView importantImageView;
    TextView nameTextView;
    TextView lastServiceTextView;
    TextView notesTextView;
    LinearLayout importantNotesLinearLayout;
    TextView importantNotesTextView;
    TextView emailTextView;
    TextView phoneTextView;
    TextView birthdayTextView;
    TextView goalsTextView;
    TextView referralTextView;
    CheckBox massagedPreviouslyCheckBox;
    TextView massagedPreviouslyDateTextView;
    CheckBox pregnantCheckBox;
    TextView pregnantWeeksTextView;
    TextView lastPregnancyDateTextView;
    TextView medicationsTextView;
    TextView surgeriesTextView;
    TextView healthIssuesTextView;
    CheckBox numbnessCheckBox;
    CheckBox swellingCheckBox;
    CheckBox headachesCheckBox;
    TextView emergencyContactNameTextView;
    TextView emergencyContactPhoneTextView;
    TextView derogatoryNotesTextView;
    Uri queryUri;
    MenuItem itWorksTrue;
    MenuItem itWorksFalse;
    MenuItem importantTrue;
    MenuItem importantFalse;


    Cursor clientCursor;
    Long clientIDLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Bind variables to views
        itWorksImageView = (ImageView) findViewById(R.id.details_it_works_iv);
        genderImageView = (ImageView) findViewById(R.id.details_gender_iv);
        importantImageView = (ImageView) findViewById(R.id.details_important_iv);
        nameTextView = (TextView) findViewById(R.id.details_name_tv);
        lastServiceTextView = (TextView) findViewById(R.id.details_last_service_tv);
        notesTextView = (TextView) findViewById(R.id.details_notes_tv);
        importantNotesLinearLayout = (LinearLayout) findViewById(R.id.details_important_notes_ll);
        importantNotesTextView = (TextView) findViewById(R.id.details_important_notes_tv);
        emailTextView = (TextView) findViewById(R.id.details_email_tv);
        phoneTextView = (TextView) findViewById(R.id.details_phone_tv);
        birthdayTextView = (TextView) findViewById(R.id.details_birthday_tv);
        goalsTextView = (TextView) findViewById(R.id.details_goals_tv);
        referralTextView = (TextView) findViewById(R.id.details_referral_tv);
        massagedPreviouslyCheckBox = (CheckBox) findViewById(R.id
                .details_massaged_previously_checkbox);
        massagedPreviouslyDateTextView = (TextView) findViewById(R.id
                .details_massaged_previously_date_tv);
        pregnantCheckBox = (CheckBox) findViewById(R.id.details_pregnant_checkbox);
        pregnantWeeksTextView = (TextView) findViewById(R.id.details_pregnant_weeks_tv);
        lastPregnancyDateTextView = (TextView) findViewById(R.id.details_last_pregnancy_date_tv);
        medicationsTextView = (TextView) findViewById(R.id.details_medications_tv);
        surgeriesTextView = (TextView) findViewById(R.id.details_recent_surgeries_tv);
        healthIssuesTextView = (TextView) findViewById(R.id.details_health_issues_tv);
        numbnessCheckBox = (CheckBox) findViewById(R.id.details_numbness_checkbox);
        swellingCheckBox = (CheckBox) findViewById(R.id.details_swelling_checkbox);
        headachesCheckBox = (CheckBox) findViewById(R.id.details_headaches_checkbox);
        emergencyContactNameTextView = (TextView) findViewById(R.id
                .details_emergency_contact_name_tv);
        emergencyContactPhoneTextView = (TextView) findViewById(R.id
                .details_emergency_contact_phone_tv);
        derogatoryNotesTextView = (TextView) findViewById(R.id.details_derogatory_notes_tv);

        // Get the intent, pull the clientID from it, and query the database for that client.
        Intent intent = getIntent();
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
        ;
        if (itWorks) {
            itWorksImageView.setVisibility(View.VISIBLE);
        } else {
            itWorksImageView.setVisibility(View.INVISIBLE);
        }

        boolean gender = intToBoolean(clientCursor.getInt(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_GENDER)));
        if (gender) {
            genderImageView.setImageResource(R.drawable.male);
            pregnantCheckBox.setVisibility(GONE);
            pregnantWeeksTextView.setVisibility(GONE);
            lastPregnancyDateTextView.setVisibility(GONE);
        } else {
            genderImageView.setImageResource(R.drawable.female);
            pregnantCheckBox.setVisibility(View.VISIBLE);
        }

        boolean important = intToBoolean(clientCursor.getInt(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_IMPORTANT)));
        if (important) {
            importantImageView.setVisibility(View.VISIBLE);
            importantNotesLinearLayout.setVisibility(View.VISIBLE);
        } else {
            importantImageView.setVisibility(View.INVISIBLE);
        }
        String name = clientCursor.getString(clientCursor.getColumnIndex(ClientContract
                .ClientEntry.COLUMN_FIRST_NAME)) + " " + clientCursor.getString(clientCursor
                .getColumnIndex(ClientContract.ClientEntry.COLUMN_LAST_NAME));
        nameTextView.setText(name);

        lastServiceTextView.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract
                        .ClientEntry.COLUMN_LAST_SERVICE)));

        notesTextView.setText(clientCursor.getString(clientCursor.getColumnIndex(ClientContract
                .ClientEntry.COLUMN_NOTES)));

        importantNotesTextView.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_IMPORTANT_NOTES)));

        final String email = clientCursor.getString(clientCursor.getColumnIndex(ClientContract
                .ClientEntry.COLUMN_EMAIL));
        emailTextView.setText(email);
        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email,
                        null));
                startActivity(Intent.createChooser(intent, "Send email..."));
            }
        });

        final String phoneNumber = clientCursor.getString(clientCursor.getColumnIndex(ClientContract
                .ClientEntry.COLUMN_PHONE_NUMBER));
        phoneTextView.setText(phoneNumber);
        phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });

        birthdayTextView.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_BIRTHDAY)));

        goalsTextView.setText(clientCursor.getString(clientCursor.getColumnIndex(ClientContract
                .ClientEntry.COLUMN_GOALS)));

        referralTextView.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_REFERRAL)));

        boolean massagedPreviously = intToBoolean(clientCursor.getInt(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_HAD_MASSAGE_PREVIOUSLY)));
        if (massagedPreviously) {
            massagedPreviouslyCheckBox.setChecked(true);
            massagedPreviouslyDateTextView.setVisibility(View.VISIBLE);
        }

        String massagedPreviouslyDate = getString(R.string.when) + clientCursor.getString
                (clientCursor.getColumnIndex
                        (ClientContract.ClientEntry.COLUMN_HAD_MASSAGE_PREVIOUSLY_DATE));
        massagedPreviouslyDateTextView.setText(massagedPreviouslyDate);

        boolean pregnant = intToBoolean(clientCursor.getInt(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_PREGNANT)));
        if (pregnant) {
            pregnantCheckBox.setVisibility(View.VISIBLE);
            pregnantWeeksTextView.setVisibility(View.VISIBLE);
            pregnantCheckBox.setChecked(true);
        }

        String weeksPregnantString = getString(R.string.weeks_pregnant) + clientCursor.getString
                (clientCursor.getColumnIndex
                        (ClientContract.ClientEntry.COLUMN_PREGNANT_WEEKS));
        pregnantWeeksTextView.setText(weeksPregnantString);

        String lastPregnancyDateString = clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_LAST_PREGNANCY_DATE));
        if (!lastPregnancyDateString.equals("")) {
            String setText = getString(R.string.last_pregnancy) + lastPregnancyDateString;
            lastPregnancyDateTextView.setText(setText);
            lastPregnancyDateTextView.setVisibility(View.VISIBLE);
        }

        medicationsTextView.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_MEDICATIONS)));

        surgeriesTextView.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_RECENT_SURGERIES)));

        healthIssuesTextView.setText(clientCursor.getString(clientCursor.getColumnIndex
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

        emergencyContactNameTextView.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_EMERGENCY_CONTACT_NAME)));

        final String emergencyPhone = clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract
                        .ClientEntry.COLUMN_EMERGENCY_CONTACT_PHONE_NUMBER));
        emergencyContactPhoneTextView.setText(emergencyPhone);
        emergencyContactPhoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + emergencyPhone));
                startActivity(intent);
            }
        });

        derogatoryNotesTextView.setText(clientCursor.getString(clientCursor.getColumnIndex
                (ClientContract.ClientEntry.COLUMN_DEROGATORY_NOTES)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);

        // Get references to menu items to show/hide them
        itWorksTrue = menu.findItem(R.id.details_action_make_itworks_true);
        itWorksFalse = menu.findItem(R.id.details_action_make_itworks_false);
        importantTrue = menu.findItem(R.id.details_action_make_important_true);
        importantFalse = menu.findItem(R.id.details_action_make_important_false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        int rowsUpdated;
        ContentValues values;
        switch (id) {
            case R.id.details_action_new_visit:
                Intent intent = new Intent(DetailsActivity.this, NewVisitActivity.class);
                intent.putExtra("clientID", clientIDString);
                startActivity(intent);
                return true;
            case R.id.details_action_make_itworks_true:
                itWorksImageView.setVisibility(View.VISIBLE);
                // Create a new ContentValues object to update the itworks flag
                values = new ContentValues();
                values.put(ClientContract.ClientEntry.COLUMN_IT_WORKS, 1);
                rowsUpdated = getContentResolver().update(queryUri, values, null, null);
                if (rowsUpdated == 0) {
                    // If no rows were updated, then there was an error with the update, show toast.
                    Toast.makeText(this, "Error with saving client", Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise the update was successful, show toast.
                    Toast.makeText(this, "Client updated", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.details_action_make_itworks_false:
                itWorksImageView.setVisibility(View.INVISIBLE);
                // Create a new ContentValues object to update the itworks flag
                values = new ContentValues();
                values.put(ClientContract.ClientEntry.COLUMN_IT_WORKS, 0);
                rowsUpdated = getContentResolver().update(queryUri, values, null, null);
                if (rowsUpdated == 0) {
                    // If no rows were updated, then there was an error with the update, show toast.
                    Toast.makeText(this, "Error with saving client", Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise the update was successful, show toast.
                    Toast.makeText(this, "Client updated", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.details_action_make_important_true:
                importantImageView.setVisibility(View.VISIBLE);
                importantNotesLinearLayout.setVisibility(View.VISIBLE);
                // Create a new ContentValues object to update the important flag
                values = new ContentValues();
                values.put(ClientContract.ClientEntry.COLUMN_IMPORTANT, 1);
                rowsUpdated = getContentResolver().update(queryUri, values, null, null);
                if (rowsUpdated == 0) {
                    // If no rows were updated, then there was an error with the update, show toast.
                    Toast.makeText(this, "Error with saving client", Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise the update was successful, show toast.
                    Toast.makeText(this, "Client updated", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.details_action_make_important_false:
                importantImageView.setVisibility(View.INVISIBLE);
                importantNotesLinearLayout.setVisibility(GONE);
                // Create a new ContentValues object to update the important flag
                values = new ContentValues();
                values.put(ClientContract.ClientEntry.COLUMN_IMPORTANT, 0);
                rowsUpdated = getContentResolver().update(queryUri, values, null, null);
                if (rowsUpdated == 0) {
                    // If no rows were updated, then there was an error with the update, show toast.
                    Toast.makeText(this, "Error with saving client", Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise the update was successful, show toast.
                    Toast.makeText(this, "Client updated", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.details_action_edit_notes:
                displayNotesDialog();
                return true;
            case R.id.details_action_edit_important_notes:
                displayImportantNotesDialog();
                return true;
            case R.id.details_action_edit_derogatory_notes:
                displayDerogatoryNotesDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayImportantNotesDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        alert.setMessage("Please enter the important notes you would like to save.");
        alert.setTitle("Enter Important Notes");
        alert.setView(edittext);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Extract what the user typed and set to the view
                String newImportantNotes = edittext.getText().toString().trim();
                importantNotesTextView.setText(newImportantNotes);

                // Save what the user typed into the database.
                ContentValues values = new ContentValues();
                values.put(ClientContract.ClientEntry.COLUMN_IMPORTANT_NOTES, newImportantNotes);
                int rowsUpdated = getContentResolver().update(queryUri, values, null, null);
                if (rowsUpdated == 0) {
                    // If no rows were updated, then there was an error with the update, show toast.
                    Toast.makeText(DetailsActivity.this, "Error with saving client", Toast
                            .LENGTH_SHORT).show();
                } else {
                    // Otherwise the update was successful, show toast.
                    Toast.makeText(DetailsActivity.this, "Client updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DetailsActivity.this, "Important Notes entry canceled.", Toast
                        .LENGTH_SHORT).show();
            }
        });
        alert.show();
    }

    public void displayNotesDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        alert.setMessage("Please enter the notes you would like to save.");
        alert.setTitle("Enter Notes");
        alert.setView(edittext);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Extract what the user typed and set to the view
                String newNotes = edittext.getText().toString().trim();
                notesTextView.setText(newNotes);

                // Save what the user typed into the database.
                ContentValues values = new ContentValues();
                values.put(ClientContract.ClientEntry.COLUMN_NOTES, newNotes);
                int rowsUpdated = getContentResolver().update(queryUri, values, null, null);
                if (rowsUpdated == 0) {
                    // If no rows were updated, then there was an error with the update, show toast.
                    Toast.makeText(DetailsActivity.this, "Error with saving client", Toast
                            .LENGTH_SHORT).show();
                } else {
                    // Otherwise the update was successful, show toast.
                    Toast.makeText(DetailsActivity.this, "Client updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DetailsActivity.this, "Notes entry canceled.", Toast
                        .LENGTH_SHORT).show();
            }
        });
        alert.show();
    }

    public void displayDerogatoryNotesDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        alert.setMessage("Please enter the derogatory notes you would like to save.");
        alert.setTitle("Enter Derogatory Notes");
        alert.setView(edittext);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Extract what the user typed and set to the view
                String newDerogatoryNotes = edittext.getText().toString().trim();
                derogatoryNotesTextView.setText(newDerogatoryNotes);

                // Save what the user typed into the database.
                ContentValues values = new ContentValues();
                values.put(ClientContract.ClientEntry.COLUMN_DEROGATORY_NOTES, newDerogatoryNotes);
                int rowsUpdated = getContentResolver().update(queryUri, values, null, null);
                if (rowsUpdated == 0) {
                    // If no rows were updated, then there was an error with the update, show toast.
                    Toast.makeText(DetailsActivity.this, "Error with saving client", Toast
                            .LENGTH_SHORT).show();
                } else {
                    // Otherwise the update was successful, show toast.
                    Toast.makeText(DetailsActivity.this, "Client updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DetailsActivity.this, "Derogatory Notes entry canceled.", Toast
                        .LENGTH_SHORT).show();
            }
        });
        alert.show();
    }

    public boolean intToBoolean(int input) {
        if (input == 1) {
            return true;
        } else {
            return false;
        }
    }
}
