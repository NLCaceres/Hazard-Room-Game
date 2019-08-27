package itp341.caceres.nicholas.hazardRoomGame.Model;

import java.util.Objects;

/**
 * Created by NLCaceres on 4/17/2016.
 */
public class Hero {

    public static long nextID = 0;

    private long _id;
    private String name;
    private String power1;
    private String power2;
    private int health;
    private int numWins;
    private int numLosses;
    private int numTies;

    public Hero() {
        _id = 0;
        name = "";
        power1 = "";
        power2 = "";
        health = 5;
        numWins = 0;
        numLosses = 0;
        numTies = 0;
    }

    public Hero(long id, String Name, String Power1, String Power2) {
        _id = id;
        name = Name;
        power1 = Power1;
        power2 = Power2;
        health = 5;
        numWins = 0;
        numLosses = 0;
        numTies = 0;
    }

    @Override
    public String toString() {
        String heroString = "";
        return heroString;
    }

    public long getHeroID() {
        return _id;
    }
    public void setHeroID(long id) {
        _id = id;
    }

    public String getHeroName() {
        return name;
    }
    public void setHeroName(String Name) {
        name = Name;
    }

    public String getPower1() {
        return power1;
    }
    public void setPower1(String Power1) {
        power1 = Power1;
    }

    public String getPower2() {
        return power2;
    }
    public void setPower2(String Power2) {
        power2 = Power2;
    }

    public String getCurrentPower(int powerNum) {
        if (powerNum == 1) {
            return power1;
        } else { return power2; }
    }

    public int getHealth() {
        return health;
    }
    public void setHealth(int HP) {
        health = HP;
    }

    public int getWins() {
        return numWins;
    }
    public void setNumWins(int Wins) {
        numWins = Wins;
    }
    public void justWon(){
        numWins++;
    }

    public int getLosses() {
        return numLosses;
    }
    public void setNumLosses(int Losses) {
        numLosses = Losses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hero hero = (Hero) o;
        return _id == hero._id &&
                name.equals(hero.name) &&
                power1.equals(hero.power1) &&
                power2.equals(hero.power2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, name, power1, power2);
    }

    public void justLost() {
        numLosses++;
    }

    public int getTies() {
        return numTies;
    }
    public void setNumTies(int Ties) {
        numTies = Ties;
    }
    public void justTied() {
        numTies++;
    }

    public boolean isAlive() {
        if (health ==  0) {
            return false;
        }
        else {
            return true;
        }
    }
}
