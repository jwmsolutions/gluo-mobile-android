package com.gluo.pruebas.test1;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gluo.pruebas.DefaultFragment;
import com.gluo.pruebas.R;
import com.gluo.pruebas.TestDetailActivity;
import com.gluo.pruebas.tests.TestContent;
import com.gluo.pruebas.service.FetchAddressIntentService;
import com.gluo.pruebas.util.Constants;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

public class LocationFragment extends DefaultFragment {
    private static final Logger logger = LoggerManager.getLogger(LocationFragment.class);

    private Location mLastLocation;

    protected TextView latText;
    protected TextView longText;
    protected TextView accText;
    protected TextView altText;
    protected TextView addressText;

    private LocationRequest mLocationRequest;

    private LocationListener locationListener;

    private AddressResultReceiver mResultReceiver;

    protected String mAddressOutput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResultReceiver = new AddressResultReceiver(new Handler());
        logger.d("onCreate Location Fragment");
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = TestContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logger.d("onCreateView Location Fragment");
        View rootView = inflateDefaultView(inflater, container, R.layout.test1_location_mainfragment);
        Button printBtn = (Button) rootView.findViewById(R.id.printCoordinates);
        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logger.d("Mostrar ubicación");
                printCurrentLocation();
                ((Button)v).setText("Auto updating");
                ((Button)v).setEnabled(false);
            }
        });
        // Se asignan los textos de ubicación
        latText = (TextView) rootView.findViewById(R.id.latitud);
        longText = (TextView) rootView.findViewById(R.id.longitud);
        accText = (TextView) rootView.findViewById(R.id.precision);
        altText = (TextView) rootView.findViewById(R.id.altitud);
        addressText = (TextView) rootView.findViewById(R.id.address);

        createLocationRequest();

        return rootView;
    }

    @Override
    public void onPause(){
        super.onPause();
        stopLocationUpdates();
        logger.d("PAUSE");
    }

    @Override
    public void onResume() {
        super.onResume();
        printCurrentLocation();
        logger.d("RESUME");
    }

    /**
     * Método para mostrar la ubicación actual en la pantalla
     */
    private void printCurrentLocation() {
        boolean googleApiConnected = ((TestDetailActivity)getActivity()).isGoogleApiConnected();
        if(googleApiConnected) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    getGoogleApiClient());
            if (mLastLocation != null) {
                updateUI();
            } else {
                Toast.makeText(getActivity(), "No Location Detected", Toast.LENGTH_LONG).show();
            }

            startLocationUpdates();
        } else {
            logger.e("No se pudo conectar con GoogleAPI");
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        getLocationListener();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                getGoogleApiClient(), mLocationRequest, locationListener);
    }

    private void getLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                logger.d("Location has changed!");
                mLastLocation = location;
                updateUI();
                startIntentService();
            }
        };
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                getGoogleApiClient(), locationListener);
    }

    private void updateUI(){
        logger.d(mLastLocation.toString());
        logger.d(String.valueOf(mLastLocation.getLatitude()));
        logger.d(String.valueOf(mLastLocation.getLongitude()));
        // Se actualizan los contenedores
        latText.setText(String.valueOf(mLastLocation.getLatitude()));
        longText.setText(String.valueOf(mLastLocation.getLongitude()));
        accText.setText(String.valueOf(mLastLocation.getAccuracy()) + " m");
        altText.setText(String.valueOf(mLastLocation.getAltitude()));
    }

    protected void startIntentService() {
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        getActivity().startService(intent);
    }

    private GoogleApiClient getGoogleApiClient(){
        return ((TestDetailActivity)getActivity()).getmGoogleApiClient();
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            logger.d(mAddressOutput);
            addressText.setText(mAddressOutput);

            // Show a log message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                logger.d(getString(R.string.address_found));
            }

        }
    }
}
