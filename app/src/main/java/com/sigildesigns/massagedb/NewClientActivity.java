package com.sigildesigns.massagedb;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sigildesigns.massagedb.data.ClientContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.view.View.GONE;

public class NewClientActivity extends AppCompatActivity implements View.OnClickListener {

    // UI References for datepicker
    private DatePickerDialog birthdayDatePickerDialog;
    private DatePickerDialog massagedPreviouslyDatePickerDialog;
    private DatePickerDialog lastPregnancyDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    EditText firstName;
    EditText lastName;
    RadioGroup genderGroup;
    RadioButton genderMale;
    RadioButton genderFemale;
    EditText email;
    EditText phoneNumber;
    EditText birthday;
    EditText referral;
    EditText emergencyName;
    EditText emergencyPhone;
    CheckBox massagedPreviously;
    EditText massagedPreviouslyDate;
    LinearLayout massagedPreviouslyLinearLayout;
    CheckBox pregnantCheckbox;
    EditText pregnantWeeks;
    EditText lastPregnancyDate;
    LinearLayout lastPregnancyLinearLayout;
    EditText medications;
    EditText recentSurgeries;
    EditText healthIssues;
    CheckBox numbness;
    CheckBox swelling;
    CheckBox headaches;
    EditText goals;

    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firstName = (EditText) findViewById(R.id.new_client_first_name_et);
        lastName = (EditText) findViewById(R.id.new_client_last_name_et);
        genderGroup = (RadioGroup) findViewById(R.id.new_client_radio_gender_group);
        genderMale = (RadioButton) findViewById(R.id.new_client_radio_gender_male);
        genderFemale = (RadioButton) findViewById(R.id.new_client_radio_gender_female);
        email = (EditText) findViewById(R.id.new_client_email_et);
        phoneNumber = (EditText) findViewById(R.id.new_client_phone_et);
        birthday = (EditText) findViewById(R.id.new_client_birthday_et);
        referral = (EditText) findViewById(R.id.new_client_referral_et);
        emergencyName = (EditText) findViewById(R.id.new_client_emergency_contact_name);
        emergencyPhone = (EditText) findViewById(R.id.new_client_emergency_contact_phone);
        massagedPreviously = (CheckBox) findViewById(R.id.new_client_massage_previously_checkbox);
        massagedPreviouslyDate = (EditText) findViewById(R.id.new_client_massage_previously_date);
        massagedPreviouslyLinearLayout = (LinearLayout) findViewById(R.id
                .new_client_massage_previously_linearlayout);
        pregnantCheckbox = (CheckBox) findViewById(R.id.new_client_pregnant_checkbox);
        pregnantWeeks = (EditText) findViewById(R.id.new_client_pregnant_weeks_et);
        lastPregnancyDate = (EditText) findViewById(R.id.new_client_last_pregnancy_et);
        lastPregnancyLinearLayout = (LinearLayout) findViewById(R.id
                .new_client_pregnancy_linearlayout);
        medications = (EditText) findViewById(R.id.new_client_medications_et);
        recentSurgeries = (EditText) findViewById(R.id.new_client_recent_surgeries_et);
        healthIssues = (EditText) findViewById(R.id.new_client_health_issues_et);
        numbness = (CheckBox) findViewById(R.id.new_client_numbness_checkbox);
        swelling = (CheckBox) findViewById(R.id.new_client_swelling_checkbox);
        headaches = (CheckBox) findViewById(R.id.new_client_headaches_checkbox);
        goals = (EditText) findViewById(R.id.new_client_goals_et);

