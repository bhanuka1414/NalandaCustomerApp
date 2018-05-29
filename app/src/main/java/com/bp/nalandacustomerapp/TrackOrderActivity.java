package com.bp.nalandacustomerapp;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bp.nalandacustomerapp.services.CommonConstants;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TrackOrderActivity extends FragmentActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
    Marker marker1, marker2;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.red};
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        polylines = new ArrayList<>();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng defaultLoc = new LatLng(6.9019, 80.9079);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Nalanda Driver").icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker)));


        MarkerOptions del = new MarkerOptions().position(defaultLoc).title("Nalanda Driver").icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
        marker1 = mMap.addMarker(del);

        MarkerOptions cus = new MarkerOptions().position(defaultLoc).title("You");
        marker2 = mMap.addMarker(cus);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLoc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 14.0f));
        //updateMap(6.9271,79.8612);
        Intent intent = getIntent();
        new BackgroundGetOrderLoc().execute(intent.getStringExtra("order_id"));
        setRepeatingLocationUpdate(intent.getStringExtra("order_id"));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
        timer.purge();
        Toast.makeText(this, "cancel timer", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void updateMap(double v, double v1, double v2, double v3) {
        //marker.setPosition(new LatLng(v,v1));
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(new LatLng(v2, v3), new LatLng(v, v1))
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    private void setRepeatingLocationUpdate(final String oid) {


        final Handler handler = new Handler();
        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            new BackgroundGetOrderLoc().execute(oid);
                        } catch (Exception e) {
                            // error, do something
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, 10000);  // interval of one minute

    }


    class BackgroundGetOrderLoc extends AsyncTask<String, Void, String> {
        String myUrl = CommonConstants.SITE_URL + "get_order_location.php";


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            String[] parts = s.split(",");
            Double clat = Double.parseDouble(parts[0]);
            Double clng = Double.parseDouble(parts[1]);
            Double dlat = Double.parseDouble(parts[2]);
            Double dlng = Double.parseDouble(parts[3]);
            updateMap(clat, clng, dlat, dlng);
            marker1.setPosition(new LatLng(dlat, dlng));
            marker2.setPosition(new LatLng(clat, clng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(dlat, dlng)));

            //Toast.makeText(TrackOrderActivity.this,parts[0]+" "+parts[1]+" "+parts[2]+" "+parts[3],Toast.LENGTH_LONG).show();

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(myUrl);
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("oid", strings[0]));
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
