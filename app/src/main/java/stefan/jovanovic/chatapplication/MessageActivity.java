package stefan.jovanovic.chatapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

public class MessageActivity extends Activity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private Button btnLogout;
    private Button btnSend;
    private Button btnRefresh;
    private EditText etMessage;
    private TextView tvContactname;
    private ListView lvMessages;

    private MessageListAdapter messageListAdapter = new MessageListAdapter(this);

    private static final String MY_PREFS_NAME = "PrefsFile";
    private String receiverUsername;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String POST_MESSAGE_URL = BASE_URL + "/message";
    private static String GET_MESSAGE_URL = BASE_URL + "/message/";
    private static String LOGOUT_URL = BASE_URL + "/logout";

    public MessageClass[] messageClass;

    private HttpHelper httphelper;
    private Handler handler;

    private TextWatcher twSend = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable e) {

            String sMessage = etMessage.getText().toString();

            // Disables send button if message is not entered
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

        // Contact name in upper corner
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        receiverUsername = prefs.getString("receiverUsername", null);

        btnLogout = findViewById(R.id.btn_logout_message);
        btnSend = findViewById(R.id.btn_send);
        btnRefresh = findViewById(R.id.btn_refresh_message);
        etMessage = findViewById(R.id.et_message);
        tvContactname = findViewById(R.id.chatting_with);
        lvMessages = findViewById(R.id.messages_list);

        tvContactname.setText(receiverUsername);

        // Setting adapter to list
        lvMessages.setAdapter(messageListAdapter);

        // On item long click listener
        //lvMessages.setOnItemLongClickListener(this);

        // Disables send button on message activity create
        btnSend.setEnabled(false);

        // Adds text watcher to message field
        etMessage.addTextChangedListener(twSend);

        // Adds listeners on logout and send buttons
        btnLogout.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);

        httphelper = new HttpHelper();

        handler = new Handler();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Update messages list
        updateMessagesList();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_logout_message:
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            final boolean response = httphelper.logOutUserFromServer(MessageActivity.this, LOGOUT_URL);
                            handler.post(new Runnable(){
                                public void run() {
                                    if (response) {
                                        startActivity(new Intent(MessageActivity.this, LoginActivity.class));
                                    } else {
                                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                        String logoutErr = prefs.getString("logoutErr", null);
                                        Toast.makeText(MessageActivity.this, logoutErr, Toast.LENGTH_SHORT).show();
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

            case R.id.btn_refresh_message:
                updateMessagesList();
                break;

            case R.id.btn_send:
                new Thread(new Runnable() {
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("receiver", receiverUsername);
                            jsonObject.put("data", etMessage.getText().toString());

                            final boolean success = httphelper.sendMessageToServer(MessageActivity.this, POST_MESSAGE_URL, jsonObject);

                            handler.post(new Runnable(){
                                public void run() {
                                    if (success) {
                                        Toast.makeText(MessageActivity.this, getText(R.string.message_sent), Toast.LENGTH_SHORT).show();
                                        etMessage.getText().clear();
                                        updateMessagesList();
                                    } else {
                                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                        String sendMsgErr = prefs.getString("sendMsgErr", null);
                                        Toast.makeText(MessageActivity.this, sendMsgErr, Toast.LENGTH_SHORT).show();
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

    public void updateMessagesList() {
        new Thread(new Runnable() {

            public void run() {
                try {
                    final JSONArray messages = httphelper.getMessagesFromServer(MessageActivity.this, GET_MESSAGE_URL+receiverUsername);

                    handler.post(new Runnable(){
                        public void run() {
                            if (messages != null) {

                                JSONObject json_message;
                                messageClass = new MessageClass[messages.length()];

                                for (int i = 0; i < messages.length(); i++) {
                                    try {
                                        json_message = messages.getJSONObject(i);
                                        messageClass[i] = new MessageClass(json_message.getString("sender"),json_message.getString("data"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                messageListAdapter.update(messageClass);
                            } else {
                                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                String getMessagesErr = prefs.getString("getMessagesErr", null);
                                Toast.makeText(MessageActivity.this, getMessagesErr, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        /*final MessageClass message = (MessageClass) messagelistadapter.getItem(position);

        // Check if user is deleting his messages, if not, show toast
        if (message.getsSenderId().compareTo(sender_userid) == 0) {
            // Delete confirmation dialog
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getText(R.string.dialog_delete_title));
            alert.setMessage(getText(R.string.dialog_delete_message_confirmation_text));

            alert.setPositiveButton(getText(R.string.dialog_delete_positive_btn), new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Deleting message from database
                    //chatDbHelper.deleteMessage(message.getsMessageId());

                    // Updating messages list
                    //updateMessagesList(sender_userid, receiver_userid);
                }
            });

            alert.setNegativeButton(getText(R.string.dialog_delete_negative_btn), new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            alert.show();

            return true;
        } else {
            Toast.makeText(this, getText(R.string.error_delete_only_your_messages), Toast.LENGTH_SHORT).show();
            return false;
        }*/
        return false;
    }
}
