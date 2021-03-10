package org.dut.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends AppCompatActivity {

    static final byte NUMBER_OF_BUTTONS = 10;
    static final byte MAXIMUM_LEVEL = 7;
    static final byte DEFAULT_BUTTONS_NUMBER = 3;

    GameMode gameMode;
    byte level, lives;
    private ArrayList<Button> allButtons = new ArrayList<Button>();

    /**
     * Créer une sous-liste en tirant aléatoirement dans une liste d'origine.
     * @param list La liste d'origine
     * @param numberOfElements Le nombre d'éléments de la sous-liste
     * @return Sous-liste d'éléments
     */
    protected ArrayList<Button> getRandomSubList(ArrayList<Button> list, int numberOfElements) {

        // Prépare la liste de résultat
        ArrayList<Button> results = new ArrayList<Button>();

        // Tire aléatoirement N boutons
        Random random = new Random();
        for(int i = 0; i < numberOfElements; i++) {
            int index = random.nextInt(list.size());
            results.add(list.remove(index));
        }

        return results;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Simule le passage d'argument par une activité précèdente
        level = 7;

        // Récupère tout les boutons du layout
        for(int i = 1; i <= NUMBER_OF_BUTTONS; i++) {

            // Construit l'identifiant et récupère l'id
            String identifier = "gameButton" + i;
            int buttonId = getResources().getIdentifier(identifier, "id", getPackageName());

            allButtons.add((Button)findViewById(buttonId));
        }

        // Création d'une sous-liste avec les blocs à allumer
        ArrayList<Button> activeButtons = getRandomSubList(
                new ArrayList<Button>(allButtons),
                level+DEFAULT_BUTTONS_NUMBER
        );

        for(Button button : activeButtons) {
            button.setVisibility(View.VISIBLE);
        }
    }
}