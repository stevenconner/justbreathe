package com.sigildesigns.massagedb;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sigildesigns.massagedb.data.Client;
import com.sigildesigns.massagedb.data.ClientContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ListActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<Cursor>, ClientsAdapter.ClientsAdapterOnClickHandler {

    private ArrayList<Client> mClientList;
    private RecyclerView mRecyclerView;
    private ClientsAdapter mClientAdapter;
    private ProgressBar mLoadingIndicator;
    private int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, NewClientActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_client_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mClientAdapter = new ClientsAdapter(this, null, this);
        mRecyclerView.setAdapter(mClientAdapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                ClientContract.ClientEntry._ID,
                ClientContract.ClientEntry.COLUMN_FIRST_NAME,
                ClientContract.ClientEntry.COLUMN_LAST_NAME,
                ClientContract.ClientEntry.COLUMN_LAST_SERVICE,
                ClientContract.ClientEntry.COLUMN_IMPORTANT,
                ClientContract.ClientEntry.COLUMN_IT_WORKS,
                ClientContract.ClientEntry.COLUMN_GENDER};
        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,                       // Parent activity context
                ClientContract.ClientEntry.CONTENT_URI,     // Provider content URI to query
                projection,                          // Columns to include in the resulting Cursor
                null,                                       // No selection clause
                null,                                       // No selection argument
                ClientContract.ClientEntry.COLUMN_FIRST_NAME + " ASC");     // Sort by first name
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mClientAdapter.swapCursor(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // Helper method to delete all data from the database for testing purposes.
    private void deleteAllData() {
        int rowsDeleted = getContentResolver().delete(ClientContract.ClientEntry.CONTENT_URI,
                null, null);
        Log.v("ListActivity", rowsDeleted + " rows deleted from client database");
    }

    //  Helper method to add dummy data to the database to verify if things are working.
    private void addDummyData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M-dd-yyyy", Locale.US);
        Calendar newDate = Calendar.getInstance();
        String lastServiceTest = dateFormat.format(newDate.getTime());
        // Store dummy data into a new contentvalues variable.
        ContentValues values = new ContentValues();
        values.put(ClientContract.ClientEntry.COLUMN_FIRST_NAME, "Namey");
        values.put(ClientContract.ClientEntry.COLUMN_LAST_NAME, "Fakeyson");
        values.put(ClientContract.ClientEntry.COLUMN_LAST_SERVICE, lastServiceTest);
        values.put(ClientContract.ClientEntry.COLUMN_IMPORTANT, 1);
        values.put(ClientContract.ClientEntry.COLUMN_IMPORTANT_NOTES, "This is an important note");
        values.put(ClientContract.ClientEntry.COLUMN_IT_WORKS, 0);
        values.put(ClientContract.ClientEntry.COLUMN_GENDER, 0);
        values.put(ClientContract.ClientEntry.COLUMN_EMAIL, "nameyfakeyson@fakemail.com");
        values.put(ClientContract.ClientEntry.COLUMN_PHONE_NUMBER, "(209) 648-4183");
        values.put(ClientContract.ClientEntry.COLUMN_BIRTHDAY, "01-11-1960");
        values.put(ClientContract.ClientEntry.COLUMN_REFERRAL, "Name Fakerton");
        values.put(ClientContract.ClientEntry.COLUMN_EMERGENCY_CONTACT_NAME, "Name Fakerton");
        values.put(ClientContract.ClientEntry.COLUMN_EMERGENCY_CONTACT_PHONE_NUMBER, "(209) " +
                "648-4183");
        values.put(ClientContract.ClientEntry.COLUMN_HAD_MASSAGE_PREVIOUSLY, 1);
        values.put(ClientContract.ClientEntry.COLUMN_HAD_MASSAGE_PREVIOUSLY_DATE, "01-12-2016");
        values.put(ClientContract.ClientEntry.COLUMN_PREGNANT, 1);
        values.put(ClientContract.ClientEntry.COLUMN_PREGNANT_WEEKS, "20");
        values.put(ClientContract.ClientEntry.COLUMN_LAST_PREGNANCY_DATE, "01-12-2016");
        values.put(ClientContract.ClientEntry.COLUMN_MEDICATIONS, "This is the medications");
        values.put(ClientContract.ClientEntry.COLUMN_GOALS, "These are the goals");
        values.put(ClientContract.ClientEntry.COLUMN_RECENT_SURGERIES, "These are the recent " +
                "surgeries");
        values.put(ClientContract.ClientEntry.COLUMN_HEALTH_ISSUES, "These are the health issues");
        values.put(ClientContract.ClientEntry.COLUMN_NUMBNESS, 1);
        values.put(ClientContract.ClientEntry.COLUMN_SWELLING, 1);
        values.put(ClientContract.ClientEntry.COLUMN_HEADACHES, 1);
        values.put(ClientContract.ClientEntry.COLUMN_DEROGATORY_NOTES, "This is the derogatory " +
                "notes section");
        values.put(ClientContract.ClientEntry.COLUMN_NOTES, "This is the notes section");

        // Insert a new item into the provider, returning the content URI for the new item.
        Uri newUri = getContentResolver().insert(ClientContract.ClientEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not inserting was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with the insertion.
            Toast.makeText(this, R.string.toast_error_saving, Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display toast reflecting.
            Toast.makeText(this, R.string.toast_insertion_success, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mClientAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String clientID) {
        displayPasswordDialog(clientID);
    }

    // Helper method to display a password input
    public void displayPasswordDialog(final String clientID) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setMessage("Please enter the password to access the client");
        alert.setTitle("Enter Password");
        alert.setView(edittext);

        alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editTextValue = edittext.getText().toString().trim();
                int inputPassword = Integer.valueOf(editTextValue);
                if (inputPassword == 8724) {
                    Intent intent = new Intent(ListActivity.this, DetailsActivity.class);
                    intent.putExtra("clientID", clientID);
                    startActivity(intent);
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Password incorrect, access denied.";
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    toast.show();
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
}












