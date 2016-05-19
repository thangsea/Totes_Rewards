package com.example.totes_rewards.totesrewards;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    ProgressBar spinner;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        open(findViewById(R.id.progressBar));
        StartMenu();
    }

    public void open(View view) {
        spinner.setIndeterminate(true);
        final int totalProgressTime = 50;

        final Thread t = new Thread() {

            @Override
            public void run() {

                int jumpTime = 0;
                while (jumpTime < totalProgressTime) {
                    try {
                        sleep(1000);
                        jumpTime += 5;
                        Log.d("Message", "It's Working!");
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        };
        t.start();
    }



    public void StartMenu() {
        final Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
    }

}
