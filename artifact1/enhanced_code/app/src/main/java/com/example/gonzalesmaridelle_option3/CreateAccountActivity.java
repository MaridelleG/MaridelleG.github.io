package com.example.gonzalesmaridelle_option3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//Activity for creating a new user account.
//Stores username and password securely using EncryptedSharedPreferences

public class CreateAccountActivity extends AppCompatActivity {

    EditText newUsername, newPassword;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Back button returns to LoginActivity
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
        });

        newUsername = findViewById(R.id.editTextUsername);
        newPassword = findViewById(R.id.editTextPassword);
        saveBtn = findViewById(R.id.buttonSave);

        saveBtn.setOnClickListener(v -> {
            String username = newUsername.getText().toString().trim();
            String password = newPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            } else {
                //Use secure encrypted storage
                SharedPreferences preferences = SecurePreferences.getEncryptedPrefs(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", username);
                editor.putString("password", password); // Stored encrypted automatically
                editor.apply();

                Toast.makeText(this, "Account created securely!", Toast.LENGTH_SHORT).show();

                // Redirect back to LoginActivity
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}


