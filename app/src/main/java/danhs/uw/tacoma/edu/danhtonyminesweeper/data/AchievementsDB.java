package danhs.uw.tacoma.edu.danhtonyminesweeper.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import danhs.uw.tacoma.edu.danhtonyminesweeper.R;

/**
 * A SQLite database of achievements.
 */
public class AchievementsDB {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Achievements.db";
    private static final String ACHIEVEMENTS_TABLE = "Achievements";

    private AchievementsDBHelper mAchievementsDBHelper;
    private SQLiteDatabase mSQLiteDatabase;


    /**
     * Constructor
     * @param context The context
     */
    public AchievementsDB(Context context) {
        mAchievementsDBHelper = new AchievementsDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mAchievementsDBHelper.getWritableDatabase();
    }



    /**
     * Inserts the course into the local sqlite table. Returns true if successful, false otherwise.
     * @param name The name of the achievement.
     * @param description The description of the achievement.
     * @param condition The condition of the achievement.
     * @return true if SQLite insertion was successful, false otherwise.
     */
    public boolean insertAchievements(String name, String description, String condition) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("condition", condition);

        long rowId = mSQLiteDatabase.insert("Achievements", null, contentValues);
        return rowId != -1;
    }


    /**
     * Delete all the data from the ACHIEVEMENTS_TABLE
     */
    public void deleteAchievements() {
        mSQLiteDatabase.delete(ACHIEVEMENTS_TABLE, null, null);
    }


    /**
     * Closes the database.
     */
    public void closeDB() {
        mSQLiteDatabase.close();
    }


    /**
     * Returns the list of courses from the local Course table.
     * @return A list of achievements.
     */
    public List<Achievements> getAchievements() {

        String[] columns = {
                "name", "description", "condition"
        };

        Cursor c = mSQLiteDatabase.query(
                ACHIEVEMENTS_TABLE,  // The table to query
                columns,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();
        List<Achievements> list = new ArrayList<Achievements>();
        for (int i=0; i<c.getCount(); i++) {
            String name = c.getString(0);
            String description = c.getString(1);
            String condition = c.getString(2);
            Achievements achievements = new Achievements(name, description, condition);
            list.add(achievements);
            c.moveToNext();
        }

        return list;
    }


    /**
     * A helper class.
     */
    class AchievementsDBHelper extends SQLiteOpenHelper {

        private final String CREATE_ACHIEVEMENTS_SQL;

        private final String DROP_ACHIEVEMENTS_SQL;

        /**
         * constructor
         * @param context
         * @param name
         * @param factory
         * @param version
         */
        public AchievementsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_ACHIEVEMENTS_SQL = context.getString(R.string.CREATE_ACHIEVEMENTS_SQL);
            DROP_ACHIEVEMENTS_SQL = context.getString(R.string.DROP_ACHIEVEMENTS_SQL);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_ACHIEVEMENTS_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_ACHIEVEMENTS_SQL);
            onCreate(sqLiteDatabase);
        }
    }
}
