package org.dut.exam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private static final String FIREBASE_TAG = "FireBaseAuthentication";

    /* --- FireBase --- */
    private FirebaseAuth mAuth;

    /* -- Formulaire -- */
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Récupère les champs
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);

        // Initialise FireBase
        mAuth = FirebaseAuth.getInstance();
    }

    public void onLoginClick(View view) {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(FIREBASE_TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Passe sur l'écran de sélection
                            startActivity(new Intent(Login.this, LevelSelection.class));
                        } else {
                            // Log l'erreur et prévient l'utilisateur
                            Log.w(FIREBASE_TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onLoginLinkClick(View view) {
        startActivity(new Intent(Login.this, Register.class));
    }
}