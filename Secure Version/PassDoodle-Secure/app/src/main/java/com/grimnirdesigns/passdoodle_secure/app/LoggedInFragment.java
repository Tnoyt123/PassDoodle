package com.grimnirdesigns.passdoodle_secure.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class LoggedInFragment extends Fragment {
    public static String TAG_NAME = "com.grimnirdesigns.passdoodle_secure.app.loggedinfragment.name";
    public static String TAG_EMAIL = "com.grimnirdesigns.passdoodle_secure.app.loggedinfragment.email";
    public static String TAG_USERNAME = "com.grimnirdesigns.passdoodle_secure.app.loggedinfragment.username";

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";

    private String mUsername ="";
    private String mName = "";
    private String mEmail = "";



    private Button mLogOutButton, mSendEmailButton;

    private OnLoggedInFragmentInteractionListener mListener;


    public static LoggedInFragment newInstance(String username, String name, String email) {
        LoggedInFragment fragment = new LoggedInFragment();
        Bundle args = new Bundle();
        args.putString(TAG_USERNAME, username);
        args.putString(TAG_NAME, name);
        args.putString(TAG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }
    public LoggedInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsername = getArguments().getString(TAG_USERNAME);
            mName = getArguments().getString(TAG_NAME);
            mEmail = getArguments().getString(TAG_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_logged_in, container, false);

        TextView congratsTextView = (TextView)v.findViewById(R.id.fragment_logged_in_CongratsTextView);

        Resources res = getResources();
        String congratsString = res.getString(R.string.fragment_logged_in_SuccessfulLogIn);
        // Format congrats string with user's name
        congratsTextView.setText(String.format(congratsString, mName));

        mLogOutButton = (Button)v.findViewById(R.id.fragment_logged_in_LogOutButton);
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mListener.onLogout();
            }
        });

        mSendEmailButton = (Button)v.findViewById(R.id.fragment_logged_in_SendEmailButton);
        mSendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendEmailTask().execute(mUsername, mEmail);
            }
        });
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLoggedInFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoggedInFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnLoggedInFragmentInteractionListener {
        public void onSendEmail();
        public void onLogout();
        public void onEmailSuccess();
        public void onEmailFailure();
    }

    private class SendEmailTask extends AsyncTask <String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String emailAddress = params[1];

            UserFunctions userFunctions = new UserFunctions();
            JSONObject json = userFunctions.sendEmail(username, emailAddress);

            try {
                if (json.getString(KEY_SUCCESS) != null && json.getString(KEY_ERROR) != null) {
                    String successRes = json.getString(KEY_SUCCESS);
                    String errorRes = json.getString(KEY_ERROR);
                    if (Integer.parseInt(successRes) == 1) {
                        //user successfully logged in
                        JSONObject json_user = json.getJSONObject("user");
                        mListener.onEmailSuccess();

                    } else if (Integer.parseInt(errorRes) == 1 && json.getString(KEY_ERROR_MSG) != null){
                        String errorMessage = json.getString(KEY_ERROR_MSG);
                        mListener.onEmailFailure();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "Executed";
        }
    }
}
