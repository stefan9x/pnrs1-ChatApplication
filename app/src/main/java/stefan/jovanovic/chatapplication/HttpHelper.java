package stefan.jovanovic.chatapplication;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;


public class HttpHelper {

    private static final int SUCCESS = 200;
    public static final String MY_PREFS_NAME = "PrefsFile";
    private static String BASE_URL = "http://18.205.194.168:80";
    private static String CONTACTS_URL = BASE_URL + "/contacts";
    private static String LOGIN_URL = BASE_URL + "/login";
    private static String LOGOUT_URL = BASE_URL + "/logout";
    private static String GET_MESSAGE_URL = BASE_URL + "/message/";
    private static String POST_MESSAGE_URL = "/message";
    private static String DELETE_CONTACT_URL = BASE_URL + "/contact/";
    private static String REGISTER_URL = BASE_URL + "/register";
    private static String DELETE_MESSAGE = BASE_URL + "/message";
    private static String GET_NOTIFICATION_URL = BASE_URL + "/getfromservice";

    public boolean registerUserOnServer(Context context, JSONObject jsonObject) throws IOException{

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(REGISTER_URL);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();


        if(responseCode!=SUCCESS) {
            SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            String responseMsg = urlConnection.getResponseMessage();
            String registerErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("registerErr", registerErr);
            editor.apply();
        }

        urlConnection.disconnect();

        return (responseCode==SUCCESS);
    }

    public boolean logInUserOnServer(Context context, JSONObject jsonObject) throws IOException{

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(LOGIN_URL);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();

        String sessionId = urlConnection.getHeaderField("sessionid");

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        if(responseCode==SUCCESS) {
            editor.putString("sessionId", sessionId);
            editor.apply();
        } else {
            String responseMsg = urlConnection.getResponseMessage();
            String loginErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("loginErr", loginErr);
            editor.apply();
        }

        urlConnection.disconnect();
        return (responseCode==SUCCESS);
    }

    public JSONArray getContactsFromServer(Context context) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(CONTACTS_URL);
        urlConnection = (HttpURLConnection) url.openConnection();


        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("sessionid", sessionId);
        //urlConnection.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }

        br.close();

        String jsonString = sb.toString();

        int responseCode =  urlConnection.getResponseCode();
        String responseMsg = urlConnection.getResponseMessage();

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        urlConnection.disconnect();

        if (responseCode == SUCCESS){
            return new JSONArray(jsonString);
        } else {
            String getContactsErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("getContactsErr", getContactsErr);
            editor.apply();
            return null;
        }

    }

    public boolean logOutUserFromServer(Context context) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(LOGOUT_URL);

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        int responseCode =  urlConnection.getResponseCode();

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        if(responseCode!=SUCCESS) {
            String responseMsg = urlConnection.getResponseMessage();
            String logoutErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("logoutErr", logoutErr);
            editor.apply();
        }

        urlConnection.disconnect();
        return (responseCode==SUCCESS);
    }

    public boolean sendMessageToServer(Context context, JSONObject jsonObject) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(POST_MESSAGE_URL);

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        if(responseCode!=SUCCESS) {
            String responseMsg = urlConnection.getResponseMessage();
            String sendMsgErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("sendMsgErr", sendMsgErr);
            editor.apply();
        }

        urlConnection.disconnect();
        return (responseCode==SUCCESS);
    }

    public JSONArray getMessagesFromServer(Context context, String contact) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(GET_MESSAGE_URL + contact);
        urlConnection = (HttpURLConnection) url.openConnection();


        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }

        br.close();

        String jsonString = sb.toString();

        int responseCode =  urlConnection.getResponseCode();
        String responseMsg = urlConnection.getResponseMessage();

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        urlConnection.disconnect();

        if (responseCode == SUCCESS){
            return new JSONArray(jsonString);
        } else {
            String getMessagesErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("getMessagesErr", getMessagesErr);
            editor.apply();
            return null;
        }
    }

    /*HTTP delete*/
    public boolean deleteUserFromServer(Context context, String contact) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(DELETE_CONTACT_URL + contact);
        urlConnection = (HttpURLConnection) url.openConnection();

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);


        urlConnection.setRequestMethod("DELETE");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        int responseCode =  urlConnection.getResponseCode();



        if (responseCode != SUCCESS) {
            SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            String responseMsg = urlConnection.getResponseMessage();
            String deleteContactErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("deleteContactErr", deleteContactErr);
            editor.apply();
        }

        urlConnection.disconnect();

        return (responseCode==SUCCESS);
    }

    /*HTTP delete*/
    public boolean deleteMessageFromServer(Context context, JSONObject jsonObject) throws IOException, JSONException {

        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(DELETE_MESSAGE);
        urlConnection = (HttpURLConnection) url.openConnection();

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        urlConnection.setRequestMethod("DELETE");
        urlConnection.setRequestProperty("sessionid",sessionId);
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
        os.writeBytes(jsonObject.toString());

        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();

        if (responseCode != SUCCESS) {
            SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            String responseMsg = urlConnection.getResponseMessage();
            String deleteMsgErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("deleteMsgErr", deleteMsgErr);
            editor.apply();
        }

        urlConnection.disconnect();
        return (responseCode==SUCCESS);
    }

    /*HTTP getNotification*/
    public boolean getNotification(Context context) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(GET_NOTIFICATION_URL);
        urlConnection = (HttpURLConnection) url.openConnection();

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);


        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");
/*
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }
*/
        //int responseCode =  urlConnection.getResponseCode();

        urlConnection.disconnect();
        return true;
        //return (responseCode==SUCCESS);
    }

    public boolean checkServer() throws IOException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(BASE_URL);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Connection", "close");
        urlConnection.setConnectTimeout(2000 /* milliseconds */ );

        try {
            urlConnection.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
