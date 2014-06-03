package com.grimnirdesigns.passdoodle_unsecure.app;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Tony on 6/2/2014.
 */
public class MockupServer {
    private static MockupServer sMockupServer = null;
    private ArrayList<UserTuple> mUserTuples; //Database stand-in
    private final double ACCURACY = 0.05;

    private MockupServer() {
        mUserTuples = new ArrayList<UserTuple>();
    }

    public static MockupServer get() {
        if (sMockupServer == null) {
            sMockupServer = new MockupServer();
        }
        return sMockupServer;
    }

    public boolean establishConnection() {
        return true; //Connection established
    }

    public boolean queryUsername(String userName) {
        for (UserTuple tuple : mUserTuples) {
            if (tuple.getUserName() == userName) return true;
        }
        return false;
    }

    public boolean registerUser(String username, String passDoodle) {
        UserTuple tuple = new UserTuple(username, passDoodle);
        mUserTuples.add(tuple);
        Log.d("MockupServer", "Added " + username);
        return true;
    }

    public int getCount() {
        return mUserTuples.size();
    }

    public boolean loginUser(String username, String passDoodle) {
        Log.d("MockupServer", "begin loginUser");
        UserTuple queriedTuple = null;
        for (UserTuple tuple : mUserTuples) {
            if (tuple.getUserName().equals(username)) queriedTuple = tuple;
        }
        if (queriedTuple == null) {
            Log.d("MockupServer", "queriedTuple null");
            return false;
        }
        return comparePassDoodles(queriedTuple.getPassDoodle(), passDoodle);
    }

    private boolean comparePassDoodles(String savedPassDoodle, String inputPassDoodle) {
        String[] savedPassDoodlePairs = savedPassDoodle.split(";");
        String[] inputPassDoodlePairs = inputPassDoodle.split(";");
        for (int i = 0; i < 100; i++) {
            String[] savedCoords = savedPassDoodlePairs[i].split(",");
            String[] inputCoords = inputPassDoodlePairs[i].split(",");

            double savedX = Double.parseDouble(savedCoords[0]);
            double savedY = Double.parseDouble(savedCoords[1]);

            double inputX = Double.parseDouble(inputCoords[0]);
            double inputY = Double.parseDouble(inputCoords[1]);

            Log.d("CompareCoords", savedX + "," + savedY + ";" + inputX + "," + inputY);
            if (Math.sqrt(Math.pow((inputX-savedX),2) + Math.pow((inputY-savedY),2)) > ACCURACY) {
                return false;
            }
        }
        return true;
    }

    private class UserTuple {
        private String mUserName;
        private String mPassDoodle;

        public UserTuple(String userName, String passDoodle) {
            mUserName = userName;

            mPassDoodle = passDoodle;
        }

        public String getUserName() {
            return mUserName;
        }

        public String getPassDoodle() {
            return mPassDoodle;
        }
    }
}
