package com.example.totes_rewards.totesrewards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class RewardActivity extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;
    private String store;
    private String value;
    private String email = "blank";
    private String password = "blank";

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<Map<String, String>> productsList;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_REWARDS = "rewards";
    private static final String TAG_STORE = "store";
    private static final String TAG_VALUE = "value";

    // products JSONArray
    JSONArray rewards = null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_products);

        // Hashmap for ListView
        productsList = new ArrayList<>();

        // Loading products in Background Thread
        new LoadAllProducts().execute();

        // Get listview
        ListView lv = getListView();

        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String store = ((TextView) view.findViewById(R.id.store)).getText()
                        .toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        EditRewardActivity.class);
                // sending pid to next activity
                in.putExtra(TAG_STORE, store);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Reward Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.totes_rewards.totesrewards/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Reward Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.totes_rewards.totesrewards/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     */
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RewardActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            // getting JSON string from URL
            String url_all_products =
                    "http://cssgate.insttech.washington.edu/~luiss3/pull.php";

            email = PrefUtils.getFromPrefs(RewardActivity.this, "email", "null");
            password = PrefUtils.getFromPrefs(RewardActivity.this, "password", "null");

            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));

            JSONObject json = jParser.makeHttpRequest(url_all_products, "POST", params);

            // Check your log cat for JSON reponse
            if (json != null) {
                Log.d("All Products: ", json.toString());
            }

            try {
                int success = 1;
                // Checking for SUCCESS TAG
                if (json != null) {
                    success = json.getInt(TAG_SUCCESS);
                }

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    try {
                        assert json != null;
                        rewards = json.getJSONArray(TAG_REWARDS);
                    } catch (Exception e) {
                        Log.d("ERROR!!!!", "Rewards was not created.");
                    }

                    // looping through All Products
                        for (int i = 0; i < rewards.length(); i++) {
                            JSONObject c = rewards.getJSONObject(i);

                            // Storing each json item in variable
                            store = c.getString(TAG_STORE);
                            value = c.getString(TAG_VALUE);

                            // creating new HashMap
                            Map<String, String> map = new HashMap<>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_STORE, store);
                            map.put(TAG_VALUE, value);

                            // adding HashList to ArrayList
                            productsList.add(map);
                        }
                }
//                else {
//                    // no products found
//                    // Launch Add New product Activity
//                    Intent i = new Intent(getApplicationContext(),
//                            NewProductActivity.class);
//                    // Closing all previous activities
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            RewardActivity.this, productsList,
                            R.layout.list_item, new String[]{TAG_STORE,
                            TAG_VALUE},
                            new int[]{R.id.store, R.id.value});
                    // updating listview
                    setListAdapter(adapter);
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        pDialog.dismiss();
    }
}
