package org.dut.exam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Register extends AppCompatActivity {

    private static final String FIREBASE_TAG = "FireBaseAuthentication";

    /* --- FireBase --- */
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;

    /* --- Formulaire --- */
    private EditText email;
    private EditText password;
    private EditText firstName;
    private  EditText lastName;
    private EditText birthDate;
    private RadioGroup genders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialise FireBase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        // Récupère les champs d'inscription
        email = findViewById(R.id.emailTextEmailAddress);
        password = findViewById(R.id.passwordEditText);
        firstName = findViewById(R.id.firstNameEditText);
        lastName = findViewById(R.id.lastNameEditText);
        birthDate = findViewById(R.id.birthDateEditText);
        genders = findViewById(R.id.genderRadioGroup);

        // Initialise le datePicker
        findViewById(R.id.birthDatePickerImageButton).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, year);

                        birthDate.setText(DateFormat.getDateInstance().format(
                                calendar.getTime()
                        ));
                    }
                });
                datePickerDialog.show();
            }
        });
    }

    public void onSignUpClick(View view) {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnFailureListener(Register.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log l'erreur
                        Log.i(FIREBASE_TAG, String.format("User registration failed: %s", e.toString()));
                    }
                })
                .addOnSuccessListener(Register.this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Log l'inscription
                        Log.i(FIREBASE_TAG, String.format("User %s registered successfully.", mAuth.getCurrentUser().getUid()));
                    }
                });
    }

}