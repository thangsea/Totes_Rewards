package com.example.totes_rewards.totesrewards;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import android.util.Log;


public class PopupActivity extends Activity implements OnClickListener {

    Button okButton, cancelButton;
    TextView popupText;
    private ProgressDialog pDialog;
    String results;
    String[] value;
    JSONParser jsonParser = new JSONParser();
    String url_create_product;
    private static final String TAG_SUCCESS = "success";
    final Intent menuIntent = new Intent(this, MenuActivity.class);
    final Intent scanIntent = new Intent(this, ScanActivity.class);


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
            pDialog = new ProgressDialog(PopupActivity.this);
            pDialog.setMessage("Adding/Updating Reward...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            value = results.split(":");
            // url to create new product
            url_create_product =
                    "http://cssgate.insttech.washington.edu/~luiss3/create_storecode.php";


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            String id = PrefUtils.getFromPrefs(PopupActivity.this, "email", "null");
            id.toLowerCase().hashCode();

            params.add(new BasicNameValuePair("userid", id));
            //params.add(new BasicNameValuePair("storename", results));
            params.add(new BasicNameValuePair("code", value[1]));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    startActivity(menuIntent);
                    finish();
                } else {
                    startActivity(scanIntent);
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
            pDialog.dismiss();
        }

    }
}
