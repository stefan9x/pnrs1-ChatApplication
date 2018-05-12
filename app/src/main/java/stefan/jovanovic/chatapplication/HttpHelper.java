package stefan.jovanovic.chatapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;


public class HttpHelper {

    private static final int SUCCESS = 200;
    public static final String MY_PREFS_NAME = "PrefsFile";

    public boolean registerUserOnServer(String urlString, JSONObject jsonObject) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);

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

        urlConnection.disconnect();

        return (responseCode==SUCCESS);
    }

    public boolean logInUserOnServer(Context login_context, String urlString, JSONObject jsonObject) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);

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

        String sessionid = urlConnection.getHeaderField("sessionid");

        urlConnection.disconnect();

        if(responseCode==SUCCESS) {
            SharedPreferences.Editor editor = login_context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("sessionId", sessionid);
            editor.apply();
        } /*else {
            SharedPreferences.Editor editor = login_context.getSharedPreferences(MY_PREFS_NAME, login_context.MODE_PRIVATE).edit();
            editor.putString("error_msg", str_err);
            editor.apply();
        }*/

        return (responseCode==SUCCESS);
    }

    public JSONArray getContactsFromServer(Context contacts_context, String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();


        SharedPreferences prefs = contacts_context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String loggedin_userId = prefs.getString("sessionId", null);

        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("sessionid", loggedin_userId);
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

        urlConnection.disconnect();

        return responseCode == SUCCESS ? new JSONArray(jsonString) : null;
    }

    public boolean logOutUserFromServer(Context contacts_context, String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);

        SharedPreferences prefs = contacts_context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String loggedin_userId = prefs.getString("sessionId", null);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("sessionid", loggedin_userId);
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

        urlConnection.disconnect();

        return (responseCode==SUCCESS);
    }
}
