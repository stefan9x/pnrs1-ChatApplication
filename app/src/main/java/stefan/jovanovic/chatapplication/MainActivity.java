package stefan.jovanovic.chatapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;

    public TextWatcher etTextwatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable e) {

            String sUsername = etUsername.getText().toString();
            String sPassword = etPassword.getText().toString();

            if (sUsername.length() > 0 && sPassword.length() > 5) {
                btnLogin.setEnabled(true);
            } else {
                btnLogin.setEnabled(false);
                etPassword.setError("Minimum 6 characters");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etPassword = findViewById(R.id.password);
        etUsername = findViewById(R.id.username);
        btnLogin = findViewById(R.id.login);
        if (etUsername.length() == 0 && etPassword.length() == 0){
            btnLogin.setEnabled(false);
        }
        etUsername.addTextChangedListener(etTextwatcher);
        etPassword.addTextChangedListener(etTextwatcher);

        btnRegister = findViewById(R.id.register);
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register) {
            Intent intRegisteractivity = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intRegisteractivity);
        }
        if (view.getId() == R.id.login){
            Intent intContactsactivity = new Intent(MainActivity.this, ContactsActivity.class);
            startActivity(intContactsactivity);
        }
    }
}
