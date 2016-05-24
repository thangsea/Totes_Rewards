package com.example.totes_rewards.totesrewards;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialsApi;

public class MainActivity extends AppCompatActivity {

    ProgressBar spinner;
    SharedPreferences userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        //CredentialsApi.request();
        open(findViewById(R.id.progressBar));
        try {
            userDetails = this.getSharedPreferences("userdetails", MODE_PRIVATE);
        } catch (Exception e) {
            Toast.makeText(this, "userdetails is null!", Toast.LENGTH_LONG).show();
        }
    }

    public void open(View view) {
        spinner.setIndeterminate(true);
        final int totalProgressTime = 50;


        try {
            String email = userDetails.getString("username", "");
            String password = userDetails.getString("password", "");
            Toast.makeText(this, email.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            //StartLogin();
            Toast.makeText(this, "Email is null", Toast.LENGTH_LONG).show();
        }


//        final Thread t = new Thread() {
//
//            @Override
//            public void run() {
//
//                int jumpTime = 0;
//                while (jumpTime < totalProgressTime) {
//                    try {
//                        sleep(1000);
//                        jumpTime += 5;
//                        Log.d("Message", "It's Working!");
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                }
//                StartMenu();
//            }
//        };
//        t.start();
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
