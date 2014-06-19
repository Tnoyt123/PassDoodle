package com.grimnirdesigns.passdoodle_unsecure.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.grimnirdesigns.passdoodle_unsecure.app.R;

public class LoggedInActivity extends Activity {

    private UserFunctions mUserFunctions;

    private Button mLogOutButton, mSendEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        mUserFunctions = new UserFunctions();

        mLogOutButton = (Button)findViewById(R.id.activity_logged_in_LogOutButton);
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserFunctions.logoutUser(getApplicationContext());

                Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(i);
            }
        });

        mSendEmailButton = (Button)findViewById(R.id.activity_logged_in_SendEmailButton);

    }



}
