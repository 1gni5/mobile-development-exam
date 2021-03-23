package org.dut.exam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore database;

    EditText email;
    EditText password;
    EditText lastname, firstname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialise FireBase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        // Récupère les champs
        EditText birthDate = (EditText)findViewById(R.id.birthDateEditText);
        ImageButton birthDatePickerButton = (ImageButton)findViewById(R.id.birthDatePickerImageButton);
        email = (EditText)findViewById(R.id.emailEditText);
        password = (EditText)findViewById(R.id.passwordEditText);
        lastname = (EditText)findViewById(R.id.lastNameEditText);
        firstname = (EditText)findViewById(R.id.firstNameEditText);

        // Ajoute les listeners
        birthDatePickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);

                        birthDate.setText(DateFormat.getDateInstance().format(calendar.getTime()));
                    }
                });
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        // FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }

    private void createAccount(String email, String password) {
        // Créer un nouvel utilisateur
        Map<String, Object> user = new HashMap<>();
        user.put("firstName", firstname);
        user.put("lastName", lastname);
        user.put("birthDate", Calendar.getInstance().getTime());
        user.put("email", email);
        user.put("password", password);

        // Authentification
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Sign-up success !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Register.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Ajoute les informations à la base
        database.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Register.this, "Info written !", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onSignUpClick(View view) {
        createAccount("example2@mail.fr", "motdepasse123");
    }

}