package com.gluo.pruebas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.gluo.pruebas.tests.TestContent;
import com.gluo.pruebas.tests.TestContent.TestItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;


/**
 * An activity representing a single Test detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link TestListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link TestDetailFragment}.
 */
public class TestDetailActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final Logger logger = LoggerManager.getLogger(TestDetailActivity.class);

    private GoogleApiClient mGoogleApiClient;

    private DefaultFragment fragment;

    private boolean googleApiConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);
        logger.d("onCreate");

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            String testId = getIntent().getStringExtra(DefaultFragment.ARG_ITEM_ID);
            arguments.putString(DefaultFragment.ARG_ITEM_ID,
                    testId);
            TestItem test = TestContent.ITEM_MAP.get(testId);
            if (test.fragmentClass != null) {
                fragment = test.fragmentClass;
            } else {
                fragment = new TestDetailFragment();
            }
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.test_detail_container, fragment)
                    .commit();
        }

        logger.d("Creating googleApiClient");
        buildGoogleApiClient();
    }

    @Override
    public void onStart() {
        logger.d("onStart");
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        logger.d("onStop");
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            googleApiConnected = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, TestListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        logger.d("onConnected");
        googleApiConnected = true;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        logger.d("onConnectionSuspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        logger.d("onConnectionFailed: " + connectionResult.getErrorCode());
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    /**
     * GETTERS & SETTERS
     */
    public boolean isGoogleApiConnected() {
        return googleApiConnected;
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

}
