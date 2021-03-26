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
import com.google.firebase.auth.FirebaseUser;
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

                        birthDate.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(
                                calendar.getTime()
                        ));
                    }
                });
                datePickerDialog.show();
            }
        });
    }

    /**
     * Vérifie que tout les champs du formulaire soient remplis, ajoute une erreur si le champ
     * est vide.
     * @return True si le formulaire est remplis, false sinon.
     */
    private boolean isFormComplete() {
        // Champs à vérifier
        EditText[] editTexts = {firstName, lastName, birthDate, email, password};
        boolean formIsComplete = true;

        for(EditText editText : editTexts) {
            if(editText.getText().toString().isEmpty()) {
                formIsComplete = false;
                editText.setError("Ce champs est obligatoire.");
            }
        }

        return formIsComplete;
    }

    /**
     * Vérifie si la date de naissance donnée est valide, si non affiche une erreur.
     * @return True si la date est valide, false sinon.
     */
    private boolean isBirthDateValid() {
        try {
            DateFormat.getDateInstance(DateFormat.SHORT).parse(birthDate.getText().toString());
            return true;
        } catch (ParseException e) {
            birthDate.setError("Format de date invalide.");
            return false;
        }
    }

    private Map<String, Object> createUserFromActivityData(){
        Map<String, Object> user = new HashMap<>();

        // Récupère le nom et le prénom
        user.put("firstName", firstName.getText().toString());
        user.put("lastName", lastName.getText().toString());

        // Ajoute la date de naissance
        user.put("birthDate", birthDate.getText().toString());

        // Récupère le genre
        RadioButton selectedGender = findViewById(genders.getCheckedRadioButtonId());
        String gender = selectedGender.getText().toString().equals(this.getString(R.string.male_gender)) ? "Male":"Female";
        user.put("gender", gender);

        // Ajoute un meilleur score par défaut
        user.put("highscore", 0.0);

        return user;
    }

    public void onSignUpClick(View view) {
        if(isFormComplete() && isBirthDateValid()) {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(FIREBASE_TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                // Ajoute l'utilisateur à FireBase
                                database.collection("users")
                                        .document(user.getUid())
                                        .set(createUserFromActivityData());

                                // Passe sur l'écran de sélection
                                startActivity(new Intent(Register.this, LevelSelection.class));
                            } else {
                                // Log l'erreur et prévient l'utilisateur
                                Log.w(FIREBASE_TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

    public void onRegisterLinkClick(View view) {
        startActivity(new Intent(Register.this, Login.class));
    }
}