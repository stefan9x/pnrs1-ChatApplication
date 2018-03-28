package stefan.jovanovic.chatapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends Activity implements View.OnClickListener {

    private Button btnLogout;
    private Button btnSend;
    private EditText etMessage;
    private TextView tvContactname;
    private ListView lvMessages;

    int pos = 0;

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
            Toast.makeText(this, getText(R.string.message_sent), Toast.LENGTH_SHORT).show();
            messagelistadapter.addMessagesClass(new MessageClass(etMessage.getText().toString()));
            etMessage.getText().clear();
            pos++;
            chatBot();

        }
    }

    public void chatBot() {

        if (pos == 1) {
            messagelistadapter.addMessagesClass(new MessageClass("Hey!"));
        }
        if (pos == 2) {
            messagelistadapter.addMessagesClass(new MessageClass("I'm fine thanks, whats up?"));
        }
        if (pos == 3) {
            messagelistadapter.addMessagesClass(new MessageClass("Nothing special, will i see you tonight?"));
        }
        if (pos == 4) {
            messagelistadapter.addMessagesClass(new MessageClass("Fine, see you!"));
        }
        if(pos == 5){
            messagelistadapter.addMessagesClass(new MessageClass("Bye!"));
        }
    }
}
