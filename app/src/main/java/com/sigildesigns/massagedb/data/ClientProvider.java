package com.sigildesigns.massagedb.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * {@link android.content.ContentProvider} for MassageDB app.
 */

public class ClientProvider extends ContentProvider {
    // Constant values for a URI matcher to determine which operation to carry out on a line of
    // code.
    private static final int CLIENTS = 100;
    private static final int CLIENT_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ClientContract.CONTENT_AUTHORITY, ClientContract.PATH_CLIENTS, CLIENTS);
        sUriMatcher.addURI(ClientContract.CONTENT_AUTHORITY, ClientContract.PATH_CLIENTS + "/#",
                CLIENT_ID);
    }

    // Tag for the log messages
    public static final String LOG_TAG = ClientProvider.class.getSimpleName();

    // Variable for the ClientDbHelper object.
    private ClientDbHelper mDbHelper;

    // Initialize the provider and the database helper object.
    @Override
    public boolean onCreate() {
        // Create the ClientDbHelper object to interact with the database.
        mDbHelper = new ClientDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query.
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case CLIENTS:
                /**
                 * For the CLIENTS code, query the clients table directly with the given projection,
                 * selection, selection arguments, and sort order. The cursor could contain multiple
                 * rows of the items table.
                 */
                cursor = database.query(ClientContract.ClientEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case CLIENT_ID:
                /**
                 * For the CLIENT_ID code, extract out the ID from the URI. For example, URI such as
                 * "content://com.sigildesigns.massagedb/clients/3", the selection will be "_id=?"
                 * and the selection argument will be a String array containing the actual ID of 3
                 * in this case.
                 * For every "?" in the selection, we need to have an element in the selection
                 * arguments that will fill in the "?". Since we have 1 question mark in the
                 * selection, we have 1 String in the selection arguments' String array.
                 */
                selection = ClientContract.ClientEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the items table where the _ID equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(ClientContract.ClientEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        // Set notification URI on the Cursor, so we know what content URI the Cursor was created
        // for. If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    // Returns the MIME type of data for the content URI.
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CLIENTS:
                return ClientContract.ClientEntry.CONTENT_LIST_TYPE;
            case CLIENT_ID:
                return ClientContract.ClientEntry.CONTENT_CLIENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CLIENTS:
                return insertClient(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    // Helper method to insert a client into the database with the given ContentValues. Returns the
    // new content URI for that specific row in the database.
    private Uri insertClient(Uri uri, ContentValues values) {
        // Check that the required values are not null
        String firstName = values.getAsString(ClientContract.ClientEntry.COLUMN_FIRST_NAME);
        if (firstName == null) {
            throw new IllegalArgumentException("Client requires a first name");
        }
        String lastName = values.getAsString(ClientContract.ClientEntry.COLUMN_LAST_NAME);
        if (lastName == null) {
            throw new IllegalArgumentException("Client requires a last name");
        }
        String lastService = values.getAsString(ClientContract.ClientEntry.COLUMN_LAST_SERVICE);
        if (lastService == null) {
            throw new IllegalArgumentException("Client requires a last service date (may be " +
                    "today)");
        }
        Integer important = values.getAsInteger(ClientContract.ClientEntry.COLUMN_IMPORTANT);
        if (important == null) {
            throw new IllegalArgumentException("Client requires a value for the Important flag");
        }
        Integer itWorks = values.getAsInteger(ClientContract.ClientEntry.COLUMN_IT_WORKS);
        if (itWorks == null) {
            throw new IllegalArgumentException("Client requires a value for the It Works flag");
        }
        Integer massagePreviously = values.getAsInteger(ClientContract.ClientEntry
                .COLUMN_HAD_MASSAGE_PREVIOUSLY);
        if (massagePreviously == null) {
            throw new IllegalArgumentException("Client requires a value for whether or not " +
                    "they have been massaged previously.");
        }
        Integer pregnant = values.getAsInteger(ClientContract.ClientEntry.COLUMN_PREGNANT);
        if (pregnant == null) {
            throw new IllegalArgumentException("Client requires a value for the pregnant flag");
        }
        Integer numbness = values.getAsInteger(ClientContract.ClientEntry.COLUMN_NUMBNESS);
        if (numbness == null) {
            throw new IllegalArgumentException("Client requires a value for the numbness flag");
        }
        Integer swelling = values.getAsInteger(ClientContract.ClientEntry.COLUMN_SWELLING);
        if (swelling == null) {
            throw new IllegalArgumentException("Client requires a value for the swelling flag");
        }
        Integer headaches = values.getAsInteger(ClientContract.ClientEntry.COLUMN_HEADACHES);
        if (headaches == null) {
            throw new IllegalArgumentException("Client requires a value for the headaches flag");
        }

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new client with the given values
        long id = database.insert(ClientContract.ClientEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        // Notify all listeners that the data has changed for the client content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID of the newly inserted row appended at the end.
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted = 0;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CLIENTS:
                rowsDeleted = database.delete(ClientContract.ClientEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case CLIENT_ID:
                // For the CLIENT_ID code, extract out the ID from the URI, so we know which row to
                // delete.
                selection = ClientContract.ClientEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ClientContract.ClientEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the given
        // URI has changed.
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CLIENTS:
                return updateClient(uri, values, selection, selectionArgs);
            case CLIENT_ID:
                /**
                 * For the CLIENT_ID code, extract out the ID from the URI, so we know which row to
                 * update. Selection will be "_id=?" and selection arguments will be a String array
                 * containing the actual ID.
                 */
                selection = ClientContract.ClientEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateClient(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    // Helper method to update clients in the database with the given content values. Apply the
    // changes to the rows specified in the selection and selection arguments (which could be 0 or 1
    // or more clients). Return the number of rows that were successfully updated.
    private int updateClient(Uri uri, ContentValues values, String selection, String[]
            selectionArgs) {
        // If there are no values to update, then don't try to update the database.
        if (values.size() == 0) {
            return 0;
        }

        // Check if items that need to be NOT NULL are being updated, if so sanity check them.
        if (values.containsKey(ClientContract.ClientEntry.COLUMN_FIRST_NAME)) {
            String firstName = values.getAsString(ClientContract.ClientEntry.COLUMN_FIRST_NAME);
            if (firstName == null) {
                throw new IllegalArgumentException("Client requires a first name");
            }
        }
        if (values.containsKey(ClientContract.ClientEntry.COLUMN_LAST_NAME)) {
            String lastName = values.getAsString(ClientContract.ClientEntry.COLUMN_LAST_NAME);
            if (lastName == null) {
                throw new IllegalArgumentException("Client requires a last name");
            }
        }
        if (values.containsKey(ClientContract.ClientEntry.COLUMN_LAST_SERVICE)) {
            String lastService = values.getAsString(ClientContract.ClientEntry.COLUMN_LAST_SERVICE);
            if (lastService == null) {
                throw new IllegalArgumentException("Client requires a last service date (may be " +
                        "today)");
            }
        }
        if (values.containsKey(ClientContract.ClientEntry.COLUMN_IMPORTANT)) {
            Integer important = values.getAsInteger(ClientContract.ClientEntry.COLUMN_IMPORTANT);
            if (important == null) {
                throw new IllegalArgumentException("Client requires a value for the Important " +
                        "flag");
            }
        }
        if (values.containsKey(ClientContract.ClientEntry.COLUMN_IT_WORKS)) {
            Integer itWorks = values.getAsInteger(ClientContract.ClientEntry.COLUMN_IT_WORKS);
            if (itWorks == null) {
                throw new IllegalArgumentException("Client requires a value for the It Works flag");
            }
        }
        if (values.containsKey(ClientContract.ClientEntry.COLUMN_HAD_MASSAGE_PREVIOUSLY)) {
            Integer massagePreviously = values.getAsInteger(ClientContract.ClientEntry
                    .COLUMN_HAD_MASSAGE_PREVIOUSLY);
            if (massagePreviously == null) {
                throw new IllegalArgumentException("Client requires a value for whether or not " +
                        "they have been massaged previously.");
            }
        }
        if (values.containsKey(ClientContract.ClientEntry.COLUMN_PREGNANT)) {
            Integer pregnant = values.getAsInteger(ClientContract.ClientEntry.COLUMN_PREGNANT);
            if (pregnant == null) {
                throw new IllegalArgumentException("Client requires a value for the pregnant flag");
            }
        }
        if (values.containsKey(ClientContract.ClientEntry.COLUMN_NUMBNESS)) {
            Integer numbness = values.getAsInteger(ClientContract.ClientEntry.COLUMN_NUMBNESS);
            if (numbness == null) {
                throw new IllegalArgumentException("Client requires a value for the numbness flag");
            }
        }
        if (values.containsKey(ClientContract.ClientEntry.COLUMN_SWELLING)) {
            Integer swelling = values.getAsInteger(ClientContract.ClientEntry.COLUMN_SWELLING);
            if (swelling == null) {
                throw new IllegalArgumentException("Client requires a value for the swelling flag");
            }
        }
        if (values.containsKey(ClientContract.ClientEntry.COLUMN_HEADACHES)) {
            Integer headaches = values.getAsInteger(ClientContract.ClientEntry.COLUMN_HEADACHES);
            if (headaches == null) {
                throw new IllegalArgumentException("Client requires a value for the headaches " +
                        "flag");
            }
        }

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(ClientContract.ClientEntry.TABLE_NAME, values, selection,
                selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the given
        // URI has changed.
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows updated
        return rowsUpdated;
    }


}
