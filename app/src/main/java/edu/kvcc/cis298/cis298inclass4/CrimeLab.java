package edu.kvcc.cis298.cis298inclass4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.kvcc.cis298.cis298inclass4.CrimeDbSchema.CrimeTable;

/**
 * Created by cisco on 10/16/2017.
 */

public class CrimeLab {
    //This static variable will hold our single instance of
    //crime lab.
    //It might seem weird that the class is holding a reference
    //to itself, but it that's just how it's done.
    //Kind of like a Linked List.
    private static CrimeLab sCrimeLab;

    //Context for the database
    private Context mContext;
    //Database
    private SQLiteDatabase mDatabase;

    //This will be the public static method that is used to always
    //get the same instance that is stored in the static sCrimeLab
    //field.
    //Currently it takes in a Context that we will not use until
    //we get to the sqlite part.
    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    //This is the constructor. It is private because it is part
    //of a singleton. This ensures that the only code that can
    //make an instance of this class is this class.
    //The instance is made above in the get method.
    private CrimeLab(Context context) {

        //Why are we asking for the context from the activity
        //when we also have one sent into the contructor?????
        mContext = context.getApplicationContext();

        //Get the database using the CrimeBaseHelper class
        mDatabase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();

    }

    //Add a new crime to the list
    public void addCrime(Crime c) {
        //Get the database content values by passing in the crime
        ContentValues values = getContentValues(c);
        //Store the crime in the database
        //SQL will look like:
        //insert into crimes (uuid, ...) values (?, ...)
        mDatabase.insert(CrimeTable.NAME, null,values);
    }

    public void updateCrime(Crime crime) {
        //Get the UUID from the crime and convert it to a string
        String uuidString = crime.getId().toString();
        //Get the content values for the crime
        ContentValues values = getContentValues(crime);

        //Update the record with the content values.
        //Params are: Table Name, Values, Where Clause, Where Params
        //The SQL Will look something like the following
        //update crimes set (uuid = ?, ...) where uuid = ?;
        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                //CrimeTable.Cols.UUID + " = " + uuidString
                new String[] { uuidString });
    }

    //Getter for all of the crimes
    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        //Passing null, null for the where clause and args
        //will allow us to get all of the results with no
        //constraints
        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            //This will move to the first record in the result set
            cursor.moveToFirst();
            //While the cursor is still in the result set
            while(!cursor.isAfterLast()) {
                //Add the crime to the array list
                crimes.add(cursor.getCrime());
                //Move to the next record in the result list
                cursor.moveToNext();
            }
        } finally {
            //Be sure to always close the cursor
            cursor.close();
        }
        //Return the list of crimes
        return crimes;
    }

    //Getter for a single crime
    public Crime getCrime(UUID id) {

        //Use the CrimeCursorWrapper to get out a single
        //crime based on the UUID.
        //1st Param is the where clause,
        //2nd is the string array for the where args
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        //Since we need to close the cursor in the finally,
        //we put this in a try
        try {
            //Check to see if there were results
            //There almost ALWAYS should be, but just in case
            if (cursor.getCount() == 0) {
                return null;
            }

            //Move to the first result
            cursor.moveToFirst();
            //get the crime from the getCrime method which does the mapping
            return cursor.getCrime();
        } finally {
            //Be sure to close the cursor
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Crime crime) {
        //Make a new instance of content values
        ContentValues values = new ContentValues();

        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);

        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, //Columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null  //orderBy
        );

        return new CrimeCursorWrapper(cursor);
    }
}
