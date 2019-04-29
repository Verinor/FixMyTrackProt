package com.example.fixmytrack;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ReportList extends AppCompatActivity {

    ListView reportListView;
    ArrayList<String> reports = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    ArrayList<Double> reportLatitudes = new ArrayList<Double>();
    ArrayList<Double> reportLongitudes = new ArrayList<Double>();

    ArrayList<String> usernames = new ArrayList<String>();
    ArrayList<String> category = new ArrayList<>();
    ArrayList<String> objectId = new ArrayList<>();

    LocationManager locationManager;

    LocationListener locationListener;

    public void updateListView(Location location) {

        if (location != null) {


            ParseQuery<ParseObject> query = ParseQuery.getQuery("Report");

            final ParseGeoPoint geoPointLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

            query.whereNear("location", geoPointLocation);

            query.whereDoesNotExist("engineerUsername");


            query.setLimit(10);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (e == null) {

                        reports.clear();
                        reportLatitudes.clear();
                        reportLongitudes.clear();

                        if (objects.size() > 0) {

                            for (ParseObject object : objects) {

                                ParseGeoPoint reportLocation = (ParseGeoPoint) object.get("location");

                                if (reportLocation != null) {

                                    Double distanceInMiles = geoPointLocation.distanceInMilesTo(reportLocation);

                                    //this gets it to 1 decimal point
                                    Double distanceOneDP = (double) Math.round(distanceInMiles * 10) / 10;

                                    //String reportUsername = (String) object.get("username");
                                    String reportcategory = (String) object.get("category");
                                    String reportObjectId = (String)object.get("objectId");

                                    // add stuff to show on the list here like reportUsername etc / description of problem
                                    reports.add(distanceOneDP.toString() + " miles " + reportcategory);


                                    reportLatitudes.add(reportLocation.getLatitude());
                                    reportLongitudes.add(reportLocation.getLongitude());
                                    usernames.add(object.getString("username"));
                                    category.add(object.getString("category"));
                                    objectId.add(object.getString("objectId"));




                                }

                            }


                        } else {

                            reports.add("No active requests nearby");

                        }


                        arrayAdapter.notifyDataSetChanged();

                    }

                }
            });


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    updateListView(lastKnownLocation);

                }

            }


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        setTitle("Nearby Reports");

        reportListView = (ListView) findViewById(R.id.reportList);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, reports);



        reports.clear();

        reports.add("Getting nearby requests...");

        reportListView.setAdapter(arrayAdapter);

        reportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                  @Override
                                                  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                      if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(ReportList.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                                                          Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                                          if (reportLatitudes.size() > i && reportLongitudes.size() > i && usernames.size() > i && lastKnownLocation != null) {

                                                      Intent intent = new Intent(getApplicationContext(), EngineerLocation.class);

                                                     intent.putExtra("reportLatitude", reportLatitudes.get(i));
                                                     intent.putExtra("reportLongitude", reportLongitudes.get(i));
                                                     intent.putExtra("username", usernames.get(i));
                                                     intent.putExtra("category", category.get(i));
                                                     intent.putExtra("engineerLatitude", lastKnownLocation.getLatitude());
                                                     intent.putExtra("engineerLongitude", lastKnownLocation.getLongitude());
                                                      //intent.putExtra("objectId", objectId);

                                                      startActivity(intent);
//
//
//
//                                                              //Intent intent = new Intent(getApplicationContext(), EngineerLocation.class);
//                                                              Intent intent = new Intent(getApplicationContext(), EngineerLocation.class);
//
//                                                              intent.putExtra("reportLatitude", reportLatitudes.get(i));
//                                                              intent.putExtra("reportLongitude", reportLongitudes.get(i));
//                                                              intent.putExtra("engineerLatitude", lastKnownLocation.getLatitude());
//                                                              intent.putExtra("engineerLongitude", lastKnownLocation.getLongitude());
//                                                              intent.putExtra("username", usernames.get(i));
//                                                              //intent.putExtra("category", category.get(i));
//
//                                                              startActivity(intent);



                                                      }
                                                      }
                                                  }

        });




        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateListView(location);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (lastKnownLocation != null) {

                    updateListView(lastKnownLocation);

                }


            }


        }

    }


}


//    ArrayList<String> reports = new ArrayList<String>();
//    ArrayAdapter arrayAdapter;
//    ListView reportList;
//    LocationManager locationManager;
//    LocationListener locationListener;
//
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == 1) {
//
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//                    updateReports(lastKnownLocation);
//
//                }
//            }
//        }
//    }
//
//    public void updateReports(Location location){
//
//        if (location != null) {
//            reports.clear();
//    // Create a parse query, give it the engineer's locarion and
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Report");
//
//            final ParseGeoPoint reportsNearMe = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
//
//            query.whereNear(" location", reportsNearMe);
//
//            //could set a Limit
//            query.setLimit(10);
//            query.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> objects, ParseException e) {
//                    if (e == null){
//
//                        if (objects.size() > 0) {
//
//                            //Loop through the objects in the database
//                            for (ParseObject object : objects){
//                                //decide what information to show
//                                Double distanceInMiles = reportsNearMe.distanceInMilesTo((ParseGeoPoint) object.get("location"));
//                                //if want to round to a decimal place
//                                Double roundedDistance = (double) Math.round(distanceInMiles * 10)/10;
//                                reports.add(roundedDistance.toString() + "miles");
//
//                            }
//
//                        } else { //if there are no reports in the database
//                            reports.add("No Active Reports");
//                        }
//                        arrayAdapter.notifyDataSetChanged();
//
//                    }
//                }
//            });
//
//
//
//        }
//    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_report_list);
//
//        setTitle("Recent Local Reports");
//
//        reportList = findViewById(R.id.reportList);
//
//        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, reports);
//
//        reports.clear();
//        reports.add("Getting nearlby Reports");
//
//        reportList.setAdapter(arrayAdapter);
//
//
//
//        //GPS stuff
//
//        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//
//                updateReports(location);
//
//            }
//
//            @Override
//            public void onStatusChanged(String s, int i, Bundle bundle) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String s) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String s) {
//
//            }
//        };
//
//        if (Build.VERSION.SDK_INT < 23) {
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//        } else {
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//
//            } else {
//
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//                if (lastKnownLocation != null) {
//
//                    updateReports(lastKnownLocation);
//
//                }
//
//
//            }
//
//
//        }
//    }
//}
