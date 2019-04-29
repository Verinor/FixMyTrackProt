package com.example.fixmytrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Switch;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    String userType = "";

    public void redirectActivity() {


        //Redirects as a user
        if (ParseUser.getCurrentUser().getString("userType").equals("walker")) {

            Intent intent = new Intent(getApplicationContext(), WalkerReport.class);
            startActivity(intent);

            //Redirects as an Engineer - also can be used for a report list
        } else {
            Intent intent = new Intent(getApplicationContext(), ReportList.class);
            startActivity(intent);
        }
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();


        switch (view.getId()){

            case R.id.radioWalkers:
                if (checked)
                    userType = "walker";
                    ParseUser.getCurrentUser().put("userType", userType);

                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        Intent intent = new Intent(getApplicationContext(), WalkerReport.class);
                        startActivity(intent);

                    }
                });

                break;
            case R.id.radioCyclist:
                if (checked)
                    userType = "cyclist";
                ParseUser.getCurrentUser().put("userType", userType);

                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        Intent intent = new Intent(getApplicationContext(), CyclistReport.class);
                        startActivity(intent);

                    }
                });
                break;

        }

        //Switch userTypeSwitch = (Switch) findViewById(R.id.userTypeSwitch);

        //Log.i("Switch value", String.valueOf(userTypeSwitch.isChecked()));

//        String userType = "user";
//
//        if (userTypeSwitch.isChecked()) {
//
//            userType = "engineer";
//
//        }

//        ParseUser.getCurrentUser().put("userOrEngineer", userType);
//
//        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//
//                redirectActivity();
//
//            }
//        });

    }


    public void engineerLogin (View view){

        userType = "engineer";
        ParseUser.getCurrentUser().put("userType", userType);

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                Intent intent = new Intent(getApplicationContext(), EngineerLogin.class);
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();



        if (ParseUser.getCurrentUser() == null) {

            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (e == null) {

                        Log.i("Info", "Anonymous login successful");

                    } else {

                        Log.i("Info", "Anonymous login failed");

                    }


                }
            });

        } else {

            if (ParseUser.getCurrentUser().get("userType") != null) {

                Log.i("Info", "Redirecting as " + ParseUser.getCurrentUser().get("userType"));

                redirectActivity();

            }


        }


        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

}


//    //Redirect Activity if needed
//    public void redirect(){
//        if (ParseUser.getCurrentUser().getString("userType").equals("walker")){
//            Intent intent = new Intent(getApplicationContext(), WalkerReport.class);
//            startActivity(intent);
//
//
//        }
//    }
//
//    public void walkerStart(View view){
//
//        String userType = "walker";
////        Intent intent = new Intent(getApplicationContext(), WalkerReport.class);
////        startActivity(intent);
//
//        ParseUser.getCurrentUser().put("userType", userType);
//        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                redirect();
//            }
//        });
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        //include to remove top actionbar?
//        //getSupportActionBar().hide();
//
//        if (ParseUser.getCurrentUser() == null) {
//
//            ParseAnonymousUtils.logIn(new LogInCallback() {
//                @Override
//                public void done(ParseUser user, ParseException e) {
//                    if (e == null) {
//                        Log.i("Info", "Anon Login Success");
//                    } else {
//                        Log.i("Info", "Failed");
//                    }
//                }
//            });
//
//        } else {
//
//        }
//
//
//        ParseAnalytics.trackAppOpenedInBackground(getIntent());
//    }
//
//}
