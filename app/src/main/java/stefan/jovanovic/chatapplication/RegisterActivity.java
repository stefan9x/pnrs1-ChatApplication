package stefan.jovanovic.chatapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import java.util.Date;


public class RegisterActivity extends Activity implements View.OnClickListener{

    private EditText etUsername;
    private EditText etPassword;
    //private EditText etName; // for later use
    //private EditText etLastname; // for later use
    private EditText etEmail;
    private Button btnRegister;
    private DatePicker dpDatepicker;

    public TextWatcher twRegister = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable e) {

            String sUsername = etUsername.getText().toString();
            String sPassword = etPassword.getText().toString();
            String sEmail = etEmail.getText().toString();

            boolean bUsername, bPassword, bEmail;

            // Checks if users entered email in correct format
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()){
                bEmail = true;
            } else{
                // Display error if email is incorrectly typed
                etEmail.setError(getText(R.string.error_email));
                bEmail = false;
            }

            // Checks if username is entered
            if (sUsername.length()>0){
                bUsername = true;
            } else{
                bUsername = false;
                etUsername.setError(getText(R.string.error_username));
            }

            // Checks if password with minimum of 6 characters is entered
            if (sPassword.length()>5){
                bPassword = true;
            } else{
                // Display error if password is too short
                etPassword.setError(getText(R.string.error_password_minimum));
                bPassword = false;
            }

            // Enables register button if required fields are filled
            if (bEmail && bUsername && bPassword) {
                btnRegister.setEnabled(true);
            } else{
                btnRegister.setEnabled(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_new_username);
        etPassword = findViewById(R.id.et_new_password);
        etEmail = findViewById(R.id.et_email);
        btnRegister = findViewById(R.id.btn_new_register);
        dpDatepicker = findViewById(R.id.dp_datePicker);

        // Sets max date for date picker
        dpDatepicker.setMaxDate(new Date().getTime());

        // Disables register button on register activity create
        btnRegister.setEnabled(false);

        // Adds text watchers on username, password and email fields
        etUsername.addTextChangedListener(twRegister);
        etPassword.addTextChangedListener(twRegister);
        etEmail.addTextChangedListener(twRegister);

        // Adds register button listeners
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Starts contacts activity if register button is pressed
        if (view.getId() == R.id.btn_new_register){
            Intent ContactsActivity_intent = new Intent (RegisterActivity.this, ContactsActivity.class);
            startActivity(ContactsActivity_intent);
        }
    }
}
