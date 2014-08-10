package com.grimnirdesigns.passdoodle_secure.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


public class RegisterActivity extends Activity implements RegisterFragment.OnRegisterFragmentInteractionListener{

    private UserFunctions mUserFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserFunctions = new UserFunctions();
        setContentView(R.layout.activity_register);

        FragmentManager fm = getFragmentManager();
        RegisterFragment fragment = RegisterFragment.newInstance();

        fm.beginTransaction().add(R.id.activity_register_FragmentContainer, fragment).commit();
    }

    @Override
    public void onSuccessfulRegistrationInteraction() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Registration Successful!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), LoggingInActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onUnsuccessfulRegistrationInteraction(final String errorMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Registration Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
