package com.grimnirdesigns.passdoodle_secure.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.Toast;


public class LoggingInActivity extends Activity implements LogInFragment.OnSuccessfulLoginListener, LoggedInFragment.OnLoggedInFragmentInteractionListener{

    private UserFunctions mUserFunctions;

    private String mUsername, mEmail, mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging_in);

        mUserFunctions = new UserFunctions();

        FragmentManager fm = getFragmentManager();
        LogInFragment fragment = LogInFragment.newInstance();

        fm.beginTransaction().add(R.id.activity_logging_in_FragmentContainer, fragment).commit();
    }

    @Override
    public void onSuccessfulLogin(String username, String name, String email) {
        mUsername = username;
        mName = name;
        mEmail = email;

        DatabaseHandler db = new DatabaseHandler(this);
        mUserFunctions.logoutUser(this);
        db.addUser(username, name, email);

        LoggedInFragment fragment = LoggedInFragment.newInstance(username, name, email);
        FragmentManager fm = getFragmentManager();

        fm.beginTransaction().replace(R.id.activity_logging_in_FragmentContainer, fragment).commit();
    }

    @Override
    public void onUnsuccessfulLogin() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onLogout() {
        mUsername = null;
        mName = null;
        mEmail = null;

        mUserFunctions.logoutUser(this);
        finish();
    }

    @Override
    public void onSendEmail() {
    }

    @Override
    public void onEmailFailure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Email failed to send", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEmailSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Email Sent!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
