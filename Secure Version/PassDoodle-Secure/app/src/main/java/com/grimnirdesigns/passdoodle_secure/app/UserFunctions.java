package com.grimnirdesigns.passdoodle_secure.app;

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UserFunctions {

    private JSONParser mJSONParser;

    //private static String loginURL = "69.195.124.111";
    //private static String registerURL = "69.195.124.111";

    private static String loginURL = "http://www.grimnirdesigns.com/passdoodle/secure";
    private static String registerURL = "http://www.grimnirdesigns.com/passdoodle/secure";
    private static String sendEmailURL = "http://www.grimnirdesigns.com/passdoodle/secure";

    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String send_email_tag = "send_email";

    public UserFunctions() {
        mJSONParser = new JSONParser();
    }

    public JSONObject loginUser(String username, String passdoodle) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("passdoodle", passdoodle));

        JSONObject json = mJSONParser.getJSONFromUrl(loginURL, params);

        return json;
    }

    public JSONObject registerUser(String username, String passdoodle, String name, String email) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("passdoodle", passdoodle));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));

        JSONObject json = mJSONParser.getJSONFromUrl(registerURL, params);

        return json;
    }

    public JSONObject sendEmail(String username, String email) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", send_email_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("email", email));

        JSONObject json = mJSONParser.getJSONFromUrl(sendEmailURL, params);
        return json;
    }

    public boolean isUserLoggedIn(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if (count > 0) {
            // logged in
            return true;
        }
        return false;
    }

    public boolean logoutUser(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}
