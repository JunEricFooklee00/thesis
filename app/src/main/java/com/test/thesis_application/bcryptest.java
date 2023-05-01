package com.test.thesis_application;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.mindrot.jbcrypt.BCrypt;

public class bcryptest extends AppCompatActivity {

    Button hashthepasswordnow, check;
    TextView storethehash;
    EditText checkthehash, hashthepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcryptest);
        hashthepassword = findViewById(R.id.editTextTextPersonName);
        checkthehash = findViewById(R.id.editTextTextPersonName2);
        storethehash = findViewById(R.id.textView9);
        hashthepasswordnow = findViewById(R.id.button);
        check = findViewById(R.id.button2);

        hashthepasswordnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPassword = hashthepassword.getText().toString();
                String hashedPassword = hashPassword(inputPassword);
                storethehash.setText(hashedPassword);
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPassword = checkthehash.getText().toString();
                String hashedPassword = storethehash.getText().toString();
                boolean passwordMatch = verifyPassword(inputPassword, hashedPassword);
                if (passwordMatch) {
//                    Toast.makeText(bcryptest.this,"tama",Toast.LENGTH_LONG).show();
//                  Intent homeScreen = new Intent(MainActivity.this, employee_home.class);
//                                    homeScreen.putExtra("user_ID", idEmployee);
//                                    startActivity(homeScreen);
                } else {
                    Toast.makeText(bcryptest.this,"Wrong password",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String hashPassword(String input) {
        // Define the strength of the hashing algorithm
        int strength = 10;
        // Generate a salt value
        String salt = BCrypt.gensalt(strength);
        // Hash the password using the generated salt
        String hashedPassword = BCrypt.hashpw(input, salt);
        return hashedPassword;
    }

    private boolean verifyPassword(String inputPassword, String hashedPassword) {
        // Check if the input password matches the hashed password
        boolean passwordMatch = BCrypt.checkpw(inputPassword, hashedPassword);
        return passwordMatch;
    }
}
