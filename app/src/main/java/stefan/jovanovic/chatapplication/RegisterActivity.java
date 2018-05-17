package stefan.jovanovic.chatapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;


public class RegisterActivity extends Activity implements View.OnClickListener {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etFirstName;
    private EditText etLastname;
    private EditText etEmail;
    private Button btnRegister;
    private DatePicker dpDatepicker;

    private HttpHelper httphelper;
    private Handler handler;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String REGISTER_URL = BASE_URL + "/register";

    public static final String MY_PREFS_NAME = "PrefsFile";

    public TextWatcher twRegister = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable e) {


            String sPassword = etPassword.getText().toString();
            String sEmail = etEmail.getText().toString();
            String sFistName = etFirstName.getText().toString();
            String sLastName = etLastname.getText().toString();
            String sUsername = etUsername.getText().toString();

            boolean bUsername, bPassword, bEmail, bFistName, bLastName;

            // Checks if users entered email in correct format
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
                bEmail = true;
            } else {
                // Display error if email is incorrectly typed
                etEmail.setError(getText(R.string.error_email));
                bEmail = false;
            }

            // Checks if username is entered
            if (sUsername.length() > 0) {
                bUsername = true;
            } else {
                bUsername = false;
                etUsername.setError(getText(R.string.error_username));
            }

            // Checks if password with minimum of 6 characters is entered
            if (sPassword.length() > 5) {
                bPassword = true;
            } else {
                // Display error if password is too short
                etPassword.setError(getText(R.string.error_password_minimum));
                bPassword = false;
            }

            if (sFistName.length() > 0) {
                bFistName = true;
            } else {
                // Display error if password is too short
                etFirstName.setError(getText(R.string.error_name));
                bFistName = false;
            }

            if (sLastName.length() > 0) {
                bLastName = true;
            } else {
                // Display error if password is too short
                etLastname.setError(getText(R.string.error_last_name));
                bLastName = false;
            }

            // Enables register button if required fields are filled
            if (bEmail && bUsername && bPassword && bFistName && bLastName) {
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

        httphelper = new HttpHelper();
        handler = new Handler();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_new_register:
                new Thread(new Runnable() {
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("username", etUsername.getText().toString());
                            jsonObject.put("password", etPassword.getText().toString());
                            jsonObject.put("email", etEmail.getText().toString());

                            final boolean response = httphelper.registerUserOnServer(RegisterActivity.this, REGISTER_URL, jsonObject);

                            handler.post(new Runnable(){
                                public void run() {
                                    if (response) {
                                        Toast.makeText(RegisterActivity.this, getText(R.string.success_user_register), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    } else {
                                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                        String registerErr = prefs.getString("registerErr", null);
                                        Toast.makeText(RegisterActivity.this, registerErr, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;

        }
    }
}
