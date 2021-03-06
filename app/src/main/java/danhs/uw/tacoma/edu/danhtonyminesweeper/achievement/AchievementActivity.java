package danhs.uw.tacoma.edu.danhtonyminesweeper.achievement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import danhs.uw.tacoma.edu.danhtonyminesweeper.R;
import danhs.uw.tacoma.edu.danhtonyminesweeper.data.Achievements;
import danhs.uw.tacoma.edu.danhtonyminesweeper.authenticate.SignInActivity;

/**
 * An activity for Achievements.
 */
public class AchievementActivity extends AppCompatActivity implements AchievementFragment.AchievementInteractionListener {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);



        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.fragment_achievement_list) == null) {
            AchievementFragment achievementFragment = new AchievementFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.achievement_fragment_container, achievementFragment)
                    .commit();
        }
        /*
        AchievementFragment achievementFragment = new AchievementFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.achievement_fragment_container, achievementFragment)
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
     * Implements the achievementInteractionListener interface.
     * @param achievements An Achievements object.
     */
    public void achievementInteraction(Achievements achievements) {

    }
}
