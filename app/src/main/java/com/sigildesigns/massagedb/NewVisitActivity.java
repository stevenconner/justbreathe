package com.sigildesigns.massagedb;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sigildesigns.massagedb.data.ClientContract;
import com.squareup.sdk.register.ChargeRequest;
import com.squareup.sdk.register.RegisterClient;
import com.squareup.sdk.register.RegisterSdk;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.squareup.sdk.register.CurrencyCode.USD;

public class NewVisitActivity extends AppCompatActivity {

    Cursor clientCursor;
    String emailString;
    String dateStamp;
    String clientName;
    Uri queryUri;

    TextView nameTextView;
    TextView dateTextView;
    EditText minutesEditText;
    EditText priceEditText;
    CheckBox cashCheckBox;
    CheckBox creditCheckBox;
    CheckBox checkCheckBox;
    CheckBox certCheckBox;
    CheckBox appPaymentCheckBox;
    EditText mileageEditText;
    EditText tipEditText;
    ImageView squareImageView;
    Button sendButton;
    private RegisterClient registerClient;

    private static final String SQUARE_API_KEY = BuildConfig.SQUARE_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String clientID = intent.getStringExtra("clientID");
        long clientIDLong = Long.valueOf(clientID);
        queryUri = ContentUris.withAppendedId(ClientContract.ClientEntry.CONTENT_URI, clientIDLong);
        clientCursor = getContentResolver().query(queryUri, null, null, null, null);
        clientCursor.moveToFirst();

        SimpleDateFormat dateFormat = new SimpleDateFormat("M-dd-yyyy", Locale.US);
        Calendar newDate = Calendar.getInstance();
        dateStamp = dateFormat.format(newDate.getTime());
        dateTextView = (TextView) findViewById(R.id.newvisit_date_tv);
        dateTextView.setText(dateStamp);


        clientName = clientCursor.getString(clientCursor.getColumnIndex(ClientContract
                .ClientEntry.COLUMN_FIRST_NAME)) + " " + clientCursor.getString(clientCursor
                .getColumnIndex(ClientContract.ClientEntry.COLUMN_LAST_NAME));
        nameTextView = (TextView) findViewById(R.id.newvisit_name_tv);
        nameTextView.setText(clientName);

        minutesEditText = (EditText) findViewById(R.id.newvisit_minutes_et);

        priceEditText = (EditText) findViewById(R.id.newvisit_price_et);

