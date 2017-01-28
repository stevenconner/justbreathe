package com.sigildesigns.massagedb.data;

/**
 * Class to contain each client's information to pass to the RecyclerView
 */

public class Client {
    private int mClientID;
    private String mFirstName;
    private String mLastName;
    private String mLastService;
    private boolean mImportant;
    private String mImportantNotes;
    private boolean mItWorks;
    private boolean mGender;
    private String mEmail;
    private String mPhoneNumber;
    private String mBirthday;
    private String mReferral;
    private String mEmergencyName;
    private String mEmergencyPhone;
    private boolean mMassagePreviously;
    private String mMassagePreviouslyDate;
    private boolean mPregnant;
    private String mPregnantWeeks;
    private String mLastPregnancy;
    private String mMedications;
    private String mGoals;
    private String mSurgeries;
    private String mHealthIssues;
    private boolean mNumbness;
    private boolean mSwelling;
    private boolean mHeadaches;
    private String mDerogatoryNotes;
    private String mNotes;

    // Public constructor containing just the stuff we need to display in a list.
    public Client(int clientID, String firstName, String lastName, String lastService,
                  boolean important, boolean itWorks) {
        mClientID = clientID;
        mFirstName = firstName;
        mLastName = lastName;
        mLastService = lastService;
        mImportant = important;
        mItWorks = itWorks;
    }

    // Public constructor containing every possible table column.
    public Client(int clientID, String firstName, String lastName, String lastService, boolean
            important, String importantNotes, boolean itWorks, boolean gender, String email,
                  String phoneNumber, String birthday, String referral, String emergencyName,
                  String emergencyPhone, boolean massagePreviously, String massagePreviouslyDate,
                  boolean pregnant, String pregnantWeeks, String lastPregnancy, String medications,
                  String goals, String surgeries, String healthIssues, boolean numbness,
                  boolean swelling, boolean headaches, String derogatoryNotes, String notes) {

        // Bind the given information for each client to the private variables
        mClientID = clientID;
        mFirstName = firstName;
        mLastName = lastName;
        mLastService = lastService;
        mImportant = important;
        mImportantNotes = importantNotes;
        mItWorks = itWorks;
        mGender = gender;
        mEmail = email;
        mPhoneNumber = phoneNumber;
        mBirthday = birthday;
        mReferral = referral;
        mEmergencyName = emergencyName;
        mEmergencyPhone = emergencyPhone;
        mMassagePreviously = massagePreviously;
        mMassagePreviouslyDate = massagePreviouslyDate;
        mPregnant = pregnant;
        mPregnantWeeks = pregnantWeeks;
        mLastPregnancy = lastPregnancy;
        mMedications = medications;
        mGoals = goals;
        mSurgeries = surgeries;
        mHealthIssues = healthIssues;
        mNumbness = numbness;
        mSwelling = swelling;
        mHeadaches = headaches;
        mDerogatoryNotes = derogatoryNotes;
        mNotes = notes;

    }

    // Getter methods for each variable
    public int getmClientID() {
        return mClientID;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public String getmLastService() {
        return mLastService;
    }

    public boolean getmImportant() {
        return mImportant;
    }

    public boolean getmItWorks() {
        return mItWorks;
    }

    public String getmImportantNotes() {
        return mImportantNotes;
    }

    public boolean getmGender() {
        return mGender;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public String getmBirthday() {
        return mBirthday;
    }

    public String getmReferral() {
        return mReferral;
    }

    public String getmEmergencyName() {
        return mEmergencyName;
    }

    public String getmEmergencyPhone() {
        return mEmergencyPhone;
    }

    public boolean getmMassagePreviously() {
        return mMassagePreviously;
    }

    public String getmMassagePreviouslyDate() {
        return mMassagePreviouslyDate;
    }

    public boolean getmPregnant() {
        return mPregnant;
    }

    public String getmPregnantWeeks() {
        return mPregnantWeeks;
    }

    public String getmLastPregnancy() {
        return mLastPregnancy;
    }

    public String getmMedications() {
        return mMedications;
    }

    public String getmGoals() {
        return mGoals;
    }

    public String getmSurgeries() {
        return mSurgeries;
    }

    public String getmHealthIssues() {
        return mHealthIssues;
    }

    public boolean getmNumbness() {
        return mNumbness;
    }

    public boolean getmSwelling() {
        return mSwelling;
    }

    public boolean getmHeadaches() {
        return mHeadaches;
    }

    public String getmDerogatoryNotes() {
        return mDerogatoryNotes;
    }

    public String getmNotes() {
        return mNotes;
    }


}
