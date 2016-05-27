package com.example.totes_rewards.totesrewards;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ProgressBar spinner;
    String email = "blank";
    String password = "blank";
    private static final String TAG_USER_NAME = "username";
    private static final String TAG_PW = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        open(findViewById(R.id.progressBar));
    }

    public void open(View view) {
        spinner.setIndeterminate(true);

        try {
            password = PrefUtils.getFromPrefs(MainActivity.this, TAG_PW, "null");
            email = PrefUtils.getFromPrefs(MainActivity.this, TAG_USER_NAME, "null");
        } catch (Exception e) {
            Toast.makeText(this, "Account not found", Toast.LENGTH_LONG).show();
        }

        if (email .equals("null")) {
            StartLogin();
        } else {
            Toast.makeText(this, "The account Name is: " + email +
                    ", \nand the Password is: " + password, Toast.LENGTH_LONG).show();
            StartMenu();
        }
    }

    public void StartMenu() {
        final Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
    }

    public void StartLogin() {
        final Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

}
