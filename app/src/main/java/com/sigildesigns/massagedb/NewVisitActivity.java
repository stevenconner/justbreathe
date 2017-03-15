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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sigildesigns.massagedb.data.ClientContract;
import com.squareup.sdk.register.ChargeRequest;
import com.squareup.sdk.register.RegisterClient;
import com.squareup.sdk.register.RegisterSdk;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
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
    Button paymentTypeButton;
    TextView paymentTypeTextView;
    CheckBox cashCheckBox;
    CheckBox creditCheckBox;
    CheckBox checkCheckBox;
    CheckBox certCheckBox;
    CheckBox appPaymentCheckBox;
    EditText mileageEditText;
    EditText tipEditText;
    Button certButton;
    ImageView squareImageView;
    Button sendButton;
    LinearLayout rootLayout;
    private RegisterClient registerClient;

    private static final String SQUARE_API_KEY = BuildConfig.SQUARE_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

        paymentTypeButton = (Button) findViewById(R.id.newvisit_payment_types_button);
        paymentTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentMethodDialog();
            }
        });

        priceEditText = (EditText) findViewById(R.id.newvisit_price_et);

        paymentTypeTextView = (TextView) findViewById(R.id.newvisit_payment_type_tv);
        mileageEditText = (EditText) findViewById(R.id.newvisit_mileage_et);
        tipEditText = (EditText) findViewById(R.id.newvisit_tip_et);
        certButton = (Button) findViewById(R.id.newvisit_sell_cert_button);
        certButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayGiftCertificateDialog();
            }
        });
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
        rootLayout = (LinearLayout) findViewById(R.id.newvisit_root_layout);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_visit, menu);
        return true;
    }

    // This method activates when the user sells a gift certificate
    private void sendGiftCertSold(int value, int price) {
        String valueString = String.valueOf(value);
        String priceString = String.valueOf(price);

        // Create intent to send email.
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "evlpacman@gmail" +
                ".com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Breathe - Gift Certificate Sold");

        String giftCertEmail = "Date: " + dateStamp + "\n"
                + "Name: " + clientName + "\n"
                + "Value: " + valueString + "\n"
                + "Price: " + priceString ;

        intent.putExtra(Intent.EXTRA_TEXT, giftCertEmail);
        startActivity(Intent.createChooser(intent, "Send email..."));
    }

    // This method shows a dialog to select payment types
    private void showPaymentMethodDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // String array for alert dialog multiple choice items
        String[] paymentTypes = new String[]{
                "Cash",
                "Credit",
                "Check",
                "App Payment",
                "Certificate"
        };

        // Boolean array for initial selected items
        final boolean[] checkedTypes = new boolean[]{
                false,
                false,
                false,
                false,
                false
        };

        // Convert the types array to a list
        final List<String> paymentTypesList = Arrays.asList(paymentTypes);

        builder.setTitle("Payment Method");
        builder.setMultiChoiceItems(paymentTypes, checkedTypes,
                new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // Update the current focused item's checked status
                checkedTypes[which] = isChecked;

                // Get the current focused item
                String currentItem = paymentTypesList.get(which);
            }
        });

        // Specify the dialog is not cancelable
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                paymentTypeTextView.setText("Payment Type(s):\n");
                for (int i = 0; i < checkedTypes.length; i++){
                    boolean checked = checkedTypes[i];
                    if (checked) {
                        paymentTypeTextView.setText(paymentTypeTextView.getText() +
                                paymentTypesList.get(i) + "\n");
                    }
                }
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
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
        String mileage = mileageEditText.getText().toString().trim();
        String tip = tipEditText.getText().toString().trim();
        String paymentType = paymentTypeTextView.getText().toString().trim();

        emailString = "Date: " + dateStamp + "\n"
                + "Name: " + clientName + "\n"
                + "Service Type: " + minutes + " minutes\n"
                + "Price: " + price + "\n\n"
                + paymentType + "\n\n"
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



    // Displays a dialog for the user to send a gift certificate sold email.
    public void displayGiftCertificateDialog() {
        LayoutInflater layoutInflater = getLayoutInflater();
        final View inflater = layoutInflater.inflate(R.layout.gift_cert_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Sell Gift Certificate");
        builder.setView(inflater);

        final EditText value = (EditText) inflater.findViewById(R.id.cert_dialog_value_et);
        final EditText price = (EditText) inflater.findViewById(R.id.cert_dialog_charged_et);

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String valueString = value.getText().toString().trim();
                        String priceString = price.getText().toString().trim();
                        sendGiftCertSold(Integer.valueOf(valueString), Integer.valueOf(priceString));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(NewVisitActivity.this, "Gift Certificate Sale Canceled", Toast
                                .LENGTH_SHORT).show();
                    }
                });
        builder.show();
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

    // Helper method to display a dialog
    private void showDialog(String title, String message, DialogInterface.OnClickListener
            listener) {
        Log.d("NewVisitActivity", title + " " + message);
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton
                (android.R.string.ok, listener).show();
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
