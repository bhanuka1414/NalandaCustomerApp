package com.bp.nalandacustomerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bp.nalandacustomerapp.services.CommonConstants;
import com.bp.nalandacustomerapp.services.DatabaseHelper;
import com.bp.nalandacustomerapp.services.models.CartModel;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LocationPickerActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private DatabaseHelper db;
    private ProgressDialog progressDialog;
    private Button orderBtn;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private TextView resutText;
    GeoDataClient mGeoDataClient;
    PlaceDetectionClient mPlaceDetectionClient;
    FusedLocationProviderClient mFusedLocationProviderClient;
    boolean mLocationPermissionGranted;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location mLastKnownLocation;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);


    String msg = "";
    double userLat = 0.0;
    double userLng = 0.0;
    String cartList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < 22)
            setStatusBarTranslucent(false);
        else
            setStatusBarTranslucent(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        db = new DatabaseHelper(this);

        Cursor res = db.getCartItems();
        if (res.getCount() > 0) {
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            while (res.moveToNext()) {

                //cartItems.add(new CartModel(res.getInt(0),res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5)));


                try {
                    //json.put("name", "student");

                    JSONObject item = new JSONObject();
                    item.put("id", res.getString(2));
                    item.put("name", res.getString(1));
                    item.put("qty", res.getString(3));
                    item.put("price", res.getString(4));
                    item.put("img", res.getString(5));
                    array.put(item);

                    json.put("cart", array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            cartList = json.toString();

        }

        Intent intent = getIntent();
        msg = intent.getStringExtra("order_msg");

        resutText = (TextView) findViewById(R.id.dragg_result);
        orderBtn = (Button) findViewById(R.id.l_order_btn);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        View locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 50);
        displayLocationSettingsRequest(LocationPickerActivity.this);

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundOrderMaking().execute();
            }
        });
        configureCameraIdle();

    }

    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(LocationPickerActivity.this);

                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        String locality = addressList.get(0).getAddressLine(0);
                        String country = addressList.get(0).getCountryName();
                        if (!locality.isEmpty() && !country.isEmpty())
                            resutText.setText(locality + "  " + country);
                        //resutText.setText(String.valueOf(latLng.latitude) + "  " + String.valueOf(latLng.longitude));
                        userLat = latLng.latitude;
                        userLng = latLng.longitude;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(onCameraIdleListener);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener((GoogleMap.OnMyLocationButtonClickListener) LocationPickerActivity.this);
        mMap.setOnMyLocationClickListener((GoogleMap.OnMyLocationClickListener) LocationPickerActivity.this);

    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //Log.i("tag", "All location settings are satisfied.");
                        Toast.makeText(LocationPickerActivity.this, "suc", Toast.LENGTH_LONG).show();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        //Log.i("tag", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        Toast.makeText(LocationPickerActivity.this, "Location settings are not satisfied. Show the user a dialog to upgrade location settings", Toast.LENGTH_LONG).show();
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(LocationPickerActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            //Log.i("tag", "PendingIntent unable to execute request.");
                            Toast.makeText(LocationPickerActivity.this, "PendingIntent unable to execute request.", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Log.i("tag", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        Toast.makeText(LocationPickerActivity.this, "cannot be fixed here", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }


    class BackgroundOrderMaking extends AsyncTask<String, Void, String> {
        String myUrl = CommonConstants.SITE_URL + "make_order.php";
        SharedPreferences prefs = getSharedPreferences(CommonConstants.USER_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("un", null);
        String cid = prefs.getString("id", "Not Loging");

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LocationPickerActivity.this);
            progressDialog.setMessage("plz wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            //Toast.makeText(LocationPickerActivity.this,s,Toast.LENGTH_LONG).show();
            if (s.trim().equals("1")) {
                db.clearCart();
                Intent intent = new Intent(LocationPickerActivity.this, MyOrdersActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LocationPickerActivity.this, "error!", Toast.LENGTH_LONG).show();
            }
            //resutText.setText(s);


            progressDialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(myUrl);
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("cid", cid));
                params.add(new BasicNameValuePair("lat", String.valueOf(userLat)));
                params.add(new BasicNameValuePair("lng", String.valueOf(userLng)));
                params.add(new BasicNameValuePair("msg", msg));
                params.add(new BasicNameValuePair("cart_items", cartList));
                /*System.out.println("GO TO?????" + query_string);
                System.out.println("GO TO PARAM???" + params);*/
                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                //view_account.setText(httpResponse.getStatusLine().toString());
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();

                BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = bufReader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                inputStream.close();
                result = builder.toString();
            } catch (Exception e) {
                Log.e("log_tag", e.toString());
            }


            return result;
        }
    }

}
