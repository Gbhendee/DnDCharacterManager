package hendee.dndcharactermanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CharacterCreate3 extends AppCompatActivity {

    private character currentChar;
    private Spinner classSpinner;
    private Button completeBtn;

    // D&D 3.5 Core Classes
    private String[] classes = {
        "Barbarian", "Bard", "Cleric", "Druid", "Fighter",
        "Monk", "Paladin", "Ranger", "Rogue", "Sorcerer", "Wizard"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_create3);

        currentChar = (character) getIntent().getSerializableExtra("character");

        classSpinner = findViewById(R.id.spin_class);
        completeBtn = findViewById(R.id.btn_complete);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedClass = classSpinner.getSelectedItem().toString();
                currentChar.characterClass = selectedClass;

                // Calculate modifier: Mod = (Score - 10) / 2
                int conMod = Math.floorDiv(currentChar.con - 10, 2);
                int intMod = Math.floorDiv(currentChar.intel - 10, 2);

                int hitDie = 8;
                int baseSkillPoints = 2;

                switch (selectedClass) {
                    case "Barbarian":
                        hitDie = 12;
                        baseSkillPoints = 4;
                        break;
                    case "Fighter":
                    case "Paladin":
                        hitDie = 10;
                        baseSkillPoints = 2;
                        break;
                    case "Ranger":
                        hitDie = 8;
                        baseSkillPoints = 6;
                        break;
                    case "Bard":
                    case "Rogue":
                        hitDie = 6;
                        baseSkillPoints = 8;
                        if(selectedClass.equals("Bard")) baseSkillPoints = 6;
                        break;
                    case "Cleric":
                    case "Druid":
                    case "Monk":
                        hitDie = 8;
                        baseSkillPoints = 2;
                        if (selectedClass.equals("Druid") || selectedClass.equals("Monk")) baseSkillPoints = 4;
                        break;
                    case "Sorcerer":
                    case "Wizard":
                        hitDie = 4;
                        baseSkillPoints = 2;
                        break;
                }

                // 3.5 Max HP at level 1: Max of Hit Die + CON mod
                currentChar.maxHP = hitDie + conMod;
                if(currentChar.maxHP < 1) currentChar.maxHP = 1; // Minimum 1 HP

                // Starting skill points: (Class Base + INT mod) * 4
                // Minimum 1 skill point per level (so min 4 at start)
                int pointsPerLevel = baseSkillPoints + intMod;
                if (pointsPerLevel < 1) pointsPerLevel = 1;

                // Human gets +1 skill point per level (+4 at 1st level)
                if(currentChar.race != null && currentChar.race.toLowerCase().startsWith("human")) {
                    pointsPerLevel += 1;
                }

                currentChar.availableSkillPoints = pointsPerLevel * 4;

                // Save character logic here
                DatabaseHelper dbHelper = new DatabaseHelper(CharacterCreate3.this);
                dbHelper.addCharacter(currentChar);

                Toast.makeText(CharacterCreate3.this, "Character Saved!", Toast.LENGTH_SHORT).show();

                // Go back to main activity
                Intent intent = new Intent(CharacterCreate3.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
