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

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private int backbtn_counter;

    public TextWatcher twLogin = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable e) {

            String sUsername = etUsername.getText().toString();
            String sPassword = etPassword.getText().toString();

            // check if username and password fields are filled correctly
            //  to enable Login button
            if (sUsername.length() > 0 && sPassword.length() > 5) {
                btnLogin.setEnabled(true);
            }
            else {
                btnLogin.setEnabled(false);
                etPassword.setError(getText(R.string.error_email));
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
            startActivity(ContactsActivity_intent);
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
