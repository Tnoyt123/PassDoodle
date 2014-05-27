package com.grimnirdesigns.passdoodle_unsecure.app;

import android.app.Activity;
import android.graphics.Path;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RegisterUserActivity extends Activity {

    private Button mClearDoodleButton, mSubmitButton;
    private DrawingBoardView mDrawingBoardView;
    private EditText mUserNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mClearDoodleButton = (Button)findViewById(R.id.activity_register_user_ClearDoodleButton);
        mClearDoodleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDoodle();
            }
        });

        mSubmitButton = (Button)findViewById(R.id.activity_register_user_SubmitButton);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRegistration();
            }
        });

        mDrawingBoardView = (DrawingBoardView)findViewById(R.id.activity_register_user_DrawingBoardView);

        mUserNameEditText = (EditText)findViewById(R.id.activity_register_user_UserNameEditText);
    }

    private void clearDoodle() {
        mDrawingBoardView.resetDrawPath();
        mDrawingBoardView.clearDrawing();
    }

    private void submitRegistration() {

    }

    private String pathToString(Path path) {

        return null;
    }
}