        cashCheckBox = (CheckBox) findViewById(R.id.newvisit_cash_cb);
        creditCheckBox = (CheckBox) findViewById(R.id.newvisit_credit_cb);
        checkCheckBox = (CheckBox) findViewById(R.id.newvisit_check_cb);
        certCheckBox = (CheckBox) findViewById(R.id.newvisit_cert_cb);
        appPaymentCheckBox = (CheckBox) findViewById(R.id.newvisit_app_payment_cb);
        mileageEditText = (EditText) findViewById(R.id.newvisit_mileage_et);
        tipEditText = (EditText) findViewById(R.id.newvisit_tip_et);
        squareImageView = (ImageView) findViewById(R.id.newvisit_square_iv);
        squareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySquarePaymentDialog();
            }
        });
        sendButton = (Button) findViewById(R.id.newvisit_send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendButtonAction();
            }
        });

        registerClient = RegisterSdk.createClient(this, SQUARE_API_KEY);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_visit, menu);
        return true;
    }

    // This method parses the checkboxes for their values and creates a string for use in the email
    private String parseCheckBoxes() {
        String returnString = "";
        if (cashCheckBox.isChecked()) {
            returnString = "cash";
        }
        if (creditCheckBox.isChecked()) {
            if (returnString.equals("")) {
                returnString = "credit";
            } else {
                returnString = returnString + " and credit";
            }
        }
        if (checkCheckBox.isChecked()) {
            if (returnString.equals("")) {
                returnString = "check";
            } else {
                returnString = returnString + " and check";
            }
        }
        if (appPaymentCheckBox.isChecked()) {
            if (returnString.equals("")) {
                returnString = "app payment";
            } else {
                returnString = returnString + " and app payment";
            }
        }
        if (certCheckBox.isChecked()) {
            if (returnString.equals("")) {
                returnString = "cert";
            } else {
                returnString = returnString + " and cert";
            }
        }
        return returnString;
    }

    // This method activates when button is clicked.
    private void sendButtonAction() {
        // Create intent to send email.
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "evlpacman@gmail" +
                ".com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Breathe - New Client Visit");

        // Get the values from views
        String minutes = minutesEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String paymentType = parseCheckBoxes();
        String mileage = mileageEditText.getText().toString().trim();
        String tip = tipEditText.getText().toString().trim();

        emailString = "Date: " + dateStamp + "\n"
                + "Name: " + clientName + "\n"
                + "Service Type: " + minutes + " minutes\n"
                + "Price: " + price + "\n"
                + "Payment Type: " + paymentType + "\n"
                + "Mileage: " + mileage + "\n"
                + "Tip: " + tip;

        // Set the body of the email to the String we built
        intent.putExtra(Intent.EXTRA_TEXT, emailString);

        // Create a new ContentValues object to update the last service date
        ContentValues values = new ContentValues();
        values.put(ClientContract.ClientEntry.COLUMN_LAST_SERVICE, dateStamp);
        int rowsUpdated = getContentResolver().update(queryUri, values, null, null);
        if (rowsUpdated == 0) {
            // If no rows were updated, then there was an error with the update.
            Toast.makeText(this, "Error with saving item", Toast.LENGTH_SHORT).show();
        } else {
            Log.v("NewVisitActivity", "Client Last Service Date Updated to: " + dateStamp);
        }
        // Fire off the email intent
        startActivity(Intent.createChooser(intent, "Send email..."));
    }

    // Helper method to display a dialog
    private void showDialog(String title, String message, DialogInterface.OnClickListener
            listener) {
        Log.d("NewVisitActivity", title + " " + message);
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton
                (android.R.string.ok, listener).show();
    }

    public static final int CHARGE_REQUEST_CODE = 1;

    // Helper method to display a password input
    public void displaySquarePaymentDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setMessage("Please enter the amount you are charging the client.");
        alert.setTitle("Enter Charge Amount");
        alert.setView(edittext);

        alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editTextValue = edittext.getText().toString().trim();
                editTextValue = editTextValue + "00";
                int chargeAmount = Integer.valueOf(editTextValue);

                ChargeRequest request = new ChargeRequest.Builder(chargeAmount, USD).build();
                try {
                    Intent intent = registerClient.createChargeIntent(request);
                    startActivityForResult(intent, CHARGE_REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    showDialog("Error", "Square Register is not installed", null);
                    registerClient.openRegisterPlayStoreListing();
                }

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHARGE_REQUEST_CODE) {
            if (data == null) {
                showDialog("Error", "Square Register was uninstalled or crashed", null);
                return;
            }
            if (resultCode == Activity.RESULT_OK) {
                ChargeRequest.Success success = registerClient.parseChargeSuccess(data);
                String message = "Client transaction id: " + success.clientTransactionId;
                showDialog("Success!", message, null);
            } else {
                ChargeRequest.Error error = registerClient.parseChargeError(data);
                if (error.code == ChargeRequest.ErrorCode.TRANSACTION_ALREADY_IN_PROGRESS) {
                    String title = "A transaction is already in progress";
                    String message = "Please complete the current transaction in Register.";

                    showDialog(title, message, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Some errors can only be fixed by launching Register
                            // from the Home screen.
                            registerClient.launchRegister();
                        }
                    });
                } else {
                    showDialog("Error: " + error.code, error.debugDescription, null);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clientCursor.close();
    }
}
