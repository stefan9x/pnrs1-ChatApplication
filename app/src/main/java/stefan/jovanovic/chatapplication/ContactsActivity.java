package stefan.jovanovic.chatapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ContactsActivity extends Activity implements View.OnClickListener {

    private ListView lvContacts;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        lvContacts = findViewById(R.id.contacts_list);
        btnLogout = findViewById(R.id.btn_logout);

        // Adds logout button listener
        btnLogout.setOnClickListener(this);

        // Adding contacts to list
        ContactsListAdapter contactslistadapter = new ContactsListAdapter(this);
        contactslistadapter.addContactClass(new ContactClass((String) getText(R.string.contact1)));
        contactslistadapter.addContactClass(new ContactClass((String) getText(R.string.contact2)));
        contactslistadapter.addContactClass(new ContactClass((String) getText(R.string.contact3)));
        contactslistadapter.addContactClass(new ContactClass((String) getText(R.string.contact4)));

        // Setting adapter to contacts list
        lvContacts.setAdapter(contactslistadapter);
    }

    @Override
    public void onClick(View view) {
        // Starts main activity if logout button is pressed
        if (view.getId() == R.id.btn_logout) {
            Intent intMainactivity = new Intent(ContactsActivity.this, MainActivity.class);
            startActivity(intMainactivity);
        }
    }
}
