// MODIFY THE PACKAGE NAME BASED ON YOUR PROJECT
package itp341.caceres.nicholas.hazardRoomGame.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import itp341.caceres.nicholas.hazardRoomGame.Database.DbSchema.TABLE_POWERS;
import itp341.caceres.nicholas.hazardRoomGame.Database.DbSchema.TABLE_HEROES;


/**
 * Created by R on 11/2/2015.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "heroes.db";
    private static final int DATABASE_VERSION = 1;
    public final static String DATABASE_ROOT_PATH = "/data/data/";
    public final static String DATABASE_SUB_PATH = "/databases/";


    //SQL statement to create table

    private static final String CREATE_TABLE_POWERS =
            "CREATE TABLE " +
                    TABLE_POWERS.NAME + " (" +
                    TABLE_POWERS.KEY_ID + " integer primary key autoincrement, " +
                    TABLE_POWERS.KEY_OWN_POWER + " TEXT, " +
                    TABLE_POWERS.KEY_OPPOSING_POWER + " TEXT, " +
                    TABLE_POWERS.KEY_WINNING_POWER + " REAL " +
                    ");";

    private static final String CREATE_TABLE_HEROES =
            "CREATE TABLE " +
                    TABLE_HEROES.NAME + " (" +
                    TABLE_HEROES.KEY_ID + " integer primary key autoincrement, " +
                    TABLE_HEROES.KEY_NAME + " TEXT, " +
                    TABLE_HEROES.KEY_POWER1 + " TEXT, " +
                    TABLE_HEROES.KEY_POWER2 + " TEXT, " +
                    TABLE_HEROES.KEY_NUM_WINS + " REAL, " +
                    TABLE_HEROES.KEY_NUM_LOSSES + " REAL, " +
                    TABLE_HEROES.KEY_NUM_TIES + " REAL" +
                    ");";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "DbHelper constructor - copying database from assets");

        File databaseFile = new File(DATABASE_ROOT_PATH
                + context.getPackageName()
                + DATABASE_SUB_PATH
                + DATABASE_NAME);
        if (!databaseFile.exists()) {
            try {
                CopyDatabaseFromAsset(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //Called only first time database is created
    //Create the schema for the new table
    @Override
    public void onCreate(SQLiteDatabase db) {
        //These lines have been replaced by pre-loading the database in the assets folder
//        db.execSQL(CREATE_TABLE_POWERS);
//        db.execSQL(CREATE_TABLE_HEROES);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    /*
        The purpose of this method is to pre-load a database file from the
        assets folder.
        The file "heroes.db" MUST be copied into the /assets folder
        of your Android project

     */
    public void CopyDatabaseFromAsset(Context context) throws IOException {
        InputStream in = context.getAssets().open(DATABASE_NAME);
        Log.d(TAG, "Starting copying");
        String outputFileName = DATABASE_ROOT_PATH
                + context.getPackageName()
                + DATABASE_SUB_PATH
                + DATABASE_NAME;
        File databaseFolder = new File(DATABASE_ROOT_PATH
                + context.getPackageName()
                + DATABASE_SUB_PATH);
        // check if databases folder exists, if not create one and its subfolders
        if (!databaseFolder.exists()) {
            databaseFolder.mkdir();
        }

        OutputStream out = new FileOutputStream(outputFileName);

        byte[] buffer = new byte[1024];
        int length;


        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        out.flush();
        out.close();
        in.close();

    }


}