package stefan.jovanovic.chatapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ContactsActivity extends Activity implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, ServiceConnection {

    private ListView lvContacts;
    private Button btnLogout;
    private Button btnRefresh;
    private TextView tvLoggedinas;

    private ContactsListAdapter contactsListAdapter;

    private static final String MY_PREFS_NAME = "PrefsFile";
    private String loggedinUsername;

    private HttpHelper httphelper;
    private Handler handler;

    private INotificationBinder mService = null;

    private Crypto mCrypto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        lvContacts = findViewById(R.id.contacts_list);
        btnLogout = findViewById(R.id.btn_logout);
        tvLoggedinas = findViewById(R.id.logged_user);
        btnRefresh = findViewById(R.id.btn_refresh);

        // Adds logout button listener
        btnLogout.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);

        // Adding contacts to list
        contactsListAdapter = new ContactsListAdapter(this);

        // Setting adapter to contacts list
        lvContacts.setAdapter(contactsListAdapter);
        lvContacts.setOnItemClickListener(this);
        lvContacts.setOnItemLongClickListener(this);

        // Getting logged user userid, from SharedPreference file
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        loggedinUsername = prefs.getString("loggedinUsername", null);

        tvLoggedinas.setText(loggedinUsername);

        httphelper = new HttpHelper();

        handler = new Handler();
        mCrypto = new Crypto();
        
        bindService(new Intent(ContactsActivity.this, NotificationService.class), this, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Updating list
        updateContactList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mService != null) {
            unbindService(this);
        }
    }

    @Override
    public void onClick(View view) {
        // Starts main activity if logout button is pressed
        switch (view.getId()){
            case R.id.btn_logout:

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            final boolean success = httphelper.logOutUserFromServer(ContactsActivity.this);
                            handler.post(new Runnable(){
                                public void run() {
                                    if (success) {
                                        startActivity(new Intent(ContactsActivity.this, LoginActivity.class));
                                    } else {
                                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                        String logoutErr = prefs.getString("logoutErr", null);
                                        Toast.makeText(ContactsActivity.this, logoutErr, Toast.LENGTH_SHORT).show();}
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;

            case R.id.btn_refresh:
                updateContactList();
                break;
        }
    }

    // Updating contacts list
    public void updateContactList() {
        new Thread(new Runnable() {
            ContactClass[] contactsClass;
            public void run() {
                try {
                    final JSONArray contacts = httphelper.getContactsFromServer(ContactsActivity.this);
                    handler.post(new Runnable(){
                        public void run() {
                            if (contacts != null) {
                                JSONObject json_contact;
                                contactsClass = new ContactClass[contacts.length()];

                                for (int i = 0; i < contacts.length(); i++) {
                                    try {
                                        json_contact = contacts.getJSONObject(i);
                                        contactsClass[i] = new ContactClass(json_contact.getString("username"), getText(R.string.tap_to_get_msg).toString());
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                contactsListAdapter.update(contactsClass);
                            }
                        }
                    });
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
    }

    public void getLastMsg(ContactClass oldContact){
        final String contact = oldContact.getsUserName();
        final ContactClass oldContactTemp = oldContact;

        new Thread(new Runnable() {

            public void run() {
                try {
                    final JSONArray messages = httphelper.getMessagesFromServer(ContactsActivity.this, contact);
                    handler.post(new Runnable(){
                        public void run() {
                            if (messages != null) {
                                String lastMsg = getText(R.string.no_new_messages).toString();
                                JSONObject json_message;
                                if (messages.length()>0){
                                    int lastMsgIndex = messages.length()-1;
                                    try {
                                        json_message = messages.getJSONObject(lastMsgIndex);

                                        String message = json_message.getString("data");
                                        String cryptedMsg = mCrypto.crypt(message);
                                        lastMsg = json_message.getString("sender") +": " + cryptedMsg;
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                ContactClass newContact = new ContactClass(contact, lastMsg);
                                contactsListAdapter.updateOne(oldContactTemp, newContact);
                            }
                        }
                    });
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        ContactClass oldContact = (ContactClass) contactsListAdapter.getItem(position);
        getLastMsg(oldContact);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

        final ContactClass contactForDelete = (ContactClass) contactsListAdapter.getItem(position);

        // Delete confirmation dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getText(R.string.dialog_delete_title));
        alert.setMessage(getText(R.string.dialog_delete_contact_confirmation_text));


        alert.setPositiveButton(getText(R.string.dialog_delete_positive_btn), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Deleting contact on long press
                new Thread(new Runnable() {
                    public void run() {

                        try {
                            String usernameForDelete = contactForDelete.getsUserName();

                            final boolean response = httphelper.deleteUserFromServer(ContactsActivity.this, usernameForDelete);

                            handler.post(new Runnable(){
                                public void run() {
                                    if (response) {
                                        Toast.makeText(ContactsActivity.this, getText(R.string.success_contact_delete).toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                        String deleteContactErr = prefs.getString("deleteContactErr", null);
                                        Toast.makeText(ContactsActivity.this, deleteContactErr, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            // Updating list
                            updateContactList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();



            }
        });

        alert.setNegativeButton(getText(R.string.dialog_delete_negative_btn), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();

        return false;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mService = INotificationBinder.Stub.asInterface(iBinder);
        try {
            mService.setCallback(new NotificationCallback());
        } catch (RemoteException e) {
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mService = null;
    }

    private class NotificationCallback extends INotificationCallback.Stub {

        @Override
        public void onCallbackCall() throws RemoteException {

            final HttpHelper httpHelper = new HttpHelper();
            final Handler handler = new Handler();

            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), null)
                    .setSmallIcon(R.drawable.ic_stat_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.mipmap.ic_launcher))
                    .setContentTitle(getText(R.string.app_name))
                    .setContentText(getText(R.string.have_new_message))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());


            new Thread(new Runnable() {
                public void run() {
                    try {
                        final boolean response = httpHelper.getNotification(ContactsActivity.this);

                        handler.post(new Runnable() {
                            public void run() {
                                if (response) {
                                    // notificationId is a unique int for each notification that you must define
                                    notificationManager.notify(2, mBuilder.build());
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

}
