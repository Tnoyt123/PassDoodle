package com.grimnirdesigns.passdoodle_unsecure.app;

/*
This class acts as a stand-in for an offline version of ServerCommunicator.
This class, in conjunction with MockupServer, will be used to test the theoretical functionality of
PassDoodle, as well as assisting in the development of the interface of ServerCommunicator. In the
final build, this class will be replaced with ServerCommunicator, and all public methods will
maintain the same signature.
 */
public class MockupServerCommunicator {

    private boolean mConnectionOpen;

    public MockupServerCommunicator() {
        mConnectionOpen = false;
    }

    /*
    Establishes connection to the MockupServer
     */
    public void openConnection() {

    }

    /*
    Checks that the MockupServer has been initially connected to, and that the connection is
    still available.
     */
    public boolean isConnectionOpen() {
        return false;
    }

    /*
    Checks if a username is already in the server's database. Returns true if the user name is used,
    false otherwise.
     */
    public boolean checkForUsername(String username) {
        return false;
    }

    /*
    Attempts to register a username and PassDoodle(in the form of a String of the normalized points
    of the drawn Path. Returns true if the registration succeeds, false otherwise.
     */
    public boolean registerUser(String username, String normalizedPath) {
        return false;
    }

    /*
    Checks the server's database for an input username. If the username is in the system, the server
    will then check that the input normalized points lie within the tolerance range of the saved
     points. Returns true if the username exists, and the points match up; false otherwise.
     */
    public boolean userLogin(String username, String normalizedPath) {
        return false;
    }

    /*
    Performs any necessary actions to close the connection to the server.
     */
    public void closeConnection() {

    }

}
