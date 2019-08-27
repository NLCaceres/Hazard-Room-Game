package itp341.caceres.nicholas.hazardRoomGame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import itp341.caceres.nicholas.hazardRoomGame.Model.BattleSingleton;
import itp341.caceres.nicholas.hazardRoomGame.Model.Hero;

public class MainBattle extends AppCompatActivity {

    private Button addHero;
    private Button fightButton;
    private ButtonListener battleListener;

    private ListView rankingsLV;
    private HeroRankingsAdapter heroRankingAdapter;

    private Spinner heroOneSpinner;
    private Spinner heroTwoSpinner;
    private ArrayAdapter<String> heroSpinnerAdapter;
    private String heroOneName;
    private String heroTwoName;

    private TextView battleTV;

    private BattleSingleton battleModels;

    public static final String EXTRA_NEW_HERO_NAME = "itp341.caceres.nicholas.hazardRoomGame.extra_new_hero_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_battle);

        battleModels = BattleSingleton.getInstance(getApplicationContext());
        //battleModels.resetBattleResults();

        battleListener = new ButtonListener();
        addHero = (Button) findViewById(R.id.addButton);
        addHero.setOnClickListener(battleListener);
        fightButton = (Button) findViewById(R.id.fightButton);
        fightButton.setOnClickListener(battleListener);

        rankingsLV = (ListView) findViewById(R.id.rankingsListView);
        rankingsLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        rankingsLV.setMultiChoiceModeListener(new HeroRankingsMultiChoiceListener());
        new HeroRankingsTask().execute();

