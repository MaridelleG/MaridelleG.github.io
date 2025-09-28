package com.example.gonzalesmaridelle_option3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gonzalesmaridelle_option3.R;

public class SMSPermissionActivity extends AppCompatActivity {

    private TextView textView7, textView8;
    private Button buttonAllow, buttonDontAllow;

    private static final int SMS_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_permission);

        textView7 = findViewById(R.id.textView7);  // "SMS permission denied" TextView
        textView8 = findViewById(R.id.textView8);  // "SMS permission granted" TextView
        buttonAllow = findViewById(R.id.button3);  // "Allow" Button
        buttonDontAllow = findViewById(R.id.button4);  // "Don't Allow" Button

        // Check if SMS permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted
            textView8.setVisibility(View.VISIBLE);
            textView7.setVisibility(View.GONE);
        } else {
            // Request permission if not granted
            textView8.setVisibility(View.GONE);
            textView7.setVisibility(View.GONE);
        }

        // Handle "Allow" button click
        buttonAllow.setOnClickListener(v -> {
            // Request SMS permission
            ActivityCompat.requestPermissions(SMSPermissionActivity.this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        });

        // Handle "Don't Allow" button click
        buttonDontAllow.setOnClickListener(v -> {
            // Deny permission
            textView7.setVisibility(View.VISIBLE);
            textView8.setVisibility(View.GONE);
            Toast.makeText(SMSPermissionActivity.this, "SMS Permission Denied", Toast.LENGTH_SHORT).show();
        });
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                textView8.setVisibility(View.VISIBLE);
                textView7.setVisibility(View.GONE);
                Toast.makeText(this, "SMS Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                textView7.setVisibility(View.VISIBLE);
                textView8.setVisibility(View.GONE);
                Toast.makeText(this, "SMS Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
