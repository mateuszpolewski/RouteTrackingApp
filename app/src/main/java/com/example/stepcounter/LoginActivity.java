package com.example.stepcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stepcounter.dao.RouteEntryDAO;
import com.example.stepcounter.model.RouteEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private EditText email;
    private EditText password;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);


        //new RouteEntryDAO().addToDatabase(new RouteEntry("a", "1", 2, 3, 4, 5));

    login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String txt_email = email.getText().toString();
            String txt_password = password.getText().toString();

            if(TextUtils.isEmpty(txt_email)|| TextUtils.isEmpty(txt_password)) {
                Toast.makeText(LoginActivity.this, "Brak danych", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(txt_email, txt_password);
            }
        }
    });
    }
    private void loginUser(String email, String password) {
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Poprawnie zalogowano", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, UserMenuActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this,"Błędne dane logowania", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
