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
 * A database for game statistics.
 */
public class StatsDB {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Stats.db";
    private static final String STATS_TABLE = "Stats";

    private StatsDBHelper mStatsDBHelper;
    private SQLiteDatabase mSQLiteDatabase;


    /**
     * Constructor
     * @param context The context.
     */
    public StatsDB(Context context) {
        mStatsDBHelper = new StatsDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mStatsDBHelper.getWritableDatabase();
    }



    /**
     * Inserts the course into the local sqlite table. Returns true if successful, false otherwise.
     * @param username The username for the account.
     * @param games The number of games played.
     * @param won The number of games won.
     * @param lost The number of games lost.
     * @return true if insertion into SQLite database was successful, false otherwise.
     */
    public boolean insertStats(String username, String games, String won, String lost) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("games", games);
        contentValues.put("won", won);
        contentValues.put("lost", lost);

        long rowId = mSQLiteDatabase.insert("Stats", null, contentValues);
        return rowId != -1;
    }


    /**
     * Delete all the data from the STATS_TABLE
     */
    public void deleteStats() {
        mSQLiteDatabase.delete(STATS_TABLE, null, null);
    }


    /**
     * Closes the database.
     */
    public void closeDB() {
        mSQLiteDatabase.close();
    }


    /**
     * Returns the list of statistics from the local database.
     * @return A list of statistics.
     */
    public List<Stats> getStats() {

        String[] columns = {
                "username", "games", "won", "lost"
        };

        Cursor c = mSQLiteDatabase.query(
                STATS_TABLE,  // The table to query
                columns,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();
        List<Stats> list = new ArrayList<Stats>();
        for (int i=0; i<c.getCount(); i++) {
            String username = c.getString(0);
            String games = c.getString(1);
            String won = c.getString(2);
            String lost = c.getString(3);
            Stats stats = new Stats(username, games, won, lost);
            list.add(stats);
            c.moveToNext();
        }

        return list;
    }


    /**
     * Helper class
     */
    class StatsDBHelper extends SQLiteOpenHelper {

        private final String CREATE_STATS_SQL;

        private final String DROP_STATS_SQL;

        /**
         * Constructor
         * @param context
         * @param name
         * @param factory
         * @param version
         */
        public StatsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_STATS_SQL = context.getString(R.string.CREATE_STATS_SQL);
            DROP_STATS_SQL = context.getString(R.string.DROP_STATS_SQL);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_STATS_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_STATS_SQL);
            onCreate(sqLiteDatabase);
        }
    }
}
