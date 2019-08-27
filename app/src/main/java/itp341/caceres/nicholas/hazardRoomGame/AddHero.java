package itp341.caceres.nicholas.hazardRoomGame;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import itp341.caceres.nicholas.hazardRoomGame.Model.BattleSingleton;
import itp341.caceres.nicholas.hazardRoomGame.Model.Hero;

public class AddHero extends AppCompatActivity {

    private EditText heroNameEditText;
    private String heroName;

    private Spinner power1Spinner;
    private Spinner power2Spinner;
    private ArrayAdapter<String> powersAdapter;
    private String power1;
    private String power2;

    private Button saveButton;

    private BattleSingleton battleModels;

    public static final String EXTRA_HERO_ID = "itp341.caceres.nicholas.hazardRoomGame.extra_hero_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hero);

        battleModels = BattleSingleton.getInstance(getApplicationContext());

        heroNameEditText = (EditText) findViewById(R.id.heroNameEditText);
        /* heroNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                heroName = heroNameEditText.getText().toString();
                return true;
            }
        }); */
        heroNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                heroName = heroNameEditText.getText().toString();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        power1Spinner = (Spinner) findViewById(R.id.powerOneSpinner);
        ArrayList<String> heroPowers = battleModels.getUniquePowers();
        powersAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, heroPowers);
        powersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        power1Spinner.setAdapter(powersAdapter);
        power1 = power1Spinner.getSelectedItem().toString();
        power1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                power1 = power1Spinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        power2Spinner = (Spinner) findViewById(R.id.powerTwoSpinner);
        power2Spinner.setAdapter(powersAdapter);
        power2 = power2Spinner.getSelectedItem().toString();
        power2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                power2 = power2Spinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        saveButton = (Button) findViewById(R.id.saveHeroButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                i.putExtra(MainBattle.EXTRA_NEW_HERO_NAME, heroName);
                int newHeroID = i.getIntExtra(EXTRA_HERO_ID, battleModels.getAlphHeroes().size() + 1); // ID = index of hero
                Hero newHero = new Hero(newHeroID, heroName, power1, power2);
                battleModels.addHero(newHero);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });
    }

    //@Override
    //protected void onDestroy() { }
}
