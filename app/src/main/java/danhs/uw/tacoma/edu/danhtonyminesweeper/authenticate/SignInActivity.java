package danhs.uw.tacoma.edu.danhtonyminesweeper.authenticate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import danhs.uw.tacoma.edu.danhtonyminesweeper.MainActivity;
import danhs.uw.tacoma.edu.danhtonyminesweeper.R;
import danhs.uw.tacoma.edu.danhtonyminesweeper.WelcomeActivity;

public class SignInActivity extends AppCompatActivity implements LoginFragment.LoginInteractionListener{

    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //if not login ask for login, else go to WelcomeActivity
        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new LoginFragment())
                    .commit();
        } else {
            Intent i = new Intent(this, WelcomeActivity.class);
            startActivity(i);
            finish();
        }

    }

    /**
     * implements login fragment
     */
    @Override
    public void login(String userId, String pwd) {
        mSharedPreferences
                .edit()
                .putBoolean(getString(R.string.LOGGEDIN), true)
                .commit();
        Intent i = new Intent(this, WelcomeActivity.class);
        startActivity(i);
        finish();
    }
}
