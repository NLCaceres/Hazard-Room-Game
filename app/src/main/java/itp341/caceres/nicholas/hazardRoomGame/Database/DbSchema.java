// MODIFY THE PACKAGE NAME BASED ON YOUR PROJECT
package itp341.caceres.nicholas.hazardRoomGame.Database;

/**
 * Created by R on 11/2/2015.
 */
public class DbSchema {

    public static final class TABLE_POWERS {
        public static final String NAME = "powers";

        // Column Names
        public static final String KEY_ID = "_id"; // follow this convention
        public static final String KEY_OWN_POWER = "own_power";
        public static final String KEY_OPPOSING_POWER = "opposing_power";
        public static final String KEY_WINNING_POWER = "winning_power";

        // Column indexes (good enumeration style)
        public static final int COLUMN_ID = 0;
        public static final int COLUMN_OWN_POWER = 1;
        public static final int COLUMN_OPPOSING_POWER = 2;
        public static final int COLUMN_WINNING_POWER = 3;
    }
    public static final class TABLE_HEROES {
        public static final String NAME = "heroes";

        // Column Names
        public static final String KEY_ID = "_id"; // follow this convention
        public static final String KEY_NAME = "name";
        public static final String KEY_POWER1 = "power1";
        public static final String KEY_POWER2= "power2";
        public static final String KEY_NUM_WINS = "num_wins";
        public static final String KEY_NUM_LOSSES = "num_losses";
        public static final String KEY_NUM_TIES = "num_ties";

        // Column indexes (good enumeration style)
        public static final int COLUMN_ID = 0;
        public static final int COLUMN_NAME = 1;
        public static final int COLUMN_POWER1 = 2;
        public static final int COLUMN_POWER2 = 3;
        public static final int COLUMN_NUM_WINS = 4;
        public static final int COLUMN_NUM_LOSSES = 5;
        public static final int COLUMN_NUMS_TIES = 6;
    }
}
