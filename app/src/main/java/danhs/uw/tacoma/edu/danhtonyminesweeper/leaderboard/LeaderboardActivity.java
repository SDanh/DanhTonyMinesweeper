package danhs.uw.tacoma.edu.danhtonyminesweeper.leaderboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import danhs.uw.tacoma.edu.danhtonyminesweeper.R;
import danhs.uw.tacoma.edu.danhtonyminesweeper.data.Stats;
import danhs.uw.tacoma.edu.danhtonyminesweeper.authenticate.SignInActivity;

/**
 * An activity for the Leaderboard
 */
public class LeaderboardActivity extends AppCompatActivity implements LeaderboardFragment.LeaderboardInteractionListener {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);



        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.fragment_leaderboard_list) == null) {
            LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.leaderboard_fragment_container, leaderboardFragment)
                    .commit();
        }
        /*
        LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.leaderboard_fragment_container, leaderboardFragment)
                .commit();
        */

    }

    //attaches menu to welcome activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //step 22 logout
        if (id == R.id.action_logout) {
            SharedPreferences sharedPreferences =
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                    .commit();

            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Implements the LeaderboardInteractionListener
     * @param stats A Stats object.
     */
    public void leaderboardInteraction(Stats stats) {

    }








}
