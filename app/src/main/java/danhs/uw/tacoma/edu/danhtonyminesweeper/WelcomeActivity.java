package danhs.uw.tacoma.edu.danhtonyminesweeper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import danhs.uw.tacoma.edu.danhtonyminesweeper.achievement.AchievementActivity;
import danhs.uw.tacoma.edu.danhtonyminesweeper.authenticate.SignInActivity;
import danhs.uw.tacoma.edu.danhtonyminesweeper.leaderboard.LeaderboardActivity;

/**
 * The starting point for the app.
 */
public class WelcomeActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welcomeTextView = (TextView) findViewById(R.id.welcome_text_view);
    }

    //attaches main menu to main activity
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

    @Override
    public void onResume() {
        super.onResume();
        setWelcomeTextView();
    }


    /**
     * Sets the textview on the screen.
     */
    private void setWelcomeTextView() {
        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
        String s = "Welcome\n" +
                mSharedPreferences.getString(getString(R.string.username), "");
        welcomeTextView.setText(s);
    }

    /**
     * Listener for the leaderboard button.
     * @param view The view.
     */
    public void openLeaderboard(View view){
        Intent i = new Intent(this, LeaderboardActivity.class);
        startActivity(i);
        //finish();
    }


    /**
     * Listener for the Achievement button.
     * @param view The view.
     */
    public void openAchievement(View view){
        Intent i = new Intent(this, AchievementActivity.class);
        startActivity(i);
        //finish();
    }
}
