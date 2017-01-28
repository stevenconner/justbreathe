package com.sigildesigns.massagedb.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * SQLite DB contract for the ClientList DB
 */

public class ClientContract {
    // To prevent someone from accidentally instantiating the contract class, give it an empty
    // constructor.
    private ClientContract(){}

    // Content Authority for the app
    public static final String CONTENT_AUTHORITY = "com.sigildesigns.massagedb";

    // Use CONTENT_AUTHORITY to create the base of all URIs which apps will use to contact the
    // content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible path (appended to BASE_CONTENT_URI for possible URIs)
    public static final String PATH_CLIENTS = "clients";

    // Inner class that defines constant values for the clients database table.
    // Each entry in the table represents a single client.
    public static final class ClientEntry implements BaseColumns {
        // The content URI to access the client data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CLIENTS);

        // The MIME type of the {@link #CONTENT_URI} for a list of clients
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_CLIENTS;

        // The MIME type of the {@link #CONTENT_URI} for a single client
        public static final String CONTENT_CLIENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_CLIENTS;

        // Name of the database table for clients
        public static final String TABLE_NAME = "clients";

        // Unique ID number for the client (only for use in the database table). Type: INTEGER
        public static final String _ID = BaseColumns._ID;

        // First name of the client. Type: TEXT.
        public static final String COLUMN_FIRST_NAME = "firstName";

        // Last name of the client. Type: TEXT.
        public static final String COLUMN_LAST_NAME = "lastName";

        // Last service date of the client. Type: TEXT.
        public static final String COLUMN_LAST_SERVICE = "lastService";

        // Flag for important notes about the client. Type: INTEGER. (0 = false 1 = true)
        public static final String COLUMN_IMPORTANT = "important";

        // Important notes about the client. Type: TEXT.
        public static final String COLUMN_IMPORTANT_NOTES = "importantNotes";

        // Flag for being an It Works customer. Type INTEGER. (0 = false 1 = true)
        public static final String COLUMN_IT_WORKS = "itWorks";

        // Gender for the client. Type INTEGER. (0 = female 1 = male)
        public static final String COLUMN_GENDER = "gender";

        // Email for the client. Type TEXT.
        public static final String COLUMN_EMAIL = "email";

        // Phone number for the client. Type TEXT.
        public static final String COLUMN_PHONE_NUMBER = "phone";

        // Birthday for the client. Type TEXT.
        public static final String COLUMN_BIRTHDAY = "birthday";

        // Name of the person who referred client. Type TEXT.
        public static final String COLUMN_REFERRAL = "referral";

        // Emergency contact name. Type TEXT.
        public static final String COLUMN_EMERGENCY_CONTACT_NAME = "emergencyName";

        // Emergency contact phone number. Type TEXT.
        public static final String COLUMN_EMERGENCY_CONTACT_PHONE_NUMBER = "emergencyPhone";

        // Flag for if they have had massage before. Type INTEGER. (0 = false 1 = true)
        public static final String COLUMN_HAD_MASSAGE_PREVIOUSLY = "massagePreviously";

        // Date for previous massage. Type TEXT.
        public static final String COLUMN_HAD_MASSAGE_PREVIOUSLY_DATE = "massagePreviouslyDate";

        // Flag for if the client is pregnant. Type INTEGER. (0 = false 1 = true)
        public static final String COLUMN_PREGNANT = "pregnant";

        // How many weeks pregnant. Type TEXT.
        public static final String COLUMN_PREGNANT_WEEKS = "pregnantWeeks";

        // Last pregnancy date (or current if currently pregnant). Type TEXT.
        public static final String COLUMN_LAST_PREGNANCY_DATE = "lastPregnancy";

        // Medications the client is taking. Type TEXT.
        public static final String COLUMN_MEDICATIONS = "medications";

        // Purpose or goal of visits for the client. Type TEXT.
        public static final String COLUMN_GOALS = "goals";

        // Recent surgeries within 3 years for the client. Type TEXT.
        public static final String COLUMN_RECENT_SURGERIES = "surgeries";

        // Health issues to be aware of for the client. Type TEXT.
        public static final String COLUMN_HEALTH_ISSUES = "healthIssues";

        // Client experiences numbness. Type INTEGER. (0 = false 1 = true)
        public static final String COLUMN_NUMBNESS = "numbness";

        // Client experiences swelling. Type INTEGER. (0 = false 1 = true)
        public static final String COLUMN_SWELLING = "swelling";

        // Client experiences headaches frequently. Type INTEGER. (0 = false 1 = true)
        public static final String COLUMN_HEADACHES = "headaches";

        // Client's derogatory notes. Type TEXT.
        public static final String COLUMN_DEROGATORY_NOTES = "derogatoryNotes";

        // Notes about the client. Type TEXT.
        public static final String COLUMN_NOTES = "notes";
    }
}
