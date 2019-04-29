package com.example.fixmytrack;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class EngineerLogin extends AppCompatActivity implements View.OnClickListener {


    //Boolean signUpModeActive = true;
    TextView txvLogin;
    EditText edtUsername;
    EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineer_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        //txvLogin = findViewById(R.id.txvLogin);
        ConstraintLayout backgroundLayout = findViewById(R.id.backgroundLayout);

        //setup click listeners

        backgroundLayout.setOnClickListener(this);

    }

    public void signUp(View view) {



        if (edtUsername.getText().toString().matches("") || edtPassword.getText().toString().matches("")) {

            Toast.makeText(this, "Please fill in both Username and Password", Toast.LENGTH_SHORT).show();

        } else {
            ParseUser.logInInBackground(edtUsername.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if( user != null) {
                            Log.i("Login", "ok");
                            showReportList();

                        } else {
                            ParseUser.logOut();
                            Toast.makeText(EngineerLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }


    public  void showReportList(){
        Intent intent = new Intent(getApplicationContext(), ReportList.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.backgroundLayout){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
     }
    }

//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.txvLogin){
//
//            Button btnSignUp = findViewById(R.id.btnSignUp);
//            // if statement to switch between login and signup
//            if (signUpModeActive){
//                signUpModeActive = false;
//                btnSignUp.setText("Login");
//                txvLogin.setText("or, Signup");
//            } else {
//                signUpModeActive = true;
//                btnSignUp.setText("Signup");
//                txvLogin.setText("Login");
//            }
//            //else if to kide the keyboard if the background or the logo are clicked
//        } else if (v.getId()== R.id.backgroundLayout){
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        }
//
//    }
//
//    @Override
//    public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//        //check to see if it is the enter button that is pressed and that it is when pressed
//        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() ==KeyEvent.ACTION_DOWN) {
//
//            signUp(v);
//        }
//        return false;
//    }
}
