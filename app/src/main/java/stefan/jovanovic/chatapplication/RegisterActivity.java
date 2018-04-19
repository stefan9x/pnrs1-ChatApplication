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
import android.widget.Toast;

import java.util.Date;


public class RegisterActivity extends Activity implements View.OnClickListener{

    private EditText etUsername;
    private EditText etPassword;
    private EditText etFirstName;
    private EditText etLastname;
    private EditText etEmail;
    private Button btnRegister;
    private DatePicker dpDatepicker;

    private ChatDbHelper chatDbHelper;

    public TextWatcher twRegister = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable e) {


            String sPassword = etPassword.getText().toString();
            String sEmail = etEmail.getText().toString();
            String sFistName = etFirstName.getText().toString();
            String sLastName = etLastname.getText().toString();
            String sUsername = etUsername.getText().toString();

            boolean bUsername, bPassword, bEmail, bFistName, bLastName;

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

            if (sFistName.length()>0){
                bFistName = true;
            } else{
                // Display error if password is too short
                etFirstName.setError(getText(R.string.error_username));
                bFistName = false;
            }

            if (sLastName.length()>0){
                bLastName = true;
            } else{
                // Display error if password is too short
                etLastname.setError(getText(R.string.error_username));
                bLastName = false;
            }

            // Enables register button if required fields are filled
            if (bEmail && bUsername && bPassword && bFistName && bLastName) {
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
        etFirstName = findViewById(R.id.et_firstname);
        etLastname = findViewById(R.id.et_lastname);

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

        chatDbHelper = new ChatDbHelper(this);
    }

    @Override
    public void onClick(View view) {
        // Starts contacts activity if register button is pressed

        if (view.getId() == R.id.btn_new_register){
            int found = 0;

            ContactClass[] contacts = chatDbHelper.readContacts();

            if (contacts != null) {
                for (int i = 0; i < contacts.length; i++) {
                    if (contacts[i].getTvUserName().compareTo(etUsername.getText().toString()) == 0){
                        found = 1;
                        break;
                    }
                }
            }

            if (found == 1){
                Toast.makeText(this, getText(R.string.error_user_exist), Toast.LENGTH_SHORT).show();
            } else{
                ContactClass contact = new ContactClass(etFirstName.getText().toString(), etLastname.getText().toString(),
                        etUsername.getText().toString());
                chatDbHelper.insert_contacts(contact);
                Intent MainActivity_intent = new Intent (RegisterActivity.this, MainActivity.class);
                startActivity(MainActivity_intent);
            }
        }
    }
}
