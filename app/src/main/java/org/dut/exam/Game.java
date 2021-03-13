package org.dut.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game extends AppCompatActivity {

    public static final byte NUMBER_OF_BUTTONS = 10;
    public static final byte MAXIMUM_LEVEL = 7;
    public static final byte DEFAULT_BUTTONS_NUMBER = 3;

    private ArrayList<Button> allGameButtons = new ArrayList<>(NUMBER_OF_BUTTONS);
    private ArrayList<Button> activeGameButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Récupère tout les boutons du layout
        for(int i = 1; i <= NUMBER_OF_BUTTONS; i++) {
            // Construit l'identifiant et récupère l'id
            String identifier = "gameButton" + i;
            int buttonId = getResources().getIdentifier(identifier, "id", getPackageName());

            allGameButtons.add((Button)findViewById(buttonId));
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

    public void gameStart(View view) {
        // Cache le bouton "Play"
        findViewById(R.id.playFloatingActionButton).setVisibility(View.INVISIBLE);

        // Génére la liste des boutons actifs
    }
}