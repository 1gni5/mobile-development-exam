package org.dut.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game extends AppCompatActivity {

    public static final byte NUMBER_OF_BUTTONS = 10;
    public static final byte MAXIMUM_LEVEL = 7;
    public static final byte DEFAULT_BUTTONS_NUMBER = 3;

    private byte currentLevel = 1;

    private ArrayList<Button> allGameButtons = new ArrayList<>(NUMBER_OF_BUTTONS);
    private ArrayList<Button> activeGameButtons = new ArrayList<>();
    private ArrayList<Button> currentSequence = new ArrayList<>();

    private FloatingActionButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Récupère tout les boutons du layout
        for(int i = 1; i <= NUMBER_OF_BUTTONS; i++) {
            // Construit l'identifiant et récupère l'id
            String identifier = "gameButton" + i;
            int buttonId = getResources().getIdentifier(identifier, "id", getPackageName());

            allGameButtons.add(findViewById(buttonId));
        }

        // Active le listener du playButton
        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(
                (view)->{gameStart();}
        );

        // Si la liste des boutons actifs n'est pas remplit
        if (activeGameButtons.size() == 0 ) {
            generatesActiveGameButtons(DEFAULT_BUTTONS_NUMBER + currentLevel);
        }

        // Affiche les boutons actifs
        for(Button button : activeGameButtons) {
            button.setVisibility(View.VISIBLE);
        }
    }

    private void gameStart() {
        Toast.makeText(this, "Time to play!", Toast.LENGTH_SHORT).show();

        // Cache le bouton
        playButton.setVisibility(View.INVISIBLE);

        generateNewSequence(currentLevel + 3);
        animateGameSequence(0, false);
    }

    private void animateGameSequence(int index, boolean reverseCall) {

        // Récupère le bouton à animer
        Button button = this.currentSequence.get(index);

        button.animate()
                .setStartDelay(50)
                .scaleX( (reverseCall) ? 1.0f : 1.075f)
                .scaleY( (reverseCall) ? 1.0f : 1.075f)
                .setDuration(250)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if(!reverseCall) {
                            /*
                            Si reverseCall est faux il s'agit de la première animation
                            On appelle rappelle donc l'animation dans l'autre sens.
                            */
                            animateGameSequence(index, true);
                            button.setText(button.getText() + " " + index);
                        } else if(index + 1 < currentSequence.size()) {
                            /*
                            À l'inverse si reverseCall est vrai, on vient de clore l'animation
                            précédente et on peux donc passer au prochain élément.
                             */
                            animateGameSequence(index + 1, false);
                        }
                    }
                });
    }

    /**
     * Génère une séquence aléatoire en utilisant les boutons actifs.
     * @param lengthOfSequence Longueur de la séquence.
     */
    private void generateNewSequence(int lengthOfSequence) {
        // Prépare le générateur aléatoire
        Random randomGenerator = new Random();

        // Tire lengthOfSequence boutons de manière aléatoire
        for(int i = 0; i < lengthOfSequence; i++) {
            this.currentSequence.add(
                    activeGameButtons.get(randomGenerator.nextInt(activeGameButtons.size()))
            );
        }
    }

    /**
     * Génère la liste des boutons actifs en tirant aléatoirement des boutons parmi la liste de tout
     * les boutons du jeu.
     * @param numberOfButtons Le nombre de boutons à tirer.
     */
    protected void generatesActiveGameButtons(int numberOfButtons) {
        // Copie la liste des boutons disponible
        ArrayList<Button> availableGameButtons = (ArrayList<Button>) this.allGameButtons.clone();

        // Prépare le générateur aléatoire
        Random randomGenerator = new Random();

        // Tire numberOfButtons boutons de manière aléatoire
        for(int i = 0; i < numberOfButtons; i++) {
            int index = randomGenerator.nextInt(availableGameButtons.size());
            this.activeGameButtons.add(availableGameButtons.remove(index));
        }
    }
}