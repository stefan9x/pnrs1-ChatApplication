package stefan.jovanovic.chatapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MessageActivity extends Activity implements View.OnClickListener{

    private Button btnLogout;
    private Button btnSend;
    private EditText etMessage;

    public TextWatcher etTextwatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable e) {

            String sMessage = etMessage.getText().toString();

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

        btnLogout = findViewById(R.id.logout_message);
        btnSend = findViewById(R.id.send);
        etMessage = findViewById(R.id.message);

        if (etMessage.length() == 0){
            btnSend.setEnabled(false);
        }

        etMessage.addTextChangedListener(etTextwatcher);
        btnLogout.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.logout_message){
            Intent intMainactivity = new Intent (MessageActivity.this, MainActivity.class);
            startActivity(intMainactivity);
        }

        if (view.getId() == R.id.send){
            Toast.makeText(MessageActivity.this, "Message is sent!", Toast.LENGTH_SHORT).show();
            etMessage.getText().clear();
        }
    }
}