        saveButton = (Button) findViewById(R.id.new_client_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewClient();
            }
        });

        dateFormatter = new SimpleDateFormat("M-dd-yyyy", Locale.US);
        birthday.setInputType(InputType.TYPE_NULL);
        massagedPreviouslyDate.setInputType(InputType.TYPE_NULL);
        lastPregnancyDate.setInputType(InputType.TYPE_NULL);

        setDateTimeField();

        genderMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView pregnantQuestion = (TextView) findViewById(R.id
                        .new_client_pregnancy_question_tv);
                pregnantQuestion.setVisibility(GONE);
                pregnantCheckbox.setVisibility(GONE);
            }
        });

        genderFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView pregnantQuestion = (TextView) findViewById(R.id
                        .new_client_pregnancy_question_tv);
                pregnantQuestion.setVisibility(View.VISIBLE);
                pregnantCheckbox.setVisibility(View.VISIBLE);
            }
        });

        massagedPreviously.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    massagedPreviouslyLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    massagedPreviouslyLinearLayout.setVisibility(GONE);
                }
            }
        });
        pregnantCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lastPregnancyLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    lastPregnancyLinearLayout.setVisibility(GONE);
                }
            }
        });
    }

    private void setDateTimeField() {
        birthday.setOnClickListener(this);
        massagedPreviouslyDate.setOnClickListener(this);
        lastPregnancyDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        birthdayDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog
                .OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                birthday.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get
                (Calendar.DAY_OF_MONTH));

        massagedPreviouslyDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog
                .OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                massagedPreviouslyDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get
                (Calendar.DAY_OF_MONTH));

        lastPregnancyDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog
                .OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                lastPregnancyDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get
                (Calendar.DAY_OF_MONTH));
    }


    private void saveNewClient() {
        // Read from input fields, use trim to eliminate leading or trailing white space
        String firstNameString = firstName.getText().toString().trim();
        String lastNameString = lastName.getText().toString().trim();
        int genderInt;
        if (genderMale.isChecked()) {
            genderInt = 1;
        } else if (genderFemale.isChecked()) {
            genderInt = 0;
        } else {
            genderInt = 0;
            Log.e("NewClientActivity", "Gender was not specified, defaulted as female");
        }
        String emailString = email.getText().toString().trim();
        String phoneNumberString = phoneNumber.getText().toString().trim();
        String birthdayString = birthday.getText().toString().trim();
        String referralString = referral.getText().toString().trim();
        String emergencyNameString = emergencyName.getText().toString().trim();
        String emergencyPhoneString = emergencyPhone.getText().toString().trim();
        int massagedPreviouslyInt;
        if (massagedPreviously.isChecked()) {
            massagedPreviouslyInt = 1;
        } else {
            massagedPreviouslyInt = 0;
        }
        String massagedPreviouslyDateString = massagedPreviouslyDate.getText().toString().trim();
        int currentlyPregnantInt;
        String pregnantWeeksString = pregnantWeeks.getText().toString().trim();
        if (pregnantCheckbox.isChecked() && pregnantWeeksString != "") {
            currentlyPregnantInt = 1;
        } else {
            currentlyPregnantInt = 0;
        }
        String lastPregnancyDateString = lastPregnancyDate.getText().toString().trim();
        String medicationsString = medications.getText().toString().trim();
        String recentSurgeriesString = recentSurgeries.getText().toString().trim();
        String healthIssuesString = healthIssues.getText().toString().trim();
        int numbnessInt;
        if (numbness.isChecked()) {
            numbnessInt = 1;
        } else {
            numbnessInt = 0;
        }
        int swellingInt;
        if (swelling.isChecked()) {
            swellingInt = 1;
        } else {
            swellingInt = 0;
        }
        int headachesInt;
        if (headaches.isChecked()) {
            headachesInt = 1;
        } else {
            headachesInt = 0;
        }
        String goalsString = goals.getText().toString().trim();
        String lastServiceString = java.text.DateFormat.getDateInstance(DateFormat.SHORT, Locale
                .US).format(new Date());
        int importantInt = 0;
        String importantNotesString = "";
        int itWorksInt = 0;
        String derogatoryNotesString = "";
        String notesString = "";

        // Create a ContentValues object where column names are the keys and the client attributes
        // are the values.
        ContentValues values = new ContentValues();
        values.put(ClientContract.ClientEntry.COLUMN_FIRST_NAME, firstNameString);
        values.put(ClientContract.ClientEntry.COLUMN_LAST_NAME, lastNameString);
        values.put(ClientContract.ClientEntry.COLUMN_LAST_SERVICE, lastServiceString);
        values.put(ClientContract.ClientEntry.COLUMN_IMPORTANT, importantInt);
        values.put(ClientContract.ClientEntry.COLUMN_IMPORTANT_NOTES, importantNotesString);
        values.put(ClientContract.ClientEntry.COLUMN_IT_WORKS, itWorksInt);
        values.put(ClientContract.ClientEntry.COLUMN_GENDER, genderInt);
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
        Uri newUri = getContentResolver().insert(ClientContract.ClientEntry.CONTENT_URI, values);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == birthday) {
            birthdayDatePickerDialog.show();
        }
        if (v == massagedPreviouslyDate) {
            massagedPreviouslyDatePickerDialog.show();
        }
        if (v == lastPregnancyDate) {
            lastPregnancyDatePickerDialog.show();
        }
    }
}
