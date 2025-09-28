package com.example.gonzalesmaridelle_option3;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import java.io.IOException;
import java.security.GeneralSecurityException;

// Utility class for accessing EncryptedSharedPreferences.
// This ensures all stored data is encrypted with AES-256.
public class SecurePreferences {

    public static SharedPreferences getEncryptedPrefs(Context context) {
        try {
            // Generate or retrieve a master key for encryption
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            // Create an EncryptedSharedPreferences instance
            return EncryptedSharedPreferences.create(
                    "UserData", // file name
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

        } catch (GeneralSecurityException | IOException e) {
            //if secure storage cannot be created, fail immediately
            throw new RuntimeException("Failed to create EncryptedSharedPreferences", e);
        }
    }
}