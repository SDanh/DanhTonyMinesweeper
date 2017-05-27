package danhs.uw.tacoma.edu.danhtonyminesweeper.authenticate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import danhs.uw.tacoma.edu.danhtonyminesweeper.MainActivity;
import danhs.uw.tacoma.edu.danhtonyminesweeper.R;
import danhs.uw.tacoma.edu.danhtonyminesweeper.WelcomeActivity;

public class SignInActivity extends AppCompatActivity implements LoginFragment.LoginInteractionListener{

    private static final String TAG = "SignInActivity";

    private final static String LOGIN_URL = "http://cssgate.insttech.washington.edu/~_450bteam15/login.php?";
    private final static String REGISTER_URL = "http://cssgate.insttech.washington.edu/~_450bteam15/addAccount.php?";



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
    public void login(String username, String password) {

        String url = buildLoginURL(username, password);
        LoginTask loginTask = new LoginTask();
        loginTask.execute(new String[]{url.toString()});


        //Bug must click login button twice to return to welcome screen
        //The check to go to the welcome screen finishes before the async task sets the value being checked.
        //It is an asynchronous threading issue

        /*
        // does nothing
        try {
            loginTask.get(10000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            Log.e(TAG, "login ", e);
        }
        */

        /*
        // program crashes
        try {
            this.wait(5000);
        } catch(InterruptedException e) {

        }
        */

        if(mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            Intent i = new Intent(this, WelcomeActivity.class);
            startActivity(i);
            finish();
        }
    }

    /**
     * implements login fragment
     */
    @Override
    public void register(String username, String password) {

        String url = buildRegisterURL(username, password);
        RegisterTask registerTask = new RegisterTask();
        registerTask.execute(new String[]{url.toString()});

        //Bug must click register button twice to return to welcome screen
        //The check to go to the welcome screen finishes before the async task sets the value being checked.
        //It is an asynchronous threading issue





        //mSharedPreferences
        //        .edit()
        //        .putBoolean(getString(R.string.LOGGEDIN), true)
        //        .commit();
        if(mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            Intent i = new Intent(this, WelcomeActivity.class);
            startActivity(i);
            finish();
        }
    }






    /**
     * Makes a url to get information from a server.
     * @param v the view.
     * @return The url.
     */
    private String buildLoginURL(String username, String password) {
        return buildURL(LOGIN_URL, username, password);
    }

    private String buildRegisterURL( String username, String password) {
        return buildURL(REGISTER_URL, username, password);
    }

    private String buildURL(String url, String username, String password) {
        StringBuilder sb = new StringBuilder(url);
        try {
            sb.append("username=");
            sb.append(username);

            sb.append("&password=");
            sb.append(URLEncoder.encode(password, "UTF-8"));

            Log.i(TAG, sb.toString());
        }
        catch(Exception e) {
            Log.e(TAG, "url build failed", e);
            //Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
            //        .show();
        }
        return sb.toString();
    }






    /**
     * Sends a query to the server. Checks if login credentials are valid.
     */
    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }



                } catch (Exception e) {
                    response = "Unable to login, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {

                    //SharedPreferences sharedPreferences =
                    //        getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                    //sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), true)
                    //        .commit();


                    mSharedPreferences
                            .edit()
                            .putBoolean(getString(R.string.LOGGEDIN), true)
                            .commit();

                    Toast.makeText(getApplicationContext(), "Login successful!"
                            , Toast.LENGTH_LONG)
                            .show();
                    //Log.i()


                } else {

                    Toast.makeText(getApplicationContext(), "Login failed: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data\n" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Login task", "Something wrong with the data", e);
            }
        }
    }





    /**
     * Sends a query to the server. Adds an account to the server database.
     */
    private class RegisterTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    Log.d(TAG, urlConnection.toString());
                    InputStream content = urlConnection.getInputStream();
                    Log.d(TAG, "I made it here3");

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Unable to add Account, Reason: "+ e.toString(), e);
                    response = "Unable to add Account, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Account successfully added!"
                            , Toast.LENGTH_LONG)
                            .show();

                    mSharedPreferences
                            .edit()
                            .putBoolean(getString(R.string.LOGGEDIN), true)
                            .commit();

                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }





            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }









}
