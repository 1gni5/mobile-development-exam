package org.dut.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class LevelSelection extends AppCompatActivity {

    Intent intent;
    Bundle bundle;
    private final static byte NUMBER_OF_GAMEMODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

        intent = new Intent(LevelSelection.this, Game.class);
        bundle = new Bundle();


        // Mode facile
        findViewById(R.id.easyGameModeButton).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadGame(2, 1.0, 1, 10, false);
                }
            }
        );

        // Mode difficile
        findViewById(R.id.hardGameModeButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadGame(2, 1.5, 3, 15, false);
                    }
                }
        );

        // Mode expert
        findViewById(R.id.expertGameModeButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadGame(3, 3, 5, 20, false);
                    }
                }
        );

        // Mode chrono
        findViewById(R.id.timedGameModeButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadGame(3, 2, 1, 99, true);
                    }
                }
        );

    }

    private void loadGame(
            int maxHealth, double scoreWeigth, int minSequence, int maxSequence, boolean timed) {

        // Remplit le bundle
        bundle.putInt("maxHealth", maxHealth);
        bundle.putDouble("scoreWeigth", scoreWeigth);
        bundle.putInt("minSequence", minSequence);
        bundle.putInt("maxSequence", maxSequence);
        bundle.putBoolean("isTimed", timed);

        // Lance le jeu
        intent.putExtras(bundle);
        startActivity(intent);
    }
}