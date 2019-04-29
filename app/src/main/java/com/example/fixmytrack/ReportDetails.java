package com.example.fixmytrack;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ReportDetails extends AppCompatActivity {

    Intent intent;
    TextView txvCategory;
    TextView txvDescription;
    LinearLayout linLayout;
    String objectId;
    Button btnEngineerMap;
    String username;
    String category;






//    public void updateDetailView() {
//
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Report");
//
//            query.whereEqualTo("objectId", objectId);
//
//            query.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> objects, ParseException e) {
//
//
//                    for(ParseObject object: objects){
//                    String reportCategory = (String) object.get("category");
//                    String userName = (String) object.get("username");
//
//                    txvCategory.setText(reportCategory.toString());
//                    txvDescription.setText(userName);
//                }
//            }});
//    }

//            query.whereEqualTo("objectId", objectId);
//
//            query.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> objects, ParseException e) {
//
//                    if (e == null) {
//
//                        if (objects.size() > 0) {
//
//                            for (ParseObject object : objects) {
//
//
//                                if (objects != null) {
//
//                                    String reportCategory = (String) object.get("category");
//                                    String userName = (String) object.get("username");
//
//                                    txvCategory.setText(reportCategory.toString());
//                                    txvDescription.setText(userName);
//
//
////                                    usernames.add(object.getString("username"));
////                                    category.add(object.getString("category"));
////                                    objectId.add(object.getString("objectId"));
//
//
//
//
//                                }
//
//                            }
//
//
//                        }
//
//
//                        }
//
//
//
//
//                    }
//
//
//            });





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        txvCategory = findViewById(R.id.txvCategory);
        txvDescription = findViewById(R.id.txvDescription);
        linLayout = findViewById(R.id.linLayout);




        intent = getIntent();
        //updateDetailView();

//        String categoryTitle = new String(intent.getStringExtra("category"));
//        objectId = intent.getStringExtra("objectId");
//        //LatLng reportLocation = new LatLng(intent.getDoubleExtra("reportLatitude", 0), intent.getDoubleExtra("reportLongitude", 0));
//
//        txvCategory.setText(categoryTitle.toString());


        //check which user
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        category = intent.getStringExtra("category");


        setTitle(category);

        linLayout = findViewById(R.id.linLayout);



        //go get images
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Report");
        query.whereEqualTo("username", username);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject object : objects) {
                        ParseFile file = (ParseFile) object.get("image");
                        final String reportcategory = (String) object.get("category");
                        final String reportDescription = (String) object.get("description");

                        //download the image in the file
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null && data != null ) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                    //Programmatically set up the imageviews
                                    ImageView imageView = new ImageView(getApplicationContext());

//                                    imageView.setLayoutParams(new ViewGroup.LayoutParams(
//
//
//                                            ViewGroup.LayoutParams.MATCH_PARENT,
//                                            ViewGroup.LayoutParams.WRAP_CONTENT
//                                    ));

                                    imageView.setImageBitmap(bitmap);
                                    imageView.setPadding(20, 30,40, 0);
                                    linLayout.addView(imageView);
                                    txvCategory.setText(reportcategory);
                                    txvDescription.setText(reportDescription);
                                }
                            }
                        });
                    }
                }
            }
        });


}}
