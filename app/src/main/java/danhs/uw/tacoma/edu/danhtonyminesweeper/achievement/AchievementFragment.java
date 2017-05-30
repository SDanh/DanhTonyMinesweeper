package danhs.uw.tacoma.edu.danhtonyminesweeper.achievement;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import danhs.uw.tacoma.edu.danhtonyminesweeper.R;
import danhs.uw.tacoma.edu.danhtonyminesweeper.data.Achievements;
import danhs.uw.tacoma.edu.danhtonyminesweeper.data.AchievementsDB;
import danhs.uw.tacoma.edu.danhtonyminesweeper.data.Stats;
import danhs.uw.tacoma.edu.danhtonyminesweeper.data.StatsDB;

public class AchievementFragment extends Fragment {

    private static final String TAG = "AchievementFragment";

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ACHIEVEMENT_URL
            = "http://cssgate.insttech.washington.edu/~_450bteam15/achievements.php";


    private int mColumnCount = 1;
    private AchievementInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private AchievementsDB mAchievementsDB;
    private List<Achievements> mAchievementsList;
    private StatsDB mStatsDB;
    private SharedPreferences mSharedPreferences;


    public AchievementFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.username)
                , Context.MODE_PRIVATE);

    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_achievement, container, false);
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievement_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            DownloadAchievementTask task = new DownloadAchievementTask();
            task.execute(new String[]{ACHIEVEMENT_URL});
        }


        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            DownloadAchievementTask task = new DownloadAchievementTask();
            task.execute(new String[]{ACHIEVEMENT_URL});
        } else {
            Toast.makeText(view.getContext(),
                    "No network connection available. Displaying locally stored data",
                    Toast.LENGTH_SHORT).show();
            if (mAchievementsDB == null) {
                mAchievementsDB = new AchievementsDB(getActivity());
            }
            if (mStatsDB == null) {
                mStatsDB = new StatsDB(getActivity());
            }
            // filter out achievements not earned
            if (mAchievementsList == null) {
                List<Achievements> tempAchievementsList = mAchievementsDB.getAchievements();
                //mAchievementsList = getAchievementsEarned(tempAchievementsList);
                mAchievementsList = tempAchievementsList;
            }
            mRecyclerView.setAdapter(new AchievementRecyclerViewAdapter(mAchievementsList, mListener));
        }
        //Read from file and show the text

        try {
            InputStream inputStream = getActivity().openFileInput(
                    getString(R.string.LOGIN_FILE));

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                Toast.makeText(getActivity(), stringBuilder.toString(), Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }















    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AchievementInteractionListener) {
            mListener = (AchievementInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AchievementInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface AchievementInteractionListener {
        // TODO: Update argument type and name
        void achievementInteraction(Achievements achievements);
    }


    /**
     * Gets the achievements user has actually earned from a list of all achievements
     * @param fullAchievementsList A list of all achievements
     * @return A list of achievements actually earned.
     */
    public List<Achievements> getAchievementsEarned(List<Achievements> fullAchievementsList) {
        // filter out achievements not earned
        Log.d(TAG, "getter entered");
        ArrayList<Achievements> achievementsList = new ArrayList<>();
        for(int i = 0; i < fullAchievementsList.size(); i++) {
            Achievements achievements = fullAchievementsList.get(i);
            Map<String, Integer> condition = achievements.getConditionMap();

            List<Stats> statsList = mStatsDB.getStats();
            Stats account = null;
            // get stats for the account currently logged in.
            for(Stats stats: statsList) {
                if(stats.getUsername().equals(mSharedPreferences.getString(getString(R.string.username), null))) {
                    account = stats;
                }
            }
            boolean conditionMet = false;
            if(account != null) {
                conditionMet = true;
                // go through all the conditions
                for (String key : condition.keySet()) {
                    // database uses key played
                    // sqlite uses key games
                    if (key.equals("played")) {
                        key = "games";
                    }
                    // condition <= account means condition met
                    conditionMet &= account.get(key).compareTo(condition.get(key)) >= 0;
                }
            }

            // if condition is met add to list of achievements
            if(conditionMet) {
                achievementsList.add(achievements);
            }
        }
        Log.d(TAG, achievementsList.toString());
        return achievementsList;
    }




    private class DownloadAchievementTask extends AsyncTask<String, Void, String> {

        private AchievementsDB mAchievementsDB;
        private List<Achievements> mAchievementsList;


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
                    response = "Unable to download Achievements, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            Log.d(TAG,result);
            List<Achievements> tempAchievementsList = new ArrayList<Achievements>();
            String statusresult = Achievements.parseAchievementsJSON(result, tempAchievementsList);
            // filter out achievements not earned
            //mAchievementsList = getAchievementsEarned(tempAchievementsList);
            mAchievementsList = tempAchievementsList;









            // Something wrong with the JSON returned.
            if (statusresult != null) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            // Everything is good, show the list of courses.
            if (!mAchievementsList.isEmpty()) {

                if (mAchievementsDB == null) {
                    mAchievementsDB = new AchievementsDB(getActivity());
                }

                // Delete old data so that you can refresh the local
                // database with the network data.
                mAchievementsDB.deleteAchievements();


                // Also, add to the local database
                for (int i = 0; i < mAchievementsList.size(); i++) {
                    Achievements achievements = mAchievementsList.get(i);
                    mAchievementsDB.insertAchievements(achievements.getName(),
                            achievements.getDescription(),
                            achievements.getCondition());
                }



                //mFirstCourse = mAchievementsList.get(0);
                mRecyclerView.setAdapter(new AchievementRecyclerViewAdapter(mAchievementsList, mListener));
            }
        }

    }

}
