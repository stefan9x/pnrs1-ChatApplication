package stefan.jovanovic.chatapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "chatapp.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME_CONTACTS = "contact";
    public static final String COLUMN_CONTACT_ID = "";
    public static final String COLUMN_FIRST_NAME = "FirstName";
    public static final String COLUMN_LAST_NAME = "LastName";
    public static final String COLUMN_USERNAME = "UserName";

    public static final String TABLE_NAME_MESSAGES = "message";
    public static final String COLUMN_MESSAGE_ID = "";
    public static final String COLUMN_SENDER_ID = "SenderId";
    public static final String COLUMN_RECEIVER_ID = "ReceiverId";
    public static final String COLUMN_MESSAGE = "Message";

    public ChatDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_CONTACTS + " (" +
                COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT, " +
                COLUMN_USERNAME + " TEXT);" );

        db.execSQL("CREATE TABLE " + TABLE_NAME_MESSAGES + " (" +
                COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_SENDER_ID + " TEXT, " +
                COLUMN_RECEIVER_ID + " TEXT, " +
                COLUMN_MESSAGE + " TEXT);" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert_contacts(ContactClass contact) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_ID, contact.getsId());
        values.put(COLUMN_FIRST_NAME, contact.getsFirstName());
        values.put(COLUMN_LAST_NAME, contact.getsLastName());
        values.put(COLUMN_USERNAME, contact.getsUserName());

        db.insert(TABLE_NAME_CONTACTS, null, values);
        close();
    }

    public void insert_messages(MessageClass message) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MESSAGE_ID, message.getsId());
        values.put(COLUMN_SENDER_ID, message.getsSenderId());
        values.put(COLUMN_RECEIVER_ID, message.getsReceiverId());
        values.put(COLUMN_MESSAGE, message.getsMessage());

        db.insert(TABLE_NAME_MESSAGES, null, values);
        close();
    }

    public ContactClass[] readContacts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CONTACTS, null, null, null, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        ContactClass[] contacts = new ContactClass[cursor.getCount()];

        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            contacts[i++] = createContacts(cursor);
        }

        close();
        return contacts;
    }

    /*public ContactClass readContact(String conctactId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CONTACTS, null, COLUMN_CONTACT_ID + "=?",
                new String[] {conctactId}, null, null, null);
        cursor.moveToFirst();
        ContactClass contacts = createContacts(cursor);

        close();
        return contacts;
    }*/

    public void deleteContact(String contactUsername) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_CONTACTS, COLUMN_USERNAME + "=?", new String[] {contactUsername});
        close();
    }

    private ContactClass createContacts(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID));
        String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));
        String userName = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
        
        return new ContactClass(id, firstName, lastName, userName);
    }

    //////////////////////////////////
    public MessageClass[] readMessages() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_MESSAGES, null, null, null, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        MessageClass[] messages = new MessageClass[cursor.getCount()];

        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            messages[i++] = createMessage(cursor);
        }

        close();
        return messages;
    }

    /*public ContactClass readContact(String conctactId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CONTACTS, null, COLUMN_CONTACT_ID + "=?",
                new String[] {conctactId}, null, null, null);
        cursor.moveToFirst();
        ContactClass contacts = createContacts(cursor);

        close();
        return contacts;
    }*/

    public void deleteMessage(String message) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_MESSAGES, COLUMN_MESSAGE + "=?", new String[] {message});
        close();
    }

    private MessageClass createMessage(Cursor cursor) {
        String sMessage = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE));
        String sSenderId = cursor.getString(cursor.getColumnIndex(COLUMN_SENDER_ID));
        String sReceiverId = cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVER_ID));

        return new MessageClass(sMessage, sSenderId, sReceiverId);
    }
}
