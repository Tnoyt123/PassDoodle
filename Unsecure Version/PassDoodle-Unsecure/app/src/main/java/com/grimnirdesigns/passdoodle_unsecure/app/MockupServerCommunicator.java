package com.grimnirdesigns.passdoodle_unsecure.app;

import android.util.Log;

/*
This class acts as a stand-in for an offline version of ServerCommunicator.
This class, in conjunction with MockupServer, will be used to test the theoretical functionality of
PassDoodle, as well as assisting in the development of the interface of ServerCommunicator. In the
final build, this class will be replaced with ServerCommunicator, and all public methods will
maintain the same signature.
 */
public class MockupServerCommunicator {

    private boolean mConnectionOpen;
    private MockupServer mMockupServer;

    public MockupServerCommunicator() {
        openConnection();
    }

    /*
    Establishes connection to the MockupServer
     */
    public void openConnection() {
        mMockupServer = MockupServer.get();
        mConnectionOpen = true;
    }

    /*
    Checks that the MockupServer has been initially connected to, and that the connection is
    still available.
     */
    public boolean isConnectionOpen() {
        return mConnectionOpen;
    }

    /*
    Checks if a username is already in the server's database. Returns true if the user name is used,
    false otherwise.
     */
    public boolean checkForUsername(String username) {
        return mMockupServer.queryUsername(username);
    }

    /*
    Attempts to register a username and PassDoodle(in the form of a String of the normalized points
    of the drawn Path. Returns true if the registration succeeds, false otherwise.
     */
    public boolean registerUser(String username, String normalizedPath) {
        return mMockupServer.registerUser(username, normalizedPath);
    }

    /*
    Checks the server's database for an input username. If the username is in the system, the server
    will then check that the input normalized points lie within the tolerance range of the saved
     points. Returns true if the username exists, and the points match up; false otherwise.
     */
    public boolean userLogin(String username, String normalizedPath) {
        Log.d("MockupServerCommunicator", "begin userLogin");
        return mMockupServer.loginUser(username, normalizedPath);
    }

    /*
    Performs any necessary actions to close the connection to the server.
     */
    public void closeConnection() {

    }

    public int getCount() {
        return mMockupServer.getCount();
    }
}
