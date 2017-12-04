package edu.kvcc.cis298.cis298inclass4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.kvcc.cis298.cis298inclass4.CrimeDbSchema.CrimeTable;

/**
 * Created by cisco on 12/4/2017.
 */

public class CrimeBaseHelper extends SQLiteOpenHelper {

    //Version number and database name
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    //Constructor
    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + CrimeTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            CrimeTable.Cols.UUID + ", " +
            CrimeTable.Cols.TITLE + ", " +
            CrimeTable.Cols.DATE + ", " +
            CrimeTable.Cols.SOLVED +
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
