package edu.kvcc.cis298.cis298inclass4;

/**
 * Created by cisco on 12/4/2017.
 */

//A Class full of constants that we can use when
//interacting with our database
public class CrimeDbSchema {
    //Table
    public static final class CrimeTable {
        //Name
        public static final String NAME = "crimes";

        //Columns
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
        }
    }
}
