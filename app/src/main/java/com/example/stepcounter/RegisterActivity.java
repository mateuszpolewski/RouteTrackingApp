package com.example.stepcounter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button register;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        confirmPassword = findViewById(R.id.confirmPassword);


        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_confirmPassword = confirmPassword.getText().toString();

                if(TextUtils.isEmpty(txt_email)|| TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(RegisterActivity.this, "Brak danych", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 0) {
                    Toast.makeText(RegisterActivity.this, "Hasło jest za krótkie", Toast.LENGTH_SHORT).show();
                } else if (!txt_password.equals(txt_confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Hasła się nie zgadzają", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_email, txt_password);
                }
            }
        });

        }
        private void registerUser(String email, String password) {
            auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Zarejstrowano użytkownika", Toast.LENGTH_SHORT).show();

                        /*
                        FirebaseUser user = auth.getCurrentUser();
                        //DatabaseReference usersRef = ref.child("Users");
                        Map<String, String> map = new HashMap<String, String>();
                        //map.put("provider", user.getProviderId()); //!!!!>>>>><<
                        Log.d(TAG, "onAuthStateChanged: " + user.getProviderData());
                        Log.d(TAG, "onAuthStateChanged: Display name : " + user.getDisplayName());
                        Log.d(TAG, "onAuthStateChanged: UID " + user.getUid());
                        ref.child("Users").child(user.getUid()).setValue(user.getProviderData());
                        */

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Błąd w rejestracji", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
