package com.grimnirdesigns.passdoodle_unsecure.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;


public class RegisterUserActivity extends Activity {

    public static final int PATH_STEPS = 100;

    private Button mClearDoodleButton, mSubmitButton;
    private DrawingBoardView mDrawingBoardView;
    private EditText mUserNameEditText;
    private View mScreenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mScreenView = findViewById(R.id.activity_register_user_ScreenView);
        mScreenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

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

    //Stub method until server mockup is ready
    private void submitRegistration() {
        if (!mDrawingBoardView.isDrawingEnabled()) {
            Path drawnPath = mDrawingBoardView.getDrawPath();
            String normalizedPath = normalizePath(drawnPath, mDrawingBoardView.getWidth());

            Toast.makeText(getApplicationContext(), normalizedPath.substring(0, 25),Toast.LENGTH_LONG).show();
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
}
