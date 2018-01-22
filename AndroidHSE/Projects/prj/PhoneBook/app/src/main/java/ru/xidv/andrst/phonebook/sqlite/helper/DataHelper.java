package ru.xidv.andrst.phonebook.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.xidv.andrst.phonebook.sqlite.model.Contact;

/**
 * Created by Александр on 14.12.2017.
 */

public class DataHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactManager";

    // Table Names
    private static final String TABLE_CONTACTS = "CONTACTS";

    // Table column names
    private static final String KEY_ID = "id";
    private static final String KEY_CONTACT_NAME = "contact_name";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_NOTE = "note";

    private static final String CREATE_TABLE_CONTACTS = "CREATE TABLE "
            + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CONTACT_NAME
            + " TEXT," + KEY_PHONE_NUMBER + " TEXT," + KEY_NOTE + " TEXT" + ")";


    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // create new tables
        onCreate(db);

    }

    /**
     * Creating a contact
     */
    public long createContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_NAME, contact.getContactName());
        values.put(KEY_PHONE_NUMBER, contact.getPhoneNumber());
        values.put(KEY_NOTE, contact.getNote());

        // insert row
        return db.insert(TABLE_CONTACTS, null, values);
    }

    /**
     * get single contact
     */
    public Contact getContact(long contact_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE "
                + KEY_ID + " = " + contact_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Contact contact = new Contact();
        contact.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        contact.setContactName(c.getString(c.getColumnIndex(KEY_CONTACT_NAME)));
        contact.setPhoneNumber(c.getString(c.getColumnIndex(KEY_PHONE_NUMBER)));
        contact.setNote((c.getString(c.getColumnIndex(KEY_NOTE))));

        return contact;
    }

    /**
     * getting all contacts
     * */
    public List<Contact> getAllContacts() {
        ArrayList<Contact> contactsArrayList = new ArrayList<Contact>();
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                Contact contact = new Contact();
                contact.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                contact.setContactName(c.getString(c.getColumnIndex(KEY_CONTACT_NAME)));
                contact.setPhoneNumber(c.getString(c.getColumnIndex(KEY_PHONE_NUMBER)));
                contact.setNote((c.getString(c.getColumnIndex(KEY_NOTE))));

                contactsArrayList.add(contact);
            } while (c.moveToNext());
        }

        return contactsArrayList;
    }

    /**
     * Updating a contact info
     */
    public int updateContactInfo(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_NAME, contact.getContactName());
        values.put(KEY_PHONE_NUMBER, contact.getPhoneNumber());
        values.put(KEY_NOTE, contact.getNote());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
    }

    /**
     * Deleting a contact
     */

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
    }

    /**
     * Deleting all data
     */
    public void deleteAll(String TABLE) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE);

    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
