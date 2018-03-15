package stefan.jovanovic.chatapplication;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

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
    }


}
