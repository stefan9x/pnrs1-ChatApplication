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

public class MessageActivity extends Activity implements View.OnClickListener, AdapterView.OnItemLongClickListener{

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

        chatDbHelper = new ChatDbHelper(this);

        ContactClass[] contacts = chatDbHelper.readContacts();

        if (contacts != null) {
            for (int i = 0; i < contacts.length; i++) {
                if (contacts[i].getsId().compareTo(receiver_userid) == 0){
                    String receiver_user= contacts[i].getsFirstName() + " " + contacts[i].getsLastName();
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

        messages = chatDbHelper.readMessages(sender_userid, receiver_userid);
        messagelistadapter.update(messages);

    }

    @Override
    public void onClick(View view) {
        // Starts main activity if logout button is pressed
        if (view.getId() == R.id.btn_logout_message) {
            Intent intMainactivity = new Intent(MessageActivity.this, MainActivity.class);
            startActivity(intMainactivity);
        }

        // Shows toast message if send button is pressed and clears message field
        if (view.getId() == R.id.btn_send) {

            MessageClass message = new MessageClass(null, sender_userid, receiver_userid,
                    etMessage.getText().toString());
            chatDbHelper.insert_message(message);
            messages = chatDbHelper.readMessages(sender_userid, receiver_userid);
            messagelistadapter.update(messages);

            //chatBot(etMessage.getText().toString());
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

        // Delete confirmation dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete?");
        alert.setMessage("Are you sure you want to delete message?");

        alert.setPositiveButton("YES", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do your work here
                MessageClass message = (MessageClass) messagelistadapter.getItem(deletePos);

                if (messages != null) {
                    for (int i = 0; i < messages.length; i++) {
                        if (messages[i].getsId().compareTo(message.getsId()) == 0){
                            chatDbHelper.deleteMessage(message.getsId());
                            break;
                        }
                    }
                }
                messages = chatDbHelper.readMessages(sender_userid, receiver_userid);
                messagelistadapter.update(messages);
            }
        });

        alert.setNegativeButton("NO", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();

        return true;
    }
}
