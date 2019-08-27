package itp341.caceres.nicholas.hazardRoomGame.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import itp341.caceres.nicholas.hazardRoomGame.Database.DbHelper;
import itp341.caceres.nicholas.hazardRoomGame.Database.DbSchema;

/**
 * Created by NLCaceres on 4/17/2016.
 */
public class BattleSingleton {
    private static BattleSingleton ourInstance;

    private SQLiteDatabase mDatabase;
    private Context mContext;

    public static BattleSingleton getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new BattleSingleton(context.getApplicationContext());
        }
        return ourInstance;
    }

    private BattleSingleton(Context context) {
        mContext = context;
        mDatabase = new DbHelper(context).getWritableDatabase();
    }

    public void closeDatabase() {
        this.mDatabase.close();
    }

    public ArrayList<String> getUniquePowers() {
        Cursor c = mDatabase.query("powers", new String[] {"opposing_power"}, null, null, "opposing_power", null, null);
        ArrayList<String> powersList = new ArrayList<String>();
        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                powersList.add(c.getString(c.getColumnIndex("opposing_power")));
                c.moveToNext();
            }
        } finally {
            c.close();
            return powersList;
        }
    }

    public void addHero(Hero hero) {
        ContentValues cv = new ContentValues();
        cv.put(DbSchema.TABLE_HEROES.KEY_NAME, hero.getHeroName());
        cv.put(DbSchema.TABLE_HEROES.KEY_POWER1, hero.getPower1());
        cv.put(DbSchema.TABLE_HEROES.KEY_POWER2, hero.getPower2());
        cv.put(DbSchema.TABLE_HEROES.KEY_NUM_WINS, 0);
        cv.put(DbSchema.TABLE_HEROES.KEY_NUM_LOSSES, 0);
        cv.put(DbSchema.TABLE_HEROES.KEY_NUM_TIES, 0);
        mDatabase.insert(DbSchema.TABLE_HEROES.NAME, null, cv);
    }

    public void removeHero(long heroID) {
        int oneRowDeleted = mDatabase.delete(DbSchema.TABLE_HEROES.NAME, DbSchema.TABLE_HEROES.KEY_ID + " = ?", new String[] {Long.toString(heroID)});
        if (oneRowDeleted == 1) {
            Log.d("BattleModel RemoveHero", "One Row was deleted from the SQLite DB");
        }
    }

    public ArrayList<Hero> getRankings() {
        Cursor c = mDatabase.query("heroes", null, null, null, null, null, "num_wins desc");
        ArrayList<Hero> heroesList = new ArrayList<Hero>();
        try {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                heroesList.add(new Hero());
                heroesList.get(i).setHeroID(c.getLong(c.getColumnIndex("_id")));
                heroesList.get(i).setHeroName(c.getString(c.getColumnIndex("name")));
                heroesList.get(i).setPower1(c.getString(c.getColumnIndex("power1")));
                heroesList.get(i).setPower2(c.getString(c.getColumnIndex("power2")));
                heroesList.get(i).setNumWins(c.getInt(c.getColumnIndex("num_wins")));
                heroesList.get(i).setNumLosses(c.getInt(c.getColumnIndex("num_losses")));
                heroesList.get(i).setNumTies(c.getInt(c.getColumnIndex("num_ties")));
                c.moveToNext();
            }
        } finally {
            c.close();
            return heroesList;
        }

    }

    public ArrayList<Hero> getHeroes() {
        ArrayList<Hero> heroesList = new ArrayList<>();
        try (Cursor c = mDatabase.query("heroes", null, null, null, null, null, null)) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                heroesList.add(new Hero());
                heroesList.get(i).setHeroID(c.getLong(c.getColumnIndex("_id")));
                heroesList.get(i).setHeroName(c.getString(c.getColumnIndex("name")));
                heroesList.get(i).setPower1(c.getString(c.getColumnIndex("power1")));
                heroesList.get(i).setPower2(c.getString(c.getColumnIndex("power2")));
                heroesList.get(i).setNumWins(c.getInt(c.getColumnIndex("num_wins")));
                heroesList.get(i).setNumLosses(c.getInt(c.getColumnIndex("num_losses")));
                heroesList.get(i).setNumTies(c.getInt(c.getColumnIndex("num_ties")));
                c.moveToNext();
            }
        }
        return heroesList;
    }

    public ArrayList<Hero> getAlphHeroes() {
        ArrayList<Hero> heroesList = new ArrayList<>();
        try (Cursor c = mDatabase.query("heroes", null, null, null, null, null, "name asc")) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                heroesList.add(new Hero());
                heroesList.get(i).setHeroID(c.getLong(c.getColumnIndex("_id")));
                heroesList.get(i).setHeroName(c.getString(c.getColumnIndex("name")));
                heroesList.get(i).setPower1(c.getString(c.getColumnIndex("power1")));
                heroesList.get(i).setPower2(c.getString(c.getColumnIndex("power2")));
                heroesList.get(i).setNumWins(c.getInt(c.getColumnIndex("num_wins")));
                heroesList.get(i).setNumLosses(c.getInt(c.getColumnIndex("num_losses")));
                heroesList.get(i).setNumTies(c.getInt(c.getColumnIndex("num_ties")));
                c.moveToNext();
            }
        }
        return heroesList;
    }

    public int getPowerResult(String Power1, String Power2) {
        try (Cursor c = mDatabase.query("powers", new String[] {"own_power", "opposing_power", "winning_power"}, "own_power = ? AND opposing_power = ?", new String[] {Power1, Power2}, null, null, null))
        {
            c.moveToFirst();
            return c.getInt(c.getColumnIndex("winning_power"));
        }
    }

    public void addBattleResult(Hero heroName, int result) {
        ContentValues cv = new ContentValues();
        String _ID = String.valueOf(heroName.getHeroID());
        Log.d("TAG", _ID);
        int newRecord;
        if (result == 1) {
            newRecord = heroName.getWins() + 1;
            cv.put("num_wins", String.valueOf(newRecord));
        }
        else if (result == -1) {
            newRecord = heroName.getLosses() + 1;
            cv.put("num_losses", String.valueOf(newRecord));
        }
        else {
            newRecord = heroName.getTies() + 1;
            cv.put("num_ties", String.valueOf(newRecord));
        }
        mDatabase.update(DbSchema.TABLE_HEROES.NAME, cv, "_id = ?", new String[] {_ID});
    }

    public void deleteEnd(String index) {
        mDatabase.delete(DbSchema.TABLE_HEROES.NAME, "_id = ?", new String[] {index});
    }

    public void resetBattleResults() {
        ContentValues cv = new ContentValues();
        cv.put(DbSchema.TABLE_HEROES.KEY_NUM_WINS, 0);
        cv.put(DbSchema.TABLE_HEROES.KEY_NUM_LOSSES, 0);
        cv.put(DbSchema.TABLE_HEROES.KEY_NUM_TIES, 0);
        mDatabase.update(DbSchema.TABLE_HEROES.NAME, cv, null, null);
    }
    public int getMaxID() {
        try (Cursor c = mDatabase.query(DbSchema.TABLE_HEROES.NAME, new String[] {"MAX(_id)"}, null, null, null, null, null)) {
            c.moveToFirst();
            return c.getInt(0);
        }
    }
}
