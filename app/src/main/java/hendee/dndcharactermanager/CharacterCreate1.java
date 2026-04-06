package hendee.dndcharactermanager;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * Created by Greg on 9/15/2017.
 */

public class CharacterCreate1 extends FragmentActivity {
    protected Spinner raceSpinner;
    protected Spinner subRaceSpinner;
    protected Spinner genderSpinner;
    protected EditText charName;
    protected Button nextButton;
    protected TextView features;
    protected List<String> charLanguages;
    protected List<String> charTraits;
    androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createcharacter1);

        raceSpinner = (Spinner) findViewById(R.id.races);
        subRaceSpinner = (Spinner) findViewById(R.id.subraces);
        genderSpinner = (Spinner) findViewById(R.id.gender);
        charName = (EditText) findViewById(R.id.name);
        nextButton = findViewById(R.id.createcharacter1complete);
        features = (TextView) findViewById(R.id.createcharacterracetraits);



        final Races r = new Races(getApplicationContext());
        raceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    ArrayList<String> subRacesList = r.getSubRaces(raceSpinner.getSelectedItem().toString());
                    ArrayAdapter<String> subRacesAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, subRacesList);
                    subRacesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subRaceSpinner.setAdapter(subRacesAdapter);
                    if (subRaceSpinner.getCount() == 1)
                        subRaceSpinner.setEnabled(false);
                    else
                        subRaceSpinner.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing
            }
        });


        subRaceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                loadFeatures();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing
            }

        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (charName.getText().toString().trim().equals(""))
                {
                    charName.setError("Name is Required");
                }
                else
                {
                    character newChar = new character();
                    initalizeCharacter(newChar);
                    newChar.toolProficiencies = new ArrayList<String>();
                    newChar.gender = genderSpinner.getSelectedItem().toString();
                    newChar.name = charName.getText().toString();
                    newChar.race = subRaceSpinner.getSelectedItem().toString();
                    newChar.languages = charLanguages;
                    newChar.traits = charTraits;
                    AddRacialBonuses(newChar);
                    Intent i = new Intent(getApplicationContext(), CharacterCreate2.class);
                    i.putExtra("character", newChar);
                    startActivity(i);
                }
            }
        });
    }

    public void loadFeatures()
    {
        String traits = loadTraits();
        String languages = loadLanguages();
        features.setText(traits + languages);
    }

    public String loadTraits()
    {
        final Races r = new Races(getApplicationContext());
        String featuresOutput = "";
        try {
            charTraits = r.getFeatures(subRaceSpinner.getSelectedItem().toString());
            for (int i = 0; i < charTraits.size(); i++) {
                featuresOutput += charTraits.get(i) + "\n\n";
            }
            //features.setText(featuresOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  featuresOutput;
    }

    public String loadLanguages()
    {
        final Races r = new Races(getApplicationContext());
        String featuresOutput = "";
        try {
            charLanguages = r.getLanguages(subRaceSpinner.getSelectedItem().toString());
            for (int i = 0; i < charLanguages.size(); i++) {
                featuresOutput += "Language: " + charLanguages.get(i) + "\n\n";
            }
            //features.setText(featuresOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return featuresOutput;
    }

    public void AddRacialBonuses(character currentChar)
    {
        //languages
        if (currentChar.race.toLowerCase().contains("human") || currentChar.race.toLowerCase().equals("half-elf")|| currentChar.race.toLowerCase().equals("elf (high)"))
        {
            addLanguageProficiency(currentChar, 1);
        }
        //dwarf
        if (currentChar.race.toLowerCase().contains("dwarf"))
        {
            //add stat bonusus
            currentChar.conAdditions = 2;
            //add tool proficiency
            ArrayList<String> choices = new ArrayList<String>();
            choices.add("Smith’s tools");
            choices.add("Brewer’s supplies");
            choices.add("Mason's tools");
            addToolProfiiency(currentChar, choices, "artisantools", 1);
            if (currentChar.race.toLowerCase().equals("dwarf (hill)"))
                currentChar.wisAdditions = 1;
            if (currentChar.race.toLowerCase().equals("dwarf (mountain)")) {
                currentChar.strAdditions = 2;
                currentChar.armorProficiencies.add("light");
                currentChar.armorProficiencies.add("medium");
            }
        }
        //elf
        if (currentChar.race.toLowerCase().startsWith("elf"))
        {
            currentChar.dexAdditions = 2;
            if (currentChar.race.toLowerCase().equals("elf (high)"))
            {
                currentChar.intAdditions = 1;
                currentChar.weaponProficiencies.add("longsword");
                currentChar.weaponProficiencies.add("shortsword");
                currentChar.weaponProficiencies.add("longbow");
                currentChar.weaponProficiencies.add("shortbow");
            }
            if (currentChar.race.toLowerCase().equals("elf (wood)"))
            {
                currentChar.wisAdditions = 1;
                currentChar.weaponProficiencies.add("longsword");
                currentChar.weaponProficiencies.add("shortsword");
                currentChar.weaponProficiencies.add("longbow");
                currentChar.weaponProficiencies.add("shortbow");
            }
            if (currentChar.race.toLowerCase().equals("elf (drow)"))
            {
                currentChar.wisAdditions = 1;
                currentChar.weaponProficiencies.add("rapier");
                currentChar.weaponProficiencies.add("shortsword");
                currentChar.weaponProficiencies.add("hand crossbow");
            }
        }
        //halfling
        if (currentChar.race.toLowerCase().startsWith("halfling"))
        {
            currentChar.dexAdditions = 2;
            if (currentChar.race.toLowerCase().equals("halfling (lightfoot)"))
            {
                currentChar.chaAdditions = 1;
            }
            if (currentChar.race.toLowerCase().equals("halfling (stout)"))
            {
                currentChar.conAdditions = 1;
            }
        }
        //human
        if (currentChar.race.toLowerCase().equals("human"))
        {
            currentChar.strAdditions = 1;
            currentChar.dexAdditions = 1;
            currentChar.conAdditions = 1;
            currentChar.intAdditions = 1;
            currentChar.wisAdditions = 1;
            currentChar.chaAdditions = 1;
        }
        if (currentChar.race.toLowerCase().equals("human (variant)"))
        {
            //add proficiency
            ArrayList<String> choices = new ArrayList<String>();
            addSkillProfiiency(currentChar, choices, 1);
            //add feat
            addFeat(currentChar, 1);
        }
    }

    public void addLanguageProficiency(character currentChar, int maxCount)
    {
        AddLanguages langDialogFragment = new AddLanguages();
        Bundle args = new Bundle();
        langDialogFragment.setIncomingChar(currentChar);
        args.putInt("maxCount", maxCount);
        langDialogFragment.setArguments(args);
        langDialogFragment.show(fm, "Add additional Language(s)");
    }

    public void addToolProfiiency(character currentChar, ArrayList<String> choices, String toolType, int maxCount)
    {
        AddTools toolDialogFragment =new AddTools();
        Bundle args = new Bundle();
        args.putInt("maxCount", maxCount);
        args.putString("toolSubSet",toolType);
        toolDialogFragment.setArguments(args);
        toolDialogFragment.setIncomingChar(currentChar);
        toolDialogFragment.setConcreteChoices(choices);
        toolDialogFragment.show(fm, "Add additional Tool(s)");
    }

    public void addSkillProfiiency(character currentChar, ArrayList<String> choices, int maxCount)
    {
        AddProficiencies proficienciesDialogFragment =new AddProficiencies();
        Bundle args = new Bundle();
        args.putInt("maxCount", maxCount);
        proficienciesDialogFragment.setArguments(args);
        proficienciesDialogFragment.setIncomingChar(currentChar);
        proficienciesDialogFragment.setConcreteChoices(choices);
        proficienciesDialogFragment.show(fm, "Add additional Skill(s)");
    }

    public void addFeat(character currentChar, int maxCount)
    {
        AddFeat featsDialogFragment =new AddFeat();
        Bundle args = new Bundle();
        args.putInt("maxCount", maxCount);
        featsDialogFragment.setArguments(args);
        featsDialogFragment.show(fm, "Add additional Feat");
    }

    public void initalizeCharacter(character currentChar)
    {
        currentChar.skillProficiencies = new ArrayList<>();
        currentChar.toolProficiencies = new ArrayList<String>();
        currentChar.armorProficiencies = new ArrayList<>();
        currentChar.weaponProficiencies = new ArrayList<>();
        //TODO initialize
    }
}