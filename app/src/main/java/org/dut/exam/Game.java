package org.dut.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class Game extends AppCompatActivity {

    static final byte NUMBER_OF_BUTTONS = 10;

    GameMode gameMode;
    ArrayList<Button> allButtons = new ArrayList<Button>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Récupère tout les boutons du layout
        for(int i = 1; i <= NUMBER_OF_BUTTONS; i++) {

            // Construit l'identifiant et récupère l'id
            String identifier = "gameButton" + i;
            int buttonId = getResources().getIdentifier(identifier, "id", getPackageName());

            allButtons.add((Button)findViewById(buttonId));
        }
    }
}