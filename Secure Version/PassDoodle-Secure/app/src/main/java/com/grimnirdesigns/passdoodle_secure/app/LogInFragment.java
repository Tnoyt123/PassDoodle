package com.grimnirdesigns.passdoodle_secure.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInFragment extends Fragment {
    private OnSuccessfulLoginListener mListener;

    public static final int PATH_STEPS = 100;
    public static final int GRID_SQUARES = 13;

    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_USERNAME = "username";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";

    private static boolean loggingIn = false;

    private View mScreenView;
    private EditText mUserNameEditText;
    private DrawingBoardView mDrawingBoardView;
    private Button mClearDoodleButton, mSubmitButton;

    public static LogInFragment newInstance() {
        LogInFragment fragment = new LogInFragment();
        return fragment;
    }
    public LogInFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_log_in, container, false);

        mScreenView =v.findViewById(R.id.fragment_log_in_ScreenView);
        mScreenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        mUserNameEditText = (EditText)v.findViewById(R.id.fragment_log_in_UserNameEditText);

        mDrawingBoardView = (DrawingBoardView)v.findViewById(R.id.fragment_log_in_DrawingBoardView);

        mClearDoodleButton = (Button)v.findViewById(R.id.fragment_log_in_ClearDoodleButton);
        mClearDoodleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDoodle();
            }
        });

        mSubmitButton = (Button)v.findViewById(R.id.fragment_log_in_SubmitButton);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitLogin();
            }
        });

        return v;
    }

    private void submitLogin() {
        if (!loggingIn) {
            loggingIn = true;
            Log.d("submitLogin", "begin login");
            if (!mDrawingBoardView.isDrawingEnabled()) {
                Path drawnPath = mDrawingBoardView.getDrawPath();
                String normalizedPath = normalizePath(drawnPath, mDrawingBoardView.getWidth());

                String username = mUserNameEditText.getText().toString().trim();

                if (username == null || username == "") {
                    Toast.makeText(getActivity(), "Please enter your username.", Toast.LENGTH_LONG).show();
                    return;
                }

                new SubmitLoginTask().execute(username, normalizedPath);
                clearDoodle();

            } else {
                Toast.makeText(getActivity(), "Please enter your PassDoodle.", Toast.LENGTH_LONG).show();
            }
            loggingIn = false;
        }
    }

    private String normalizePath(Path path, int viewWidth) {
        PathMeasure pm = new PathMeasure(path, false);

        double gridSize = viewWidth/ GRID_SQUARES;

        float pathLength = pm.getLength();
        float pathDivision = pathLength / PATH_STEPS;

        StringBuffer output = new StringBuffer();

        for (int i = 0; i < PATH_STEPS; i++) {
            float[] point = new float[2];
            pm.getPosTan(i * pathDivision, point, null);

            double xPos, yPos;
            xPos = (double) Math.round((double) (point[0]) / gridSize);
            yPos = (double) Math.round((double) (point[1]) / gridSize);

            output.append(xPos).append(",").append(yPos).append(";");
        }

        return output.toString();
    }

    private void clearDoodle() {
        if (mDrawingBoardView != null) {
            mDrawingBoardView.resetDrawPath();
            mDrawingBoardView.clearDrawing();
        }
    }

    private class SubmitLoginTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... inputs) {
            String username = inputs[0];
            String passDoodle = inputs[1];


            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.loginUser(username, passDoodle);

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);
                    if (Integer.parseInt(res) == 1) {
                        //user successfully logged in
                        JSONObject json_user = json.getJSONObject("user");

                        mListener.onSuccessfulLogin(json_user.getString(KEY_USERNAME), json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL));
                    } else {
                        mListener.onUnsuccessfulLogin();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "Executed";
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSuccessfulLoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSuccessfulLoginListener {
        public void onSuccessfulLogin(String username, String name, String email);
        public void onUnsuccessfulLogin();
    }

}
