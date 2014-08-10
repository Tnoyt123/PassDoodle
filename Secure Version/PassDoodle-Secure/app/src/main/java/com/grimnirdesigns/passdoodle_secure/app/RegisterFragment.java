package com.grimnirdesigns.passdoodle_secure.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {
    private OnRegisterFragmentInteractionListener mListener;

    public static final int PATH_STEPS = 100;
    public static final int GRID_SQUARES = 13;

    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_USERNAME = "username";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";

    private static boolean sRegistering = false;

    private Button mClearDoodleButton, mSubmitButton;
    private DrawingBoardView mDrawingBoardView;
    private EditText mUserNameEditText, mNameEditText, mEmailEditText;
    private View mScreenView;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        ScrollView scrollView = (ScrollView) v.findViewById(R.id.fragment_register_MainScrollView);


        mScreenView = v.findViewById(R.id.fragment_register_ScreenView);
        mScreenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        mClearDoodleButton = (Button) v.findViewById(R.id.fragment_register_ClearDoodleButton);
        mClearDoodleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDoodle();
            }
        });

        mSubmitButton = (Button) v.findViewById(R.id.fragment_register_SubmitButton);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRegistration();
            }
        });

        mDrawingBoardView = (DrawingBoardView) v.findViewById(R.id.fragment_register_DrawingBoardView);

        mUserNameEditText = (EditText) v.findViewById(R.id.fragment_register_UserNameEditText);

        mNameEditText = (EditText) v.findViewById(R.id.fragment_register_NameEditText);

        mEmailEditText = (EditText) v.findViewById(R.id.fragment_register_EmailEditText);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDrawingBoardView.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        mDrawingBoardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        return v;
    }

    private void clearDoodle() {
        mDrawingBoardView.resetDrawPath();
        mDrawingBoardView.clearDrawing();
    }

    private void submitRegistration() {
        if (!sRegistering) {
            sRegistering = true;
            if (!mDrawingBoardView.isDrawingEnabled()) {
                Path drawnPath = mDrawingBoardView.getDrawPath();
                String normalizedPath = normalizePath(drawnPath, mDrawingBoardView.getWidth());

                String username = mUserNameEditText.getText().toString().trim();
                String fullName = mNameEditText.getText().toString().trim();
                String email = mEmailEditText.getText().toString().trim();

                //VALIDATION!
                if (username == null || username == "") {
                    Toast.makeText(getActivity(), "Please enter a username.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (fullName == null || fullName == "") {
                    Toast.makeText(getActivity(), "Please enter your name.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (email == null || email == "" || !validEmail(email)) {
                    Toast.makeText(getActivity(), "Please enter a valid email address.", Toast.LENGTH_LONG).show();
                    return;
                }

                new RegisterUserTask().execute(username, normalizedPath, fullName, email);

            } else {
                Toast.makeText(getActivity(), "Please enter a PassDoodle.", Toast.LENGTH_LONG).show();
            }
            sRegistering = false;
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

    private boolean validEmail(String email) {
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Matcher matcher = Pattern.compile(EMAIL_PATTERN).matcher(email);
        return matcher.matches();
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnRegisterFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRegisterFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnRegisterFragmentInteractionListener {
        public void onSuccessfulRegistrationInteraction();
        public void onUnsuccessfulRegistrationInteraction(String errorMessage);
    }

    private class RegisterUserTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... inputs) {

            String username = inputs[0];
            String passDoodle = inputs[1];
            String fullName = inputs[2];
            String email = inputs[3];

            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.registerUser(username, passDoodle, fullName, email);

            //Check for success
            try {
                if (json.getString(KEY_SUCCESS) != null && json.getString(KEY_ERROR) != null) {
                    String successResult = json.getString(KEY_SUCCESS);
                    String errorResult = json.getString(KEY_ERROR);
                    if (Integer.parseInt(successResult) == 1) {
                        //user stored
                        //Toast.makeText(getApplicationContext(), "Registration Successful!", Toast.LENGTH_LONG).show();
                        //store details in DatabaseHandler
                        JSONObject json_user = json.getJSONObject("user");

                        mListener.onSuccessfulRegistrationInteraction();

                    } else if (Integer.parseInt(errorResult) != 0 && json.getString(KEY_ERROR_MSG) != null){
                        //Toast.makeText(getApplicationContext(), "Error occurred in registration", Toast.LENGTH_LONG).show();
                        mListener.onUnsuccessfulRegistrationInteraction(json.getString(KEY_ERROR_MSG));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "Executed";
        }
    }

}
