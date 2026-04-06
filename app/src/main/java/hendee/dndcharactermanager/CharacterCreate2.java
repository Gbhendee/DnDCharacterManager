package hendee.dndcharactermanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CharacterCreate2 extends AppCompatActivity {

    private character currentChar;
    private Button rollAllBtn;
    private Button nextBtn;
    private TextView rolledScoresTv;

    private Spinner spinStr, spinDex, spinCon, spinInt, spinWis, spinCha;
    private List<Integer> rolledScores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_create2);

        currentChar = (character) getIntent().getSerializableExtra("character");

        rollAllBtn = findViewById(R.id.btn_roll_all);
        nextBtn = findViewById(R.id.btn_next_step2);
        rolledScoresTv = findViewById(R.id.tv_rolled_scores);

        spinStr = findViewById(R.id.spin_str);
        spinDex = findViewById(R.id.spin_dex);
        spinCon = findViewById(R.id.spin_con);
        spinInt = findViewById(R.id.spin_int);
        spinWis = findViewById(R.id.spin_wis);
        spinCha = findViewById(R.id.spin_cha);

        rollAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollScores();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rolledScores.size() < 6) {
                    Toast.makeText(CharacterCreate2.this, "Please roll scores first.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get values
                String strStr = spinStr.getSelectedItem().toString();
                String dexStr = spinDex.getSelectedItem().toString();
                String conStr = spinCon.getSelectedItem().toString();
                String intStr = spinInt.getSelectedItem().toString();
                String wisStr = spinWis.getSelectedItem().toString();
                String chaStr = spinCha.getSelectedItem().toString();

                // Simple validation - ensure we selected a score
                if(strStr.isEmpty() || dexStr.isEmpty() || conStr.isEmpty() ||
                   intStr.isEmpty() || wisStr.isEmpty() || chaStr.isEmpty()) {
                    Toast.makeText(CharacterCreate2.this, "Please assign all scores.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // In a perfect world, we would validate that each rolled score is used exactly once.
                // For simplicity here, we trust the dropdowns but we could add validation.

                currentChar.str = Integer.parseInt(strStr) + currentChar.strAdditions;
                currentChar.dex = Integer.parseInt(dexStr) + currentChar.dexAdditions;
                currentChar.con = Integer.parseInt(conStr) + currentChar.conAdditions;
                currentChar.intel = Integer.parseInt(intStr) + currentChar.intAdditions;
                currentChar.wis = Integer.parseInt(wisStr) + currentChar.wisAdditions;
                currentChar.cha = Integer.parseInt(chaStr) + currentChar.chaAdditions;

                Intent intent = new Intent(CharacterCreate2.this, CharacterCreate3.class);
                intent.putExtra("character", currentChar);
                startActivity(intent);
            }
        });
    }

    private void rollScores() {
        rolledScores.clear();
        for (int i = 0; i < 6; i++) {
            rolledScores.add(roll4d6DropLowest());
        }

        // Sort descending
        Collections.sort(rolledScores, Collections.reverseOrder());

        StringBuilder sb = new StringBuilder("Rolled Scores: ");
        List<String> scoreStrings = new ArrayList<>();
        for (Integer score : rolledScores) {
            sb.append(score).append(" ");
            scoreStrings.add(String.valueOf(score));
        }
        rolledScoresTv.setText(sb.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, scoreStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinStr.setAdapter(adapter);
        spinDex.setAdapter(adapter);
        spinCon.setAdapter(adapter);
        spinInt.setAdapter(adapter);
        spinWis.setAdapter(adapter);
        spinCha.setAdapter(adapter);
    }

    private int roll4d6DropLowest() {
        Random rand = new Random();
        List<Integer> rolls = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            rolls.add(rand.nextInt(6) + 1); // 1 to 6
        }
        Collections.sort(rolls); // sorts ascending
        // drop lowest (index 0)
        return rolls.get(1) + rolls.get(2) + rolls.get(3);
    }
}
