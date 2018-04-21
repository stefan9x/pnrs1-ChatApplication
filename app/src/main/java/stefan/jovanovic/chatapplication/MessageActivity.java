package stefan.jovanovic.chatapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class MessageActivity extends Activity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private Button btnLogout;
    private Button btnSend;
    private EditText etMessage;
    private TextView tvContactname;
    private ListView lvMessages;

    private MessageListAdapter messagelistadapter = new MessageListAdapter(this);

    private static final String MY_PREFS_NAME = "PrefsFile";
    private String receiver_userid;
    private String sender_userid;

    private ChatDbHelper chatDbHelper;
    private MessageClass[] messages;

    private TextWatcher twSend = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable e) {

            String sMessage = etMessage.getText().toString();

            // Disables send button if message is not entered
            if (sMessage.length() == 0) {
                btnSend.setEnabled(false);
            } else {
                btnSend.setEnabled(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Contact name in upper corner
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        receiver_userid = prefs.getString("receiver_userId", null);
        sender_userid = prefs.getString("loggedin_userId", null);

        btnLogout = findViewById(R.id.btn_logout_message);
        btnSend = findViewById(R.id.btn_send);
        etMessage = findViewById(R.id.et_message);
        tvContactname = findViewById(R.id.tv_message_contact_name);
        lvMessages = findViewById(R.id.messages_list);

        // New chatdbhelper instance
        chatDbHelper = new ChatDbHelper(this);

        // Reading contacts from database
        ContactClass[] contacts = chatDbHelper.readContacts();

        // Setting receiver contact name in top left corner
        if (contacts != null) {
            for (int i = 0; i < contacts.length; i++) {
                if (contacts[i].getsUserId().compareTo(receiver_userid) == 0) {
                    String receiver_user = contacts[i].getsFirstName() + " " + contacts[i].getsLastName();
                    tvContactname.setText(receiver_user);
                    break;
                }
            }
        }

        // Setting adapter to list
        lvMessages.setAdapter(messagelistadapter);

        // On item long click listener
        lvMessages.setOnItemLongClickListener(this);

        // Disables send button on message activity create
        btnSend.setEnabled(false);

        // Adds text watcher to message field
        etMessage.addTextChangedListener(twSend);

        // Adds listeners on logout and send buttons
        btnLogout.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Update messages list
        updateMessagesList(sender_userid, receiver_userid);
    }

    @Override
    public void onClick(View view) {
        // Starts main activity if logout button is pressed
        if (view.getId() == R.id.btn_logout_message) {
            Intent intMainactivity = new Intent(MessageActivity.this, MainActivity.class);
            startActivity(intMainactivity);
        }

        if (view.getId() == R.id.btn_send) {

            // Inserting message into database and updating messages list
            MessageClass message = new MessageClass(null, sender_userid, receiver_userid,
                    etMessage.getText().toString());
            chatDbHelper.insert_message(message);
            updateMessagesList(sender_userid, receiver_userid);

            //chatBot(etMessage.getText().toString());

            // Shows toast if send button is pressed and clears message field
            Toast.makeText(this, getText(R.string.message_sent), Toast.LENGTH_SHORT).show();
            etMessage.getText().clear();
        }
    }

   /* public void chatBot(String text) {

        // Simple chat bot
        if (text.toLowerCase().contains("hello")) {
            messagelistadapter.addMessagesClass(new MessageClass("Hey!", "Bot"));
        }
        else if (text.toLowerCase().contains("how")) {
            messagelistadapter.addMessagesClass(new MessageClass("I'm fine thanks, you?", "Bot"));
        }
        else if (text.toLowerCase().contains("what")) {
            messagelistadapter.addMessagesClass(new MessageClass("Nothing special.", "Bot"));
        }
        else if (text.toLowerCase().contains("yes")) {
            messagelistadapter.addMessagesClass(new MessageClass("what yes?", "Bot"));
        }
        else if(text.toLowerCase().contains("no")){
            messagelistadapter.addMessagesClass(new MessageClass("what no?", "Bot"));
        }
        else if(text.toLowerCase().contains("bye")){
            messagelistadapter.addMessagesClass(new MessageClass("Bye!", "Bot"));
        }
    }*/

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        final int deletePos = position;

        final MessageClass message = (MessageClass) messagelistadapter.getItem(deletePos);

        // Check if user is deleting his messages, if not, show toast
        if (message.getsSenderId().compareTo(sender_userid) == 0) {
            // Delete confirmation dialog
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getText(R.string.dialog_delete_title));
            alert.setMessage(getText(R.string.dialog_delete_message_confirmation_text));

            alert.setPositiveButton(getText(R.string.dialog_delete_positive_btn), new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Deleting message from database
                    if (messages != null) {
                        for (int i = 0; i < messages.length; i++) {
                            if (messages[i].getsMessageId().compareTo(message.getsMessageId()) == 0) {
                                chatDbHelper.deleteMessage(message.getsMessageId());
                                break;
                            }
                        }
                    }

                    // Updating messages list
                    updateMessagesList(sender_userid, receiver_userid);
                }
            });

            alert.setNegativeButton(getText(R.string.dialog_delete_negative_btn), new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            alert.show();

            return true;
        } else {
            Toast.makeText(this, getText(R.string.error_delete_only_your_messages), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Function for reading messages from database
    // and updating messages list
    public void updateMessagesList(String senderid, String receiverid) {
        messages = chatDbHelper.readMessages(senderid, receiverid);
        messagelistadapter.update(messages);
    }
}
