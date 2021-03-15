package org.dut.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class Game extends AppCompatActivity {

    public static final byte NUMBER_OF_BUTTONS = 10;
    public static final byte NUMBER_OF_HEART = 3;
    public static final byte MAXIMUM_LEVEL = 7;
    public static final byte DEFAULT_BUTTONS_NUMBER = 3;

    private byte currentLevel = 1;
    private GameMode gameMode = GameMode.EASY;

    private ArrayList<Button> allGameButtons = new ArrayList<>(NUMBER_OF_BUTTONS);
    private ArrayList<Button> activeGameButtons = new ArrayList<>();

    private ArrayList<Button> currentSequence = new ArrayList<>();
    private ArrayList<Button> playerSequence = new ArrayList<>();

    private ArrayList<ImageView> heartSprites = new ArrayList<>(NUMBER_OF_HEART);

    private FloatingActionButton playButton;

    Hashtable<GameMode, Integer> gameModeToHearts = new Hashtable<>();

    public Game() {
        /* --- Initialise les HashTable --- */

        // Prépare les 2 listes
        GameMode[] gameModes = GameMode.values();
        int[] numberOfHearts = {2, 2, 3, 3};

        // Remplit la hashtable
        for(int index = 0; index < numberOfHearts.length; index++) {
            gameModeToHearts.put(gameModes[index], numberOfHearts[index]);
        }
    }

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

        // Affiche tout les coeurs sur le layout
        for(int i = 1; i <= gameModeToHearts.get(gameMode); i++) {
            // Construit l'identifiant et récupère l'id
            String identifier = "lifeImageView" + i;
            int heartImageViewId = getResources().getIdentifier(identifier, "id", getPackageName());

            heartSprites.add(findViewById(heartImageViewId));
        }

        for(ImageView heartSprite : heartSprites) {
            heartSprite.setVisibility(View.VISIBLE);
        }

        // Active le listener du playButton
        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(
                (view)->{gameStart();}
        );

        // Si la liste des boutons actifs n'est pas remplie
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

    /**
     * Anime la séquence actuelle en s'appellant jusqu'à avoir parcouru tout les boutons de la
     * séquence. Pour chaque bouton la méthode est utilisé 2 fois (sens normal puis inverse).
     * @param index Indice de départ de l'animation, surtout utilisé pour l'appel récursif.
     * @param reverseCall Si vrai inverse le sens de l'animation, surtout utilisé pour l'appel récursif.
     */
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

    public void onGameButtonClick(View view) {
        // Récupère le bouton cliqué
        Button button = findViewById(view.getId());

        if(playerSequence.size() + 1 < currentSequence.size()) {
            // Ajoute le bouton à la séquence du joueur
            playerSequence.add(button);
        } else {
            boolean sequenceIsGood = true;

            for(int index = 0; index < playerSequence.size(); index++) {
                sequenceIsGood &= playerSequence.get(index).equals(currentSequence.get(index));
            }

            Toast.makeText(this, sequenceIsGood ? "You won !":"You loose !", Toast.LENGTH_SHORT).show();
        }
    }
}