package com.yinkash.bioquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    //Declare a DatabaseHelper object to allow access to the methods of that class
    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*The variables below collect user input from the Login page and cast the input into a
        usable format*/
        final EditText etUserName = (EditText) findViewById(R.id.etUserName);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegisterHere);

        /*In the event the user has not registered before, by clicking on the Register Here link
        the user will switch Activity from the Login Activity to the Register Activity wi*/
        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*create an Intent; this intent will allow us to transition from the Login Activity to
                the Register Activity in the event this is the User's first interaction
                 */
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);

            }
        });

        //After entering your Login details and pressing the Login button, the code below is executed
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //store the entered UserName as a string
                String str = etUserName.getText().toString();
                //store the entered password as a string; this is the password entered into the Login form
                String pass = etPassword.getText().toString();

                //this line invokes a method which retrieves the corresponding password for a given UserName
                String password = helper.searchPass(str);

                //this loop will then compare the password entered into the Login form with the password retrieved
                if (pass.equals(password)) {
                    //if the password entered and the password retrieved match, the user will be taken to the Welcome activity
                    Intent uWelcomeIntent = new Intent(LoginActivity.this, UserWelcomeActivity.class);
                    uWelcomeIntent.putExtra("Username", str);
                    LoginActivity.this.startActivity((uWelcomeIntent));

                    SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", str);
                    editor.apply();

                    //Obtain the information of the player;Access and store their email address

                    Contact retrievedUser = helper.getContact(str);
                    String activeEmail = retrievedUser.getEmail();

                    SharedPreferences sharedPref1 = getSharedPreferences("userInfo1", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPref1.edit();
                    editor1.putString("email", activeEmail);
                    editor1.apply();
                } else {
                    //if the password entered and the password retrieved do no match an error message is produced
                    Toast.makeText(LoginActivity.this, "Username and password do not match", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
}
