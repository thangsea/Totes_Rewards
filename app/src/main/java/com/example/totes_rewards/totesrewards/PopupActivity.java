package com.example.totes_rewards.totesrewards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;


public class PopupActivity extends Activity implements OnClickListener {

    PopupWindow popupMessage;
    Button okButton, cancelButton;
    TextView popupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        init();
        popupInit();
    }

    public void init() {
        Bundle b = getIntent().getExtras();
        String results = b.getString("results");
        okButton = (Button) findViewById(R.id.okButton);
        popupText = (TextView) findViewById(R.id.resultView);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        if (popupText != null) {
            popupText.setText(getString(R.string.result) + results
                    + getString(R.string.confirm));
        }
    }

    public void popupInit() {
        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Intent menuIntent = new Intent(this, MenuActivity.class);
        final Intent scanIntent = new Intent(this, ScanActivity.class);

            if (v.getId() == R.id.okButton) {
                if (menuIntent != null) {
                    startActivity(menuIntent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }

            else if (v.getId() == R.id.cancelButton) {
                if (scanIntent != null) {
                    startActivity(scanIntent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
    }
}
