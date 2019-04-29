package com.example.fixmytrack;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class EngineerLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Intent intent;
    String username;
    String category;

    public void acceptJob(View view) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Report");

        query.whereEqualTo("username", intent.getStringExtra("username"));

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    if (objects.size() > 0) {

                        for (ParseObject object : objects) {

                            object.put("engineerUsername", ParseUser.getCurrentUser().getUsername());

                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if (e == null) {

                                        Intent intent = new Intent(getApplicationContext(), ReportDetails.class);


                                        intent.putExtra("username", username);
                                        intent.putExtra("category", category);

                                        startActivity(intent);

                                    }

                                }
                            });

                        }

                    }

                }

            }
        });

    }

    public void getDirections (View view){

        //this will get some directions, but maybe better to open a new intent with information??
        Intent directionsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + intent.getDoubleExtra("engineerLatitude", 0) + "," + intent.getDoubleExtra("engineerLongitude", 0) + "&daddr=" + intent.getDoubleExtra("reportLatitude", 0) + "," + intent.getDoubleExtra("reportLongitude", 0)));

                                        startActivity(directionsIntent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineer_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        intent = getIntent();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        RelativeLayout mapLayout = (RelativeLayout)findViewById(R.id.mapRelativeLayout);
        mapLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                LatLng engineerLocation = new LatLng(intent.getDoubleExtra("engineerLatitude", 0), intent.getDoubleExtra("engineerLongitude", 0));

                LatLng reportLocation = new LatLng(intent.getDoubleExtra("reportLatitude", 0), intent.getDoubleExtra("reportLongitude", 0));

                username = intent.getStringExtra("username");
                category = intent.getStringExtra("category");


                ArrayList<Marker> markers = new ArrayList<>();

                markers.add(mMap.addMarker(new MarkerOptions().position(engineerLocation).title("Your Location")));
                markers.add(mMap.addMarker(new MarkerOptions().position(reportLocation).title("Report Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : markers) {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();


                int padding = 60; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                mMap.animateCamera(cu);

            }
        });
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



    }
}


//    private GoogleMap mMap;
//
//    Intent intent;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_engineer_location);
//
//        intent = getIntent();
//
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//
//        //To prevent the error that the map hasnt been created yet  Annoyingly this has to be on a RelativeLayout not Contraint
//        RelativeLayout mapLayout = (RelativeLayout)findViewById(R.id.mapRelativeLayout);
//        mapLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//
//
//                //create a location from the info for engineer
//                LatLng engineerLoc = new LatLng(intent.getDoubleExtra("engineerLatitude", 0), intent.getDoubleExtra("engineerLongitude", 0));
//                //create a location for thr problem report location
//                LatLng reportLoc = new LatLng(intent.getDoubleExtra("reportLatitude", 0), intent.getDoubleExtra("reportLongitude", 0));
//
//                //create an array list of markers based on the location of problems
//
//                ArrayList<Marker> markers = new ArrayList<>();
//                markers.add(mMap.addMarker(new MarkerOptions().position(engineerLoc).title("Engineer is here")));
//                markers.add(mMap.addMarker(new MarkerOptions().position(reportLoc).title("Problem Report").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))));
//
//                LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
//                for (Marker marker : markers) {
//                    builder.include(marker.getPosition());
//                }
//
//                LatLngBounds bounds = builder.build();
//
//
//                int padding = 90; // offset from edges of the map in pixels
//                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//
//                mMap.animateCamera(cu);
//            }
//        });
//
//    }
//
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//
//
//
//
//    }
//
//    public void acceptJob(View view){
//
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Report");
//        query.whereEqualTo("username", intent.getStringExtra("username"));
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                if (e==null) {
//                    if (objects.size() > 0) {
//                        for (ParseObject object : objects){
//                            object.put("engineerUsername", ParseUser.getCurrentUser().getUsername()); // this updates the report to show its been picked up by an engineer
//                            object.saveInBackground(new SaveCallback() {
//                                @Override
//                                public void done(ParseException e) {
//                                    if (e==null){
//
//                                        //this will get some directions, but maybe better to open a new intent with information??
//                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?&addr=" + intent.getDoubleExtra("engineerLongitude", 0) + "," + intent.getDoubleExtra("engineerLatitude", 0) + "&addr=" + intent.getDoubleExtra("reportLongitude", 0) + "," + intent.getDoubleExtra("reportLatitude", 0)));
//                                        startActivity(intent);
//
//                                    }
//                                }
//                            });
//                        }
//                    }
//                }
//            }
//        });
//
//
//    }
//}
