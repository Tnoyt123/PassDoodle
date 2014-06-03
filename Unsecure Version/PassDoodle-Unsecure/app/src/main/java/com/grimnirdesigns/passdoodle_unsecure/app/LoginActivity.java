package com.grimnirdesigns.passdoodle_unsecure.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.test.suitebuilder.TestSuiteBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
This activity allows for "logging in", which is simply the verification that a given username and
PassDoodle combination exist within the system.
 */

public class LoginActivity extends Activity {

    public static final int PATH_STEPS = 100;

    private MockupServerCommunicator mServerCommunicator;

    private View mScreenView;
    private EditText mUserNameEditText;
    private DrawingBoardView mDrawingBoardView;
    private Button mClearDoodleButton, mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mServerCommunicator = new MockupServerCommunicator();

        mScreenView = findViewById(R.id.activity_login_ScreenView);
        mScreenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        mUserNameEditText = (EditText)findViewById(R.id.activity_login_UserNameEditText);

        mDrawingBoardView = (DrawingBoardView)findViewById(R.id.activity_login_DrawingBoardView);

        mClearDoodleButton = (Button)findViewById(R.id.activity_login_ClearDoodleButton);
        mClearDoodleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDoodle();
            }
        });

        mSubmitButton = (Button)findViewById(R.id.activity_login_SubmitButton);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitLogin();
            }
        });
    }

    // Stub method until server mockup is ready
    private void submitLogin() {
        Log.d("submitLogin", "begin login");
        if (!mDrawingBoardView.isDrawingEnabled()) {
            Path drawnPath = mDrawingBoardView.getDrawPath();
            String normalizedPath = normalizePath(drawnPath, mDrawingBoardView.getWidth());

            String username = mUserNameEditText.getText().toString().trim();

            if (username == null || username == "") {
                Toast.makeText(this, "Please enter your username.", Toast.LENGTH_LONG).show();
                return;
            }
            if (mServerCommunicator.isConnectionOpen()) {
                if(mServerCommunicator.userLogin(username, normalizedPath)) {
                    Toast.makeText(this, "LOGIN SUCCESSFUL!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Toast.makeText(this, "Login failed.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        else {
            Toast.makeText(this, "Please enter your PassDoodle.", Toast.LENGTH_LONG).show();
        }
    }

    private String normalizePath(Path path, int viewWidth) {
        PathMeasure pm = new PathMeasure(path, false);

        float pathLength = pm.getLength();
        float pathDivision = pathLength / PATH_STEPS;

        String output = new String();

        for (int i = 0; i < PATH_STEPS; i++) {
            float[] point = new float[2];
            pm.getPosTan(i*pathDivision,point,null);

            double xPos, yPos;
            xPos = (double)Math.round((double)(point[0])/viewWidth*1000) / 1000;
            yPos = (double)Math.round((double)(point[1])/viewWidth*1000) / 1000;

            output += xPos + "," + yPos + ";";
        }

        return output;
    }

    private void clearDoodle() {
        if (mDrawingBoardView != null) {
            mDrawingBoardView.resetDrawPath();
            mDrawingBoardView.clearDrawing();
        }
    }

}
