package hendee.dndcharactermanager;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvCharacters;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Create();
                }
            });

        lvCharacters = findViewById(R.id.lv_characters);
        tvEmpty = findViewById(R.id.tv_empty);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCharacters();
    }

    private void loadCharacters() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<character> characters = dbHelper.getAllCharacters();

        if (characters.isEmpty()) {
            lvCharacters.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            lvCharacters.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);

            List<String> displayList = new ArrayList<>();
            for (character c : characters) {
                displayList.add(c.name + " - " + c.race + " " + c.characterClass + " (Level 1)\n" +
                                "HP: " + c.maxHP + " | Skills: " + c.availableSkillPoints);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
            lvCharacters.setAdapter(adapter);
        }
    }

    public void Create()
    {
        Intent intent = new Intent(this, CharacterCreate1.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
