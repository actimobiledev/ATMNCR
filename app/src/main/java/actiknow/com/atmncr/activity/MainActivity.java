package actiknow.com.atmncr.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import actiknow.com.atmncr.R;
import actiknow.com.atmncr.adapter.AllAtmAdapter;
import actiknow.com.atmncr.adapter.NavDrawerAdapter;
import actiknow.com.atmncr.app.AppController;
import actiknow.com.atmncr.model.Atms;
import actiknow.com.atmncr.model.Questions;
import actiknow.com.atmncr.utils.AppConfigTags;
import actiknow.com.atmncr.utils.AppConfigURL;
import actiknow.com.atmncr.utils.Constants;
import actiknow.com.atmncr.utils.LoginDetailsPref;
import actiknow.com.atmncr.utils.NetworkConnection;

public class MainActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    //  karman singh

    TextView tvNoInternetConnection;
    ProgressBar progressBar;
    ListView listViewAllAtms;

    GoogleApiClient client;
    GoogleApiClient googleApiClient;
    Dialog splash;

    // Action Bar components
    private List<Atms> atmsList = new ArrayList<Atms> ();
    private AllAtmAdapter adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerPanel;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        initView ();

        setUpNavigationDrawer ();
        initLocationSettings ();

        adapter = new AllAtmAdapter (this, atmsList);
        listViewAllAtms.setAdapter (adapter);

        client = new GoogleApiClient.Builder (this).addApi (AppIndex.API).build ();

        if (Constants.splash_screen_first_time == 0) {
            Log.d ("HELLO", "splash screen first time");

            Constants.splash_screen_first_time = 1;
            splash = new Dialog (this, R.style.full_screen);
            splash.setContentView (R.layout.dialog_splash);
            splash.setCancelable (false);
            splash.show ();
        }

        final Handler handler = new Handler ();
        handler.postDelayed (new Runnable () {
            @Override
            public void run () {
                if (splash != null)
                    splash.dismiss ();
            }
        }, 3000);

        initPref ();
        isLogin ();


        if (NetworkConnection.isNetworkAvailable (this)) {
            
            Log.d ("URL", AppConfigURL.URL_GETALLATMS);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_GETALLATMS,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            int is_data_received = 0;
                            int json_array_len = 0;

                            Log.d ("SERVER RESPONSE", response);
                            if (response != null) {
                                is_data_received = 1;
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.ATMS);
                                    json_array_len = jsonArray.length ();
                                    for (int i = 0; i < json_array_len; i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject (i);
                                        Atms atm = new Atms ();
                                        atm.setAtm_id (jsonObject.getInt (AppConfigTags.ATM_ID));
                                        atm.setAtm_unique_id (jsonObject.getString (AppConfigTags.ATM_UNIQUE_ID));
                                        atm.setAtm_last_audit_date (jsonObject.getString (AppConfigTags.ATM_LAST_AUDIT_DATE));
                                        atm.setAtm_bank_name (jsonObject.getString (AppConfigTags.ATM_BANK_NAME));
                                        atm.setAtm_address (jsonObject.getString (AppConfigTags.ATM_ADDRESS));
                                        atm.setAtm_city (jsonObject.getString (AppConfigTags.ATM_CITY));
                                        atm.setAtm_pincode (jsonObject.getString (AppConfigTags.ATM_PINCODE));
                                        atmsList.add (atm);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace ();
                                }
                            } else {
                                Log.e (AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER);
                            }

                            adapter.notifyDataSetChanged ();
                            if (is_data_received != 0 && json_array_len != 0) {
                                progressBar.setVisibility (View.GONE);
                                listViewAllAtms.setVisibility (View.VISIBLE);
                                tvNoInternetConnection.setVisibility (View.GONE);
                            } else if (is_data_received != 0 && json_array_len == 0) {
                                tvNoInternetConnection.setVisibility (View.VISIBLE);
                                progressBar.setVisibility (View.GONE);
                                listViewAllAtms.setVisibility (View.GONE);
                            }
                            if (is_data_received == 0) {
                                progressBar.setVisibility (View.VISIBLE);
                                listViewAllAtms.setVisibility (View.GONE);
                                tvNoInternetConnection.setVisibility (View.GONE);
                            }


                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Log.d ("TAG", error.toString ());
                        }
                    });
               AppController.getInstance ().addToRequestQueue (strRequest);


            Log.d ("URL", AppConfigURL.URL_GETALLQUESTIONS);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_GETALLQUESTIONS,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Log.d ("SERVER RESPONSE", response);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.QUESTIONS);
                                    int json_array_len = jsonArray.length ();
                                    for (int i = 0; i < json_array_len; i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject (i);
                                        Questions question = new Questions ();
                                        question.setQuestion_id (jsonObject.getInt (AppConfigTags.QUESTION_ID));
                                        question.setQuestion (jsonObject.getString (AppConfigTags.QUESTION));
                                        Constants.questionsList.add (question);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace ();
                                }
                            } else {
                                Log.e (AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Log.d ("TAG", error.toString ());
                        }
                    });
            AppController.getInstance ().addToRequestQueue (strRequest1);

        }

    }

    private void isLogin () {
        if (Constants.username == "" || Constants.password == "") {
            Intent myIntent = new Intent (this, LoginActivity.class);
            startActivity (myIntent);
        }
        // if (Constants.username == "" || Constants.password == "")
        //    finish();
    }

    private void initPref () {
        LoginDetailsPref loginDetailsPref = LoginDetailsPref.getInstance ();
        Constants.username = loginDetailsPref.getStringPref (MainActivity.this, LoginDetailsPref.USERNAME);
        Constants.password = loginDetailsPref.getStringPref (MainActivity.this, LoginDetailsPref.PASSWORD);

    }


    private void initView () {
        listViewAllAtms = (ListView) findViewById (R.id.lvAtmList);
        tvNoInternetConnection = (TextView) findViewById (R.id.tvNoIternetConnection);
        progressBar = (ProgressBar) findViewById (R.id.progressbar);
    }

    @Override
    public void onStart () {
        super.onStart ();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect ();
        Action viewAction = Action.newAction (
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse ("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse ("android-app://actiknow.com.atmncr/http/host/path")
        );
        AppIndex.AppIndexApi.start (client, viewAction);
    }

    @Override
    public void onStop () {
        super.onStop ();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction (
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse ("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse ("android-app://actiknow.com.atmncr/http/host/path")
        );
        AppIndex.AppIndexApi.end (client, viewAction);
        client.disconnect ();
    }

    @Override
    public void onBackPressed () {
        if (mDrawerLayout.isDrawerOpen (mDrawerPanel)) {
            mDrawerLayout.closeDrawer (mDrawerPanel);
        } else {
            finish ();
            overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    private void setUpNavigationDrawer () {
        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar1);
        setSupportActionBar (toolbar);
        ActionBar actionBar = getSupportActionBar ();
        try {
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled (false);
            actionBar.setHomeButtonEnabled (false);
            actionBar.setTitle (getResources ().getString (R.string.app_name));
            actionBar.setDisplayShowTitleEnabled (false);
        } catch (Exception ignored) {
        }
        ListView mDrawerListView = (ListView) findViewById (R.id.navDrawerList);
        mDrawerPanel = (RelativeLayout) findViewById (R.id.navDrawerPanel);
        mDrawerLayout = (DrawerLayout) findViewById (R.id.drawer_layout);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<> (this, android.R.layout.simple_list_item_1, getResources ().getStringArray (R.array.menulist));
        mDrawerListView.setAdapter (new NavDrawerAdapter (this, getResources ().getStringArray (R.array.menulist)));
        mDrawerListView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                }
                mDrawerLayout.closeDrawer (mDrawerPanel);
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle (this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened (View drawerView) {
                super.onDrawerOpened (drawerView);
                //getSupportActionBar().setTitle(getString(R.string.drawer_opened));
                invalidateOptionsMenu ();
            }

            public void onDrawerClosed (View view) {
                super.onDrawerClosed (view);
                //getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu ();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled (true);
        mDrawerLayout.setDrawerListener (mDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater ().inflate (R.menu.menu_rest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        View view2 = this.getCurrentFocus ();
        if (view2 != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService (Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow (view2.getWindowToken (), 0);
        }
        if (item != null && item.getItemId () == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen (mDrawerPanel)) {
            } else {
                mDrawerLayout.openDrawer (mDrawerPanel);
            }
            return true;
        }
        return super.onOptionsItemSelected (item);
    }

    private void initLocationSettings () {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder (this).addApi (LocationServices.API).addConnectionCallbacks (this)
                    .addOnConnectionFailedListener (MainActivity.this).build ();
            googleApiClient.connect ();
            LocationRequest locationRequest = LocationRequest.create ();
            locationRequest.setPriority (LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval (30 * 1000);
            locationRequest.setFastestInterval (5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder ().addLocationRequest (locationRequest);

            builder.setAlwaysShow (true);
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings (googleApiClient, builder.build ());
            result.setResultCallback (new ResultCallback<LocationSettingsResult> () {
                @Override
                public void onResult (LocationSettingsResult result) {
                    final Status status = result.getStatus ();
                    final LocationSettingsStates state = result.getLocationSettingsStates ();
                    switch (status.getStatusCode ()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult (MainActivity.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onConnected (Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended (int i) {

    }

    @Override
    public void onLocationChanged (Location location) {

    }

    @Override
    public void onStatusChanged (String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled (String provider) {

    }

    @Override
    public void onProviderDisabled (String provider) {

    }

    @Override
    public void onConnectionFailed (ConnectionResult connectionResult) {

    }
}


