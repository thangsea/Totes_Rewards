package com.example.totes_rewards.totesrewards;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


public class PopupActivity extends Activity implements OnClickListener {

    Button okButton, cancelButton;
    TextView popupText;
    private ProgressDialog pDialog;
    String results;
    String[] value;
    JSONParser jsonParser = new JSONParser();
    String url_add_reward;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USER_NAME = "username";
    private static final String TAG_STORE = "store";
    private static final String TAG_POINTS = "points";
    private static final String TAG_CODE = "code";
    final Intent menuIntent = new Intent(PopupActivity.this, MenuActivity.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        init();
        popupInit();
    }

    public void init() {
        Bundle b = getIntent().getExtras();
        results = b.getString("results");
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

            if (v.getId() == R.id.okButton) {
                if (menuIntent != null) {
                    new CreateNewProduct().execute();
                }
            }

            else if (v.getId() == R.id.cancelButton) {
                final Intent scanIntent = new Intent(this, ScanActivity.class);
                if (scanIntent != null) {
                    startActivity(scanIntent);
                    finish();
                }
            }
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(PopupActivity.this);
//            pDialog.setMessage("Adding/Updating Reward...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            value = results.split(":");

            // URL to update a store with the scanned reward.
            url_add_reward =
                    "http://cssgate.insttech.washington.edu/~luiss3/add.php";

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(TAG_USER_NAME,
                    PrefUtils.getFromPrefs(PopupActivity.this, "email", "null")));

            params.add(new BasicNameValuePair(TAG_STORE, value[0]));
            params.add(new BasicNameValuePair(TAG_POINTS, value[1]));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_add_reward,
                    "POST", params);

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (json != null) {
                    Log.d("**** All Products ****", json.toString());
                }

                if (success == 1) {
                    startActivity(new Intent(PopupActivity.this, MenuActivity.class));
                    //pDialog.dismiss();
                    finish();
                } else {
                    startActivity(new Intent(PopupActivity.this, ScanActivity.class));
                    //pDialog.dismiss();
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            //pDialog.dismiss();
        }

    }
}
