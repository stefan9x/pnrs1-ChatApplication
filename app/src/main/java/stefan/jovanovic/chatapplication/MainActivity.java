package stefan.jovanovic.chatapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private int backbtn_counter;

    private ChatDbHelper chatDbHelper;

    public static final String MY_PREFS_NAME = "PrefsFile";

    public TextWatcher twLogin = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable e) {

            String sUsername = etUsername.getText().toString();
            String sPassword = etPassword.getText().toString();

            boolean bUsername, bPassword;

            // check if username and password fields are filled correctly
            //  to enable Login button
            if (sUsername.length() > 0)  {
                bUsername = true;
            } else {
                bUsername = false;
                etUsername.setError(getText(R.string.error_username));
            }

            if (sPassword.length() > 5) {
                bPassword = true;
            } else {
                bPassword = false;
                etPassword.setError(getText(R.string.error_password_minimum));
            }

            if (bUsername && bPassword){
                btnLogin.setEnabled(true);
            } else {
                btnLogin.setEnabled(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPassword = findViewById(R.id.et_password);
        etUsername = findViewById(R.id.et_username);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        // Adds text watchers on username and password fields
        etUsername.addTextChangedListener(twLogin);
        etPassword.addTextChangedListener(twLogin);

        // Adds login and register buttons listeners
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        // Disables login button on activity create
        btnLogin.setEnabled(false);

        chatDbHelper = new ChatDbHelper(this);
    }

    @Override
    public void onClick(View view) {
        //Starting register activity with register button
        if (view.getId() == R.id.btn_register) {
            Intent RegisterActivity_intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(RegisterActivity_intent);
        }

        //Starting contacts activity with login button
        if (view.getId() == R.id.btn_login){
            Intent ContactsActivity_intent = new Intent(MainActivity.this, ContactsActivity.class);
            ContactClass[] contacts = chatDbHelper.readContacts();
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

            int found = 0;

            if (contacts != null) {
                for (int i = 0; i < contacts.length; i++) {
                    if ((contacts[i].getsUserName().compareTo(etUsername.getText().toString())) == 0) {
                        editor.putString("userId", contacts[i].getsId());
                        found = 1;
                    }
                }
            }

            editor.apply();

            if (found == 1){
                startActivity(ContactsActivity_intent);
            }
            else{
                Toast.makeText(this, getText(R.string.error_user_not_found), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        //Display toast message if user try to leave main activity with back button
        if(backbtn_counter >= 1) {
            Intent Exit_intent = new Intent(Intent.ACTION_MAIN);
            Exit_intent.addCategory(Intent.CATEGORY_HOME);
            Exit_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Exit_intent);
        }
        else {
            Toast.makeText(this, getText(R.string.main_activity_backbtn_toast), Toast.LENGTH_SHORT).show();
            backbtn_counter++;
        }
    }
}
