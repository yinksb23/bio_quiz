package com.yinkash.bioquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    //Declare an object of the DatabaseHelper class to access the methods
    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Collect and store the entered info into EditText variables
        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etUsername = (EditText) findViewById(R.id.etUserName);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etPassword2 = (EditText) findViewById(R.id.etPassword2);

        final Button bRegister = (Button) findViewById(R.id.bRegister);

        //When the Register button is clicked, the code below is executed
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String namestr = etName.getText().toString();
                final String emailstr = etEmail.getText().toString();
                final String usernamestr = etUsername.getText().toString();
                final String passwordstr = etPassword.getText().toString(); //User enters password the first time
                final String password2str = etPassword2.getText().toString(); //User must enter the password once more to confirm


                //If the two entered passwords fail to match, the code below is executed
                if (!passwordstr.equals(password2str)) {
                    //pop up msg
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else //If the passwords do match, the UserInput is stored in the Contact class
                {
                    //Here we will set the Contact details to that entered by the user
                    Contact c = new Contact();
                    c.setName(namestr);
                    c.setEmail(emailstr);
                    c.setUname(usernamestr);
                    c.setPass(passwordstr);

                    /*The Contact class is then passed to the DatabaseHelper class for injection into the database*/
                    helper.insertContact(c);
                }

            }
        });
    }
}
