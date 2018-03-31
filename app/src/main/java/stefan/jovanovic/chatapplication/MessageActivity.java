package stefan.jovanovic.chatapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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

    public TextWatcher twSend = new TextWatcher() {

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

        btnLogout = findViewById(R.id.btn_logout_message);
        btnSend = findViewById(R.id.btn_send);
        etMessage = findViewById(R.id.et_message);
        tvContactname = findViewById(R.id.tv_message_contact_name);
        lvMessages = findViewById(R.id.messages_list);

        Intent contacts_intent = getIntent();
        tvContactname.setText(contacts_intent.getStringExtra("contact_name"));

        lvMessages.setAdapter(messagelistadapter);
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
    public void onClick(View view) {
        // Starts main activity if logout button is pressed
        if (view.getId() == R.id.btn_logout_message) {
            Intent intMainactivity = new Intent(MessageActivity.this, MainActivity.class);
            startActivity(intMainactivity);
        }

        // Shows toast message if send button is pressed and clears message field
        if (view.getId() == R.id.btn_send) {
            messagelistadapter.addMessagesClass(new MessageClass(etMessage.getText().toString(), "User"));
            chatBot(etMessage.getText().toString());
            Toast.makeText(this, getText(R.string.message_sent), Toast.LENGTH_SHORT).show();
            etMessage.getText().clear();
        }
    }

    public void chatBot(String text) {

        if (text.contains("hello")) {
            messagelistadapter.addMessagesClass(new MessageClass("Hey!", "Bot"));
        }
        if (text.contains("how")) {
            messagelistadapter.addMessagesClass(new MessageClass("I'm fine thanks, you?", "Bot"));
        }
        if (text.contains("what")) {
            messagelistadapter.addMessagesClass(new MessageClass("Nothing special.", "Bot"));
        }
        if (text.contains("yes")) {
            messagelistadapter.addMessagesClass(new MessageClass("what yes?", "Bot"));
        }
        if(text.contains("no")){
            messagelistadapter.addMessagesClass(new MessageClass("what no?", "Bot"));
        }
        if(text.contains("bye")){
            messagelistadapter.addMessagesClass(new MessageClass("Bye!", "Bot"));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        final int deletePos = position;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete?");
        alert.setMessage("Are you sure you want to delete message?");

        alert.setPositiveButton("YES", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do your work here
                messagelistadapter.removeMessagesClass(deletePos);
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
