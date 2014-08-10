package com.grimnirdesigns.passdoodle_secure.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

public class MainMenuActivity extends Activity implements MainMenuFragment.OnMainMenuFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Fragment fragment = MainMenuFragment.newInstance();

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().add(R.id.activity_main_menu_Container, fragment).commit();
    }

    @Override
    public void onFragmentLoginButtonPressed() {
        //Load Login activity
        Intent i = new Intent(this, LoggingInActivity.class);
        startActivity(i);
    }

    @Override
    public void onFragmentRegisterButtonPressed() {
        //Load Register activity
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}
