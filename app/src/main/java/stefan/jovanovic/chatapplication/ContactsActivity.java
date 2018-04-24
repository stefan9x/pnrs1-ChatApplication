package stefan.jovanovic.chatapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsActivity extends Activity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private ListView lvContacts;
    private Button btnLogout;
    private TextView tvLoggedinas;

    private ContactsListAdapter contactslistadapter;
    private ChatDbHelper chatDbHelper;
    private ContactClass[] contacts;

    private static final String MY_PREFS_NAME = "PrefsFile";
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // New chatdbhelper instance and reading contacts from database
        chatDbHelper = new ChatDbHelper(this);
        contacts = chatDbHelper.readContacts();

        lvContacts = findViewById(R.id.contacts_list);
        btnLogout = findViewById(R.id.btn_logout);
        tvLoggedinas = findViewById(R.id.logged_user);

        // Adds logout button listener
        btnLogout.setOnClickListener(this);

        // Adding contacts to list
        contactslistadapter = new ContactsListAdapter(this);

        // Setting adapter to contacts list
        lvContacts.setAdapter(contactslistadapter);
        lvContacts.setOnItemLongClickListener(this);

        // Getting logged user userid, from SharedPreference file
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userId = prefs.getString("loggedin_userId", null);

        // Searching for logged in user in database,
        // and setting his username, first and last name to textview
        if (contacts != null) {
            for (int i = 0; i < contacts.length; i++) {
                if (contacts[i].getsUserId().compareTo(userId) == 0) {
                    String loggedin_user = contacts[i].getsUserName() + "(" + contacts[i].getsFirstName() +
                            " " + contacts[i].getsLastName() + ")";
                    tvLoggedinas.setText(loggedin_user);
                    break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Deleting logged user from contacts list
        deleteLoggeduserFromList();
    }

    @Override
    public void onClick(View view) {
        // Starts main activity if logout button is pressed
        if (view.getId() == R.id.btn_logout) {
            Intent intMainactivity = new Intent(ContactsActivity.this, MainActivity.class);
            startActivity(intMainactivity);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

        final int deletePos = position;

        // Delete confirmation dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getText(R.string.dialog_delete_title));
        alert.setMessage(getText(R.string.dialog_delete_contact_confirmation_text));

        alert.setPositiveButton(getText(R.string.dialog_delete_positive_btn), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Deleting contact on long press
                ContactClass contact = (ContactClass) contactslistadapter.getItem(deletePos);

                if (contacts != null) {
                    for (int i = 0; i < contacts.length; i++) {
                        if (contacts[i].getsUserId().compareTo(contact.getsUserId()) == 0) {
                            chatDbHelper.deleteContact(contact.getsUserId());
                            break;
                        }
                    }
                }

                // Deleting current logged in user from contact list
                deleteLoggeduserFromList();

            }
        });

        alert.setNegativeButton(getText(R.string.dialog_delete_negative_btn), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();

        return true;
    }

    // Function for deleting current logged in user
    // from contact list and updating that list
    // and adding bot to database
    public void deleteLoggeduserFromList() {

        // Checking if bot is in database
        int found_bot = 0;

        if (contacts != null) {
            for (int i = 0; i < contacts.length; i++) {
                if (contacts[i].getsUserName().compareTo("chatbot") == 0) {
                    found_bot = 1;
                    break;
                }
            }

        }

        // If bot is not found, then create it, if yes skip
        if (found_bot == 0) {
            ContactClass contact = new ContactClass(null, "Chat", "Bot", "chatbot");
            chatDbHelper.insertContact(contact);
        }

        contacts = chatDbHelper.readContacts();
        contactslistadapter.update(contacts);

        if (contacts != null) {
            for (int i = 0; i < contacts.length; i++) {
                if (contacts[i].getsUserId().compareTo(userId) == 0) {
                    contactslistadapter.removecontact(i);
                    break;
                }
            }
        }
    }
}
