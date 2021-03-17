package org.dut.exam;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Game extends AppCompatActivity {

    /* --- Constantes du jeu --- */
    private static final byte NUMBER_OF_BUTTONS = 10;
    private static final byte NUMBER_OF_HEART = 3;
    private static final byte MAXIMUM_LEVEL = 7;
    private static final byte DEFAULT_BUTTONS_NUMBER = 3;

    /* --- Constantes de partie --- */
    private static final byte MIN_SEQUENCE_LENGTH = 1;
    private static final byte MAX_SEQUENCE_LENGTH = 10;
    private static final byte MAX_HEALTH = 2;
    private static final byte SCORE_WEIGHT = 1;

    /* --- Informations de la partie --- */
    private byte level;
    private byte score;
    private byte health;

    /* --- Listes de boutons --- */
    private ArrayList<Button> allGameButtons = new ArrayList<>(NUMBER_OF_BUTTONS);
    private final ArrayList<Button> activeGameButtons = new ArrayList<>();
    private ArrayList<Button> availableGameButtons = new ArrayList<>();
    private ArrayList<Button> computerSequenceButtons = new ArrayList<>();
    private final ArrayList<Button> playerSequenceButtons = new ArrayList<>();

    /* --- Éléments graphique --- */
    private FloatingActionButton playButton;
    private TextView scoreTextView;
    private  TextView levelTextView;
    private ArrayList<ImageView> healthBar = new ArrayList<>(NUMBER_OF_HEART);

    Random randomGenerator = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialise et affiche le score
        scoreTextView = findViewById(R.id.scoreTextView);
        score = 0;

        // TODO: Passer en Setter ? Doit-être appelé à chaque changement de "score".
        scoreTextView.setText(String.format(Locale.getDefault(),"%s %d", this.getString(R.string.score_template), score));

        // Initialise et affiche le niveau
        levelTextView = findViewById(R.id.levelTextView);
        level = 1;

        // TODO: Passer en Setter ? Doit-être appelé à chaque changement de "level".
        levelTextView.setText(String.format(Locale.getDefault(),"%s %d", this.getString(R.string.level_template), level));

        // Récupère tout les boutons du jeu
        this.allGameButtons = getViewFromIdPattern("gameButton", NUMBER_OF_BUTTONS);

        // Initialise les boutons actifs et disponibles
        this.availableGameButtons = (ArrayList<Button>) this.allGameButtons.clone();

        // Tire numberOfButtons boutons de manière aléatoire
        for(int i = 0; i < DEFAULT_BUTTONS_NUMBER + level; i++) {
            int index = randomGenerator.nextInt(availableGameButtons.size());
            this.activeGameButtons.add(availableGameButtons.remove(index));
        }

        // Affiche les boutons actifs
        for(Button button : activeGameButtons) {
            button.setVisibility(View.VISIBLE);
        }
        
        // Initialise le niveau de vie et l'affiche
        this.healthBar = getViewFromIdPattern("lifeImageView", NUMBER_OF_HEART);
        health = MAX_HEALTH;
        refreshHealthBar();

        // Initialise le bouton "play"
        playButton = findViewById(R.id.playButton);
    }

    /**
     * Récupère un ensemble d'éléments dont l'id corresponds à un "patternX".
     * @param pattern Paterne de l'id dans R.
     * @param numberOfElements Nombre d'éléments à récupérer.
     * @param <T> Type des éléments à récupérer, un View doit être castable en T.
     * @return La liste d'éléments.
     */
    private <T> ArrayList<T> getViewFromIdPattern(String pattern, int numberOfElements) {

        // Prépare la liste des résultats
        ArrayList<T> result = new ArrayList<T>(numberOfElements);

        for(int i = 1; i <= numberOfElements; i++) {

            // Construit l'identifiant et récupère l'id
            String identifier = pattern + i;
            int viewId = getResources().getIdentifier(identifier, "id", getPackageName());

            result.add( (T)findViewById(viewId));
        }

        return result;
    }

    /**
     * Rafraichit l'affichage de la bar de vie, assure que le nombre de coeurs affiché est cohérent
     * avec le niveau de vie du joueur.
     *
     * TODO: Passer cette fonction à l'intérieur d'un Setter ?
     *      Étant donné que cette fonction garantie la cohérence de l'affichage elle doit-être appelé
     *      à chaque changement de "health".
     */
    private void refreshHealthBar(){
        // Cache tout les coeurs
        for(ImageView heartSprite : healthBar) {
            heartSprite.setVisibility(View.GONE);
        }

        // Dessine les uniquement les coeurs nécessaires
        for(int index = 0; index < health; index++) {
            healthBar.get(index).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Méthode appelé au click sur le bouton "Play". Cache le bouton et lance une partie.
     * @param view playButton
     */
    public void onPlayButtonClick(View view) {
        Toast.makeText(this, "Time to play!", Toast.LENGTH_SHORT).show();

        // Cache le bouton
        playButton.setVisibility(View.INVISIBLE);

        // generateNewSequence(computerSequenceButtons.size() + 1);
        computerSequenceButtons = getRandomListFromValues(activeGameButtons, MIN_SEQUENCE_LENGTH);
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
        Button button = this.computerSequenceButtons.get(index);

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

                            // TODO: Cette ligne doit être retirer à la fin du développement
                            button.setText(button.getText() + " " + index);
                        } else if(index + 1 < computerSequenceButtons.size()) {
                            /*
                            À l'inverse si reverseCall est vrai, on vient de clore l'animation
                            précédente et on peux donc passer au prochain élément.
                             */
                            animateGameSequence(index + 1, false);
                        } else {
                            // Déverrouille tout les boutons
                            for(Button activeButton : activeGameButtons) {
                                activeButton.setEnabled(true);
                            }
                        }
                    }
                });
    }

    /**
     * Créer une liste aléatoire d'une taille donnée composé de valeurs tiré dans une liste donnée.
     * @param values Valeurs d'origine.
     * @param numberOfElements Nombre d'éléments que la liste aléatoire doit contenir.
     * @param <T> Type des listes : d'origine et de sortie.
     * @return Liste aléatoire d'élément T.
     */
    private <T> ArrayList<T> getRandomListFromValues(List<T> values, int numberOfElements) {

        // Prépare la liste de résultat et le générateur
        ArrayList<T> result = new ArrayList<>(numberOfElements);

        // Tire un élément de manière aléatoire dans "values"
        for(int i = 0; i < numberOfElements; i++) {
            result.add(
                    (T) values.get(randomGenerator.nextInt(values.size()))
            );
        }

        return result;
    }

    private void onSequenceEnd() {

        // Verrouille tout les boutons
        for(Button button : activeGameButtons) {
            button.setEnabled(false);

            // Clean les indices
            // TODO: Enlever les indices à la fin du dév
            button.setText("");
        }

        if(playerSequenceButtons.equals(computerSequenceButtons)) {

            if(playerSequenceButtons.size() == MAX_SEQUENCE_LENGTH) {
                onNextLevel();
            } else {
                // Lance la séquence suivante
                computerSequenceButtons = getRandomListFromValues(
                        activeGameButtons, computerSequenceButtons.size() + 1);
                animateGameSequence(0, false);
            }
        } else if(health > 0) {

            // Actualise la barre de vie
            health -= 1;
            refreshHealthBar();

            // Relance une nouvelle séquence
            computerSequenceButtons = getRandomListFromValues(
                    activeGameButtons, computerSequenceButtons.size());
            animateGameSequence(0, false);

        } else {
            Toast.makeText(this,"You loose !", Toast.LENGTH_SHORT).show();
        }

        // Nettoie la tentative du joueur
        playerSequenceButtons.clear();
    }

    private void onNextLevel() {
        if(level < MAXIMUM_LEVEL) {

            // Met à jour le score
            score += level * SCORE_WEIGHT;
            scoreTextView.setText(
                    String.format(Locale.getDefault(),"%s %d", this.getString(R.string.score_template), score));

            // Met à jour le niveau
            level += 1;
            levelTextView.setText(
                    String.format(Locale.getDefault(),"%s %d", this.getString(R.string.level_template), level));
            
            // Ajoute un bouton
            Button newGameButton = availableGameButtons.remove(
                    randomGenerator.nextInt(availableGameButtons.size()));
            newGameButton.setVisibility(View.VISIBLE);
            activeGameButtons.add(newGameButton);

            // Actualise la barre de vie
            health = MAX_HEALTH;
            refreshHealthBar();

            // Lance la séquence suivante
            computerSequenceButtons = getRandomListFromValues(
                    activeGameButtons, MIN_SEQUENCE_LENGTH);
            animateGameSequence(0, false);
        } else {
            Toast.makeText(this,"You won the game!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Méthode appelé à chaque appuie sur un bouton du jeu, ajoute le bouton pressé à la série en cours.
     * @param view Bouton pressé
     */
    public void onGameButtonClick(View view) {

        // Récupère le bouton cliqué
        Button button = findViewById(view.getId());

        // Ajoute le bouton à la séquence du joueur
        playerSequenceButtons.add(button);

        if(playerSequenceButtons.size() == computerSequenceButtons.size()) {
            onSequenceEnd();
        }
    }
}