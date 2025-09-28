package com.example.gonzalesmaridelle_option3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton, createAccountButton;
    TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.editTextText);
        passwordEditText = findViewById(R.id.editTextText2);
        loginButton = findViewById(R.id.button);
        createAccountButton = findViewById(R.id.button2);
        errorTextView = findViewById(R.id.textView2);

        // Initially hide the error message
        errorTextView.setVisibility(TextView.GONE);

        // Login button
        loginButton.setOnClickListener(v -> {
            String inputUsername = usernameEditText.getText().toString().trim();
            String inputPassword = passwordEditText.getText().toString().trim();

            if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                return; // Don't proceed if fields are empty
            }

            // Retrieve stored credentials
            SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
            String savedUsername = preferences.getString("username", "");
            String savedPassword = preferences.getString("password", "");

            if (inputUsername.equals(savedUsername) && inputPassword.equals(savedPassword)) {
                // Hide error message on successful login
                errorTextView.setVisibility(TextView.GONE);

                // Login success, navigate to MainActivity
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                // Show error message if login fails
                errorTextView.setVisibility(TextView.VISIBLE);
            }
        });

        // "Create Account" button
        createAccountButton.setOnClickListener(v -> {
            // Navigate to Create Account screen
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }
}


