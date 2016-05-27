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
    private String points;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<Map<String, String>> productsList;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_REWARDS = "rewards";
    private static final String TAG_STORE = "store";
    private static final String TAG_POINTS = "points";
    private static final String TAG_USER_NAME = "username";

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
            final List<NameValuePair> params = new ArrayList<>();

            // getting JSON string from URL
            final String url_all_rewards =
                    "http://cssgate.insttech.washington.edu/~luiss3/pull.php";

            //Add the username as a param to params.
            params.add(new BasicNameValuePair(TAG_USER_NAME,
                    PrefUtils.getFromPrefs(RewardActivity.this, TAG_USER_NAME, "null")));

            //Create the json object using the post request.
            final JSONObject json = jParser.makeHttpRequest(url_all_rewards, "POST", params);

            // Check your log cat for JSON reponse
            if (json != null) {
                Log.e("**** All Products ****", json.toString());
            }

            try {
                // Checking for SUCCESS TAG

                assert json != null;
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    try {
                        rewards = json.getJSONArray(TAG_REWARDS);
                    } catch (Exception e) {
                        Log.e("****** ERROR!!!! ******", "Rewards was not created.");
                    }

                    // looping through All Products
                        for (int i = 0; i < rewards.length(); i++) {
                            JSONObject c = null;
                            Map<String, String> map = null;

                            try {
                                c = rewards.getJSONObject(i);
                            } catch (Exception e) {
                                Log.e("****** ERROR!!!! ******", "JSON object wasn't created.");
                            }

                            assert c != null;
                            // Storing each json item in variable
                            try {

                                store = c.getString(TAG_STORE);
                            } catch (Exception e) {
                                Log.e("****** ERROR!!!! ******", "store was not created.");
                            }

                            // Storing each json item in variable
                            try {
                                points = c.getString(TAG_POINTS);
                            } catch (Exception e) {
                                Log.e("****** ERROR!!!! ******", "points was not created.");
                            }


                            // creating new HashMap
                            try {
                                map = new HashMap<>();
                            } catch (Exception e) {
                                Log.e("****** ERROR!!!! ******", "map was not created.");
                            }

                            assert map != null;
                            // adding each child node to HashMap key => value
                            try {

                                map.put(TAG_STORE, store);
                            } catch (Exception e) {
                                Log.e("****** ERROR!!!! ******", "store was not added to map.");
                            }

                            try {
                                map.put(TAG_POINTS, points);
                            } catch (Exception e) {
                                Log.e("****** ERROR!!!! ******", "points were not added to map.");
                            }


                            // adding HashList to ArrayList
                            try {
                                productsList.add(map);
                            } catch (Exception e) {
                                Log.e("****** ERROR!!!! ******",
                                        "map wasn't added to productsList.");
                            }
                        }
                } else {
                    Log.e("****** ERROR!!!! ******", "Pull was unsuccessful.");
//                    // no products found
//                    // Launch Add New product Activity
//                    Intent i = new Intent(getApplicationContext(),
//                            NewProductActivity.class);
//                    // Closing all previous activities
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
                }
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
                            TAG_POINTS},
                            new int[]{R.id.store, R.id.points});
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

    public void backButton (View view) {
        Intent intent = new Intent(RewardActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
