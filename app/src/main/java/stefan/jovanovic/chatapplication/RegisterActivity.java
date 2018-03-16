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

import java.security.AccessController;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends Activity implements View.OnClickListener{

    private EditText etUsername;
    private EditText etPassword;
    private EditText etName;
    private EditText etLastname;
    private EditText etEmail;
    private Button btnRegister;
    private DatePicker dpDatepicker;

    public TextWatcher etTextwatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable e) {

            String sUsername = etUsername.getText().toString();
            String sPassword = etPassword.getText().toString();
            String sName = etName.getText().toString();
            String sLastname = etLastname.getText().toString();
            String sEmail = etEmail.getText().toString();

            boolean bUsername, bPassword, bName, bLastname, bEmail;

            if(android.util.Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()){
                bEmail = true;
            }else{
                etEmail.setError("invalid email");
                bEmail = false;
            }

            if (sUsername.length()>0){
                bUsername = true;
            }else{
                bUsername = false;
            }

            if (sPassword.length()>5){
                bPassword = true;
            }else{
                etPassword.setError("Minimum 6 characters");
                bPassword = false;
            }

            if (sName.length()>0){
                bName = true;
            }else{
                bName = false;
            }

            if (sLastname.length()>0){
                bLastname = true;
            }else{
                bLastname = false;
            }

            if (bEmail && bUsername && bPassword && bName && bLastname) {
                btnRegister.setEnabled(true);
            } else {
                btnRegister.setEnabled(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dpDatepicker = findViewById(R.id.datePicker);
        dpDatepicker.setMaxDate(new Date().getTime());
        etPassword = findViewById(R.id.new_password);
        etUsername = findViewById(R.id.new_username);
        etName = findViewById(R.id.name);
        etLastname = findViewById(R.id.lastname);
        etEmail = findViewById(R.id.email);
        btnRegister = findViewById(R.id.new_register);
        if (etUsername.length() == 0){
            btnRegister.setEnabled(false);
        }
        etUsername.addTextChangedListener(etTextwatcher);
        etPassword.addTextChangedListener(etTextwatcher);
        etName.addTextChangedListener(etTextwatcher);
        etLastname.addTextChangedListener(etTextwatcher);
        etEmail.addTextChangedListener(etTextwatcher);

    }

    @Override
    public void onClick(View view) {

    }

}
