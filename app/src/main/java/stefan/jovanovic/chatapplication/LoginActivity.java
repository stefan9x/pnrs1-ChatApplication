package stefan.jovanovic.chatapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private int backbtn_counter;

    //private ChatDbHelper chatDbHelper;

    public static final String MY_PREFS_NAME = "PrefsFile";
    public Context login_context;

    private HttpHelper httphelper;
    private Handler handler;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String POST_URL = BASE_URL + "/login";

    public TextWatcher twLogin = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable e) {

            String sUsername = etUsername.getText().toString();
            String sPassword = etPassword.getText().toString();

            boolean bUsername, bPassword;

            // check if username and password fields are filled correctly
            //  to enable Login button
            if (sUsername.length() > 0) {
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

            if (bUsername && bPassword) {
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

        //chatDbHelper = new ChatDbHelper(this);

        login_context = this;

        httphelper = new HttpHelper();

        handler = new Handler();
    }

    @Override
    public void onClick(View view) {
        //Starting register activity with register button
        if (view.getId() == R.id.btn_register) {
            Intent RegisterActivity_intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(RegisterActivity_intent);
        }

        //Starting contacts activity with login button
        if (view.getId() == R.id.btn_login) {

            new Thread(new Runnable() {
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", etUsername.getText().toString());
                        jsonObject.put("password", etPassword.getText().toString());

                        final boolean success = httphelper.logInUserOnServer(login_context, POST_URL, jsonObject);

                        handler.post(new Runnable(){
                            public void run() {
                                if (!success) {
                                    Toast.makeText(LoginActivity.this, getText(R.string.error_user_not_found), Toast.LENGTH_SHORT).show();
                                } else {
                                    SharedPreferences.Editor editor = login_context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("leggedin_username", etUsername.getText().toString());
                                    editor.apply();

                                    Intent LoginActivity_intent = new Intent(LoginActivity.this, ContactsActivity.class);
                                    startActivity(LoginActivity_intent);
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
        }
    }

    // Double press back button to exit app
    @Override
    public void onBackPressed() {
        //Display toast message if user try to leave main activity with back button
        if (backbtn_counter >= 1) {
            Intent Exit_intent = new Intent(Intent.ACTION_MAIN);
            Exit_intent.addCategory(Intent.CATEGORY_HOME);
            Exit_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Exit_intent);
        } else {
            Toast.makeText(this, getText(R.string.main_activity_backbtn_toast), Toast.LENGTH_SHORT).show();
            backbtn_counter++;
        }
    }
}
