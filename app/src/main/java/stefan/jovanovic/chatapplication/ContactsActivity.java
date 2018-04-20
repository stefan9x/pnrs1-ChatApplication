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
    private TextView etLoggedinas;

    private ContactsListAdapter contactslistadapter;
    private ChatDbHelper chatDbHelper;

    public static final String MY_PREFS_NAME = "PrefsFile";
    public String userId;

    public ContactClass[] contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        chatDbHelper = new ChatDbHelper(this);
        contacts = chatDbHelper.readContacts();

        lvContacts = findViewById(R.id.contacts_list);
        btnLogout = findViewById(R.id.btn_logout);

        // Adds logout button listener
        btnLogout.setOnClickListener(this);

        // Adding contacts to list
        contactslistadapter = new ContactsListAdapter(this);

        // Setting adapter to contacts list
        lvContacts.setAdapter(contactslistadapter);
        lvContacts.setOnItemLongClickListener(this);

        etLoggedinas = findViewById(R.id.logged_user);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userId = prefs.getString("loggedin_userId", null);

        if (contacts != null) {
            for (int i = 0; i < contacts.length; i++) {
                if (contacts[i].getsId().compareTo(userId) == 0){
                    String loggedin_user = contacts[i].getsUserName() + "(" + contacts[i].getsFirstName() +
                            " " + contacts[i].getsLastName() + ")";
                    etLoggedinas.setText(loggedin_user);
                    break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        deleteMe();
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
        alert.setTitle("Delete?");
        alert.setMessage("Are you sure you want to delete contact?");

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do your work here
                ContactClass contact = (ContactClass) contactslistadapter.getItem(deletePos);

                if (contacts != null) {
                    for (int i = 0; i < contacts.length; i++) {
                        if (contacts[i].getsId().compareTo(contact.getsId()) == 0){
                            chatDbHelper.deleteContact(contact.getsId());
                            break;
                        }
                    }
                }

                deleteMe();
            }
        });

        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();

        return true;
    }

    public void deleteMe(){

        contacts = chatDbHelper.readContacts();
        contactslistadapter.update(contacts);

        if (contacts != null) {
            for (int i = 0; i < contacts.length; i++) {
                if (contacts[i].getsId().compareTo(userId) == 0){
                    contactslistadapter.removecontact(i);
                    break;
                }
            }
        }
    }
}
