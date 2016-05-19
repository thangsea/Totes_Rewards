package com.example.totes_rewards.totesrewards;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    ProgressBar spinner;
    final Intent menuIntent = new Intent(this, MenuActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        spinner = (ProgressBar) findViewById(R.id.progressBar);

    }

    public void open(View view) {
        spinner.setIndeterminate(true);
        final int totalProgressTime = 10;

        final Thread t = new Thread() {

            @Override
            public void run() {

                int jumpTime = 0;
                while (jumpTime < totalProgressTime) {
                    try {
                        sleep(10);
                        jumpTime += 5;
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        };
        startActivity(menuIntent);
    }
}
