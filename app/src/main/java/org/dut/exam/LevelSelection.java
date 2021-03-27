package org.dut.exam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class LevelSelection extends AppCompatActivity {

    private static final String FIREBASE_TAG = "FireBaseAuthentication";

    /* --- FireBase --- */
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private FirebaseUser currentUser;

    /* -- Bundle --- */
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

        intent = new Intent(LevelSelection.this, Game.class);
        bundle = new Bundle();

        // Initialise FireBase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        // Récupère le score de l'utilisateur
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reloadWithUserData();
        }

        // Mode facile
        findViewById(R.id.easyGameModeButton).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadGame(2, 2, 0.0, 1.0, 1, 1, 3, 1, false);
                }
            }
        );

        // Mode difficile
        findViewById(R.id.hardGameModeButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadGame(2, 2, 0.0, 1.5, 1, 3, 15, 1, false);
                    }
                }
        );

        // Mode expert
        findViewById(R.id.expertGameModeButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadGame(3, 3, 0.0, 3, 1, 5, 20, 1, false);
                    }
                }
        );

        // Mode chrono
        findViewById(R.id.timedGameModeButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadGame(3, 3, 0.0, 2, 1, 1, 99, 1, true);
                    }
                }
        );

    }

    private void reloadWithUserData(){
        DocumentReference docRef = database.collection("saves").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(FIREBASE_TAG, "DocumentSnapshot data: " + document.getData());

                        // Affiche le bouton "reprendre"
                        Button loadSavedGameButton = findViewById(R.id.loadSavedGameButton);
                        loadSavedGameButton.setVisibility(View.VISIBLE);

                        loadSavedGameButton.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        loadGame(
                                                ((Long)document.get("maxHealth")).intValue(),
                                                ((Long)document.get("health")).intValue(),
                                                (double)document.get("score"),
                                                (double)document.get("scoreWeight"),
                                                ((Long)document.get("sequenceSize")).intValue(),
                                                ((Long)document.get("minSequence")).intValue(),
                                                ((Long)document.get("maxSequence")).intValue(),
                                                ((Long)document.get("level")).intValue(),
                                                (boolean)document.get("isTimed")
                                        );
                                    }
                                }
                        );
                    }
                }
            }
        });

        docRef = database.collection("users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(FIREBASE_TAG, "DocumentSnapshot data: " + document.getData());

                        // Active le highScore
                        TextView highScoreTextView = findViewById(R.id.highScoreTextView);
                        highScoreTextView.setVisibility(View.VISIBLE);

                        // Met à jour le meilleur score du joueur
                        highScoreTextView.setText(String.format(
                                Locale.getDefault(), "%s %1.1f",
                                LevelSelection.this.getString(R.string.high_score_template),
                                document.getDouble("highScore"))
                        );

                    }
                }
            }
        });
    }

    private void loadGame(
            int maxHealth, int health, double score, double scoreWeight, int sequenceSize, int minSequence, int maxSequence, int level, boolean timed) {

        // Remplit le bundle
        bundle.putInt("maxHealth", maxHealth);
        bundle.putInt("health", health);
        bundle.putDouble("score", score);
        bundle.putDouble("scoreWeight", scoreWeight);
        bundle.putInt("sequenceSize", sequenceSize);
        bundle.putInt("minSequence", minSequence);
        bundle.putInt("maxSequence", maxSequence);
        bundle.putInt("level", level);
        bundle.putBoolean("isTimed", timed);

        // Lance le jeu
        intent.putExtras(bundle);
        startActivity(intent);
    }
}