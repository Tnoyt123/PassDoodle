package com.grimnirdesigns.passdoodle_unsecure.app;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainMenuFragment extends Fragment {

    private OnMainMenuFragmentInteractionListener mCallback;
    private Button mLoginButton;
    private Button mRegisterButton;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();
        return fragment;
    }

    public interface OnMainMenuFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentLoginButtonPressed();
        public void onFragmentRegisterButtonPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);

        mLoginButton = (Button)v.findViewById(R.id.fragment_main_menu_LoginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonPressed();
            }
        });

        mRegisterButton = (Button)v.findViewById(R.id.fragment_main_menu_RegisterButton);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButtonPressed();
            }
        });

        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnMainMenuFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    private void registerButtonPressed() {
        mCallback.onFragmentRegisterButtonPressed();
    }

    private void loginButtonPressed() {
        mCallback.onFragmentLoginButtonPressed();
    }

}