        heroOneSpinner = (Spinner) findViewById(R.id.heroOneSpinner);
        heroOneName = heroOneSpinner.getSelectedItem().toString();
        heroTwoSpinner = (Spinner) findViewById(R.id.heroTwoSpinner);
        heroTwoName = heroTwoSpinner.getSelectedItem().toString();
        new HeroSpinnerTask().execute();
        heroOneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                heroOneName = heroOneSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        heroTwoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                heroTwoName = heroTwoSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        battleTV = (TextView) findViewById(R.id.battleLogTextView);
    }

    public class HeroRankingsMultiChoiceListener implements ListView.MultiChoiceModeListener {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            final int selectedCount = rankingsLV.getCheckedItemCount();
            mode.setTitle(selectedCount + " Selected");
            heroRankingAdapter.toggleSelection(position); // Custom Adapter Helper Func
        }
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.contextual_menu, menu);
            return true;
        }
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete_icon:
                    SparseBooleanArray selected = heroRankingAdapter.getSelectedHeroes();
                    for (int i = (selected.size() - 1); i >= 0; i--) {
                        if (selected.valueAt(i)) {
                            Hero selectedHero = heroRankingAdapter.getItem(selected.keyAt(i));
                            heroRankingAdapter.remove(selectedHero);
                            battleModels.removeHero(selectedHero.getHeroID());
                            heroRankingAdapter.notifyDataSetChanged();
                            heroSpinnerAdapter.remove(selectedHero.getHeroName());
                            heroSpinnerAdapter.notifyDataSetChanged();
                        }
                    }
                    mode.finish(); // Action Bar Destroy
                    return true;
                default:
                    return false;
            }
        }
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }
        @Override
        public void onDestroyActionMode(ActionMode mode) { heroRankingAdapter.removeSelection(); }
    }

    public class HeroRankingsAdapter extends ArrayAdapter<Hero> {
        private SparseBooleanArray mSelectedItemsIds;

        public HeroRankingsAdapter(Context context, ArrayList<Hero> heroes) {
            super(context, 0, heroes);
            mSelectedItemsIds = new SparseBooleanArray();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Hero hero = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_rankings, parent, false);
            }
            TextView heroName = (TextView) convertView.findViewById(R.id.heroNameTV);
            heroName.setText(hero.getHeroName());
            TextView heroRanking = (TextView) convertView.findViewById(R.id.heroWLTTV);
            heroRanking.setText(getResources().getString(R.string.win_rankings) + " " + hero.getWins() + " " + getResources().getString(R.string.lose_rankings) + " " + hero.getLosses() + " " + getResources().getString(R.string.tie_rankings) + " " + hero.getTies());
            TextView heroPowerOne = (TextView) convertView.findViewById(R.id.powerOneTV);
            heroPowerOne.setText(hero.getPower1());
            TextView heroPowerTwo = (TextView) convertView.findViewById(R.id.powerTwoTV);
            heroPowerTwo.setText(hero.getPower2());
            return convertView;
        }

        public void toggleSelection(int position) {
            selectView(position, !mSelectedItemsIds.get(position));
        }
        public void removeSelection() {
            mSelectedItemsIds = new SparseBooleanArray();
            notifyDataSetChanged();
        }
        public void selectView(int position, boolean value) {
            if (value) { mSelectedItemsIds.put(position, value); }
            else { mSelectedItemsIds.delete(position); }
            notifyDataSetChanged();
        }
        public SparseBooleanArray getSelectedHeroes() { return mSelectedItemsIds; }
    }

    public class HeroSpinnerTask extends AsyncTask<Void, Void, ArrayList<String>> {
        protected void onPreExecute() {
            // If wanted a progress bar
        }
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> heroNames = new ArrayList<String>();
            for (int i = 0; i < battleModels.getAlphHeroes().size(); i++) {
                heroNames.add(battleModels.getAlphHeroes().get(i).getHeroName());
            }
            return heroNames;
        }
        protected void onProgressUpdate(Void... values) {
            // If I wanted to continously update the progress bar
        }
        protected void onPostExecute(ArrayList<String> heroNames) {
            heroSpinnerAdapter = new ArrayAdapter<String>(MainBattle.this, android.R.layout.simple_spinner_item, heroNames);
            heroSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            heroOneSpinner.setAdapter(heroSpinnerAdapter);
            heroTwoSpinner.setAdapter(heroSpinnerAdapter);
        }
    }

    public class HeroRankingsTask extends AsyncTask<Void, Void, ArrayList<Hero>> {
        protected void onPreExecute() {
            // If wanted a progress bar
        }
        protected ArrayList<Hero> doInBackground(Void... params) {
            return battleModels.getRankings();
        }
        protected void onProgressUpdate(Void... values) {
            // If I wanted to continously update the progress bar
        }
        protected void onPostExecute(ArrayList<Hero> rankingList) {
            heroRankingAdapter = new HeroRankingsAdapter(getApplicationContext(), rankingList);
            rankingsLV.setAdapter(heroRankingAdapter);
        }
    }

    public class UpdateRankingsTask extends AsyncTask<Void, Void, ArrayList<Hero>> {
        protected void onPreExecute() {
            // If wanted a progress bar
        }
        protected ArrayList<Hero> doInBackground(Void... params) {
            return battleModels.getRankings();
        }
        protected void onProgressUpdate(Void... values) {
            // If I wanted to continously update the progress bar
        }
        protected void onPostExecute(ArrayList<Hero> rankingList) {
            heroRankingAdapter.add(rankingList.get(rankingList.size() - 1));
            heroRankingAdapter.notifyDataSetChanged();
        }
    }

    public class AddNewHeroTask extends AsyncTask<Void, Void, Intent> {
        protected void onPreExecute() {
            // If wanted a progress bar
        }
        protected Intent doInBackground(Void... params) {
            Intent i = new Intent(getApplicationContext(), AddHero.class);
            int maxID = battleModels.getMaxID();
            i.putExtra(AddHero.EXTRA_HERO_ID, maxID+1);
            return i;
        }
        protected void onProgressUpdate(Void... values) {
            // If I wanted to continously update the progress bar
        }
        protected void onPostExecute(Intent i) {
            startActivityForResult(i, 0);
        }
    }

    public class BeginBattleTask extends AsyncTask<Void, Void, ArrayList<String>> {
        protected void onPreExecute() {
            // If wanted a progress bar
        }
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<Hero> heroesList = battleModels.getAlphHeroes();
            int heroOneIndex = 0;
            int heroTwoIndex = 0;
            for (int i = 0; i < heroesList.size(); i++) {
                String name = heroesList.get(i).getHeroName();
                if (Objects.equals(name, heroOneName)) {
                    heroOneIndex = i;
                }
                else if (Objects.equals(name, heroTwoName)) {
                    heroTwoIndex = i;
                }
            }
            Hero heroOne = heroesList.get(heroOneIndex);
            Hero heroTwo = heroesList.get(heroTwoIndex);

            ArrayList<String> battleLogText = new ArrayList<>();
            battleLogText.add(heroOneName + " " + getResources().getString(R.string.vs_battle_log) + " " + heroTwoName + "\n");
            battleLogText.add("\n");
            String roundBattleLog = getResources().getString(R.string.round_battle_log);
            String endRoundBattleLog = getResources().getString(R.string.end_round_battle_log);
            String usesBattleLog = getResources().getString(R.string.uses_battle_log);
            String winsBattleLog = getResources().getString(R.string.wins_battle_log);
            String tiesBattleLog = getResources().getString(R.string.ties_battle_log);

            Random rand = new Random();
            int roundNum = 1;
            while (heroOne.isAlive() && heroTwo.isAlive()) {
                battleLogText.add(roundBattleLog + " " + roundNum + " " + endRoundBattleLog + "\n");
                int heroOnePowerNum = rand.nextInt(2) + 1;
                String heroOnePowerString = heroOne.getCurrentPower(heroOnePowerNum);
                int heroTwoPowerNum = rand.nextInt(2) + 1;
                String heroTwoPowerString = heroTwo.getCurrentPower(heroTwoPowerNum);

                battleLogText.add(heroOneName + " " + usesBattleLog + " " + heroOnePowerString + "\n");
                battleLogText.add(heroTwoName + " " + usesBattleLog + " " + heroTwoPowerString + "\n");

                int roundResult = battleModels.getPowerResult(heroOnePowerString, heroTwoPowerString);
                if (roundResult == 1) {
                    battleLogText.add(heroOneName + " " + winsBattleLog + " " + heroOnePowerString + "\n");
                    heroTwo.setHealth(heroTwo.getHealth() - 1);
                }
                else if (roundResult == -1) {
                    battleLogText.add(heroTwoName + " " + winsBattleLog + " " + heroTwoPowerString + "\n");
                    heroOne.setHealth(heroOne.getHealth() - 1);
                }
                else {
                    battleLogText.add(heroOneName + " and " + heroTwoName + " " + tiesBattleLog + "\n");
                    heroOne.setHealth(heroOne.getHealth() - 1);
                    heroTwo.setHealth(heroTwo.getHealth() - 1);
                }
                roundNum++;
                battleLogText.add("\n");
            }

            battleLogText.add("\n");

            if (!heroOne.isAlive() && !heroTwo.isAlive()) {
                battleLogText.add(heroOneName + " and " + heroTwoName + " " + getResources().getString(R.string.tied_battle_log) + "\n");
                battleModels.addBattleResult(heroOne, 0);
                battleModels.addBattleResult(heroTwo, 0);
                //battleLogText.add(Integer.toString(0));

            } else if (!heroOne.isAlive()) {
                battleLogText.add(heroTwoName + " " + getResources().getString(R.string.winner_battle_log) + "\n");
                battleModels.addBattleResult(heroOne, -1);
                battleModels.addBattleResult(heroTwo, 1);
                //battleLogText.add(Integer.toString(-1));

            } else if (!heroTwo.isAlive()) {
                battleLogText.add(heroOneName + " " + getResources().getString(R.string.winner_battle_log) + "\n");
                battleModels.addBattleResult(heroOne, 1);
                battleModels.addBattleResult(heroTwo, -1);
                //battleLogText.add(Integer.toString(1));
            }

            return battleLogText;
        }
        protected void onProgressUpdate(Void... values) {
            // If I wanted to continously update the progress bar
        }
        protected void onPostExecute(ArrayList<String> battleLogText) {
            battleTV.setText(battleLogText.get(0));
            for (int i = 1; i < battleLogText.size(); i++) {
                /*if (i == battleLogText.size() - 1) {
                    int battleResultIndicator = Integer.parseInt(battleLogText.get(i));
                    ArrayList<Hero> heroesList = battleModels.getRankings();
                    int heroOneIndex = 0;
                    int heroTwoIndex = 0;
                    for (int j = 0; j < heroesList.size(); j++) {
                        String name = heroesList.get(j).getHeroName();
                        if (Objects.equals(name, heroOneName)) {
                            heroOneIndex = j;
                        }
                        else if (Objects.equals(name, heroTwoName)) {
                            heroTwoIndex = j;
                        }
                    }
                    Hero heroOne = heroRankingAdapter.getItem(heroOneIndex);
                    Hero heroTwo = heroRankingAdapter.getItem(heroTwoIndex);

                    heroRankingAdapter.remove(heroOne);
                    heroRankingAdapter.remove(heroTwo);
                    if (battleResultIndicator == 1) { // Hero 1 won
                        battleModels.addBattleResult(heroOne, 1);
                        battleModels.addBattleResult(heroTwo, -1);
                        heroOne.justWon();
                        heroTwo.justLost();
                    } else if (battleResultIndicator == 0) { // Heroes tied
                        battleModels.addBattleResult(heroOne, 0);
                        battleModels.addBattleResult(heroTwo, 0);
                        heroOne.justTied();
                        heroTwo.justTied();
                    } else { // Hero 2 won
                        battleModels.addBattleResult(heroOne, -1);
                        battleModels.addBattleResult(heroTwo, 1);
                        heroOne.justLost();
                        heroTwo.justWon();
                    }

                    // CHECK FOR WHICH INDEX IS LOWER -- LOWER INDEX MUST BE INSERTED BACK IN FIRST!
                    if (heroOneIndex < heroTwoIndex) {
                        if (heroTwo.getWins() > heroOne.getWins()) {
                            heroRankingAdapter.insert(heroTwo, heroOneIndex);
                            heroRankingAdapter.insert(heroOne, heroTwoIndex);
                        } else {
                            heroRankingAdapter.insert(heroOne, heroOneIndex);
                            heroRankingAdapter.insert(heroTwo, heroTwoIndex);
                        }
                    } else { // HeroTwoIndex is smaller
                        if (heroOne.getWins() > heroTwo.getWins()) {
                            heroRankingAdapter.insert(heroOne, heroTwoIndex);
                            heroRankingAdapter.insert(heroTwo, heroOneIndex);
                        } else {
                            heroRankingAdapter.insert(heroTwo, heroTwoIndex);
                            heroRankingAdapter.insert(heroOne, heroOneIndex);
                        }

                    }
                    heroRankingAdapter.notifyDataSetChanged();
                    continue;
                } */
                battleTV.append(battleLogText.get(i));
            }
            ArrayList<Hero> newRankings = battleModels.getRankings();
            heroRankingAdapter.clear();
            heroRankingAdapter.addAll(newRankings);
            heroRankingAdapter.notifyDataSetChanged();
        }
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.addButton: {
                    new AddNewHeroTask().execute();
                    break;
                }
                case R.id.fightButton: {
                    if (heroOneName == heroTwoName) {
                        break;
                    }
                    new BeginBattleTask().execute();
                    break;
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) { // Code for the add new hero activity
            new UpdateRankingsTask().execute();
            String newHeroName = data.getStringExtra(EXTRA_NEW_HERO_NAME);
            heroSpinnerAdapter.add(newHeroName);
            heroSpinnerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        battleModels.closeDatabase();
        super.onDestroy();
    }
}
