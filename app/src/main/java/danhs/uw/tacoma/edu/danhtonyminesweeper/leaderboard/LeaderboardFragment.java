package danhs.uw.tacoma.edu.danhtonyminesweeper.leaderboard;

import android.content.Context;
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

import danhs.uw.tacoma.edu.danhtonyminesweeper.R;
import danhs.uw.tacoma.edu.danhtonyminesweeper.data.Stats;
import danhs.uw.tacoma.edu.danhtonyminesweeper.data.StatsDB;

/**
 * A fragment for the leaderboard
 */
public class LeaderboardFragment extends Fragment {

    private static final String TAG = "LeaderboardFragment";

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String LEADERBOARD_URL
            = "http://cssgate.insttech.washington.edu/~_450bteam15/list.php";


    private int mColumnCount = 1;
    private LeaderboardInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private StatsDB mStatsDB;
    private List<Stats> mStatsList;


    /**
     * Constructor
     */
    public LeaderboardFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            DownloadLeaderboardTask task = new DownloadLeaderboardTask();
            task.execute(new String[]{LEADERBOARD_URL});
        }


        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            DownloadLeaderboardTask task = new DownloadLeaderboardTask();
            task.execute(new String[]{LEADERBOARD_URL});
        }
        else {
            Toast.makeText(view.getContext(),
                    "No network connection available. Displaying locally stored data",
                    Toast.LENGTH_SHORT) .show();
            if (mStatsDB == null) {
                mStatsDB = new StatsDB(getActivity());
            }
            if (mStatsList == null) {
                mStatsList = mStatsDB.getStats();
            }
            mRecyclerView.setAdapter(new LeaderboardRecyclerViewAdapter(mStatsList, mListener));
        }
        //Read from file and show the text

        try {
            InputStream inputStream = getActivity().openFileInput(
                    getString(R.string.LOGIN_FILE));

            if ( inputStream != null ) {
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
        if (context instanceof LeaderboardInteractionListener) {
            mListener = (LeaderboardInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LeaderboardInteractionListener");
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
    public interface LeaderboardInteractionListener {
        // TODO: Update argument type and name
        void leaderboardInteraction(Stats stats);
    }


    /**
     * An AsyncTask to get the leaderboard from the server.
     */
    private class DownloadLeaderboardTask extends AsyncTask<String, Void, String> {

        private StatsDB mStatsDB;
        private List<Stats> mStatsList;


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
                    response = "Unable to download Stats, Reason: "
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
            mStatsList = new ArrayList<Stats>();
            result = Stats.parseStatsJSON(result, mStatsList);

            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            // Everything is good, show the list of courses.
            if (!mStatsList.isEmpty()) {

                if (mStatsDB == null) {
                    mStatsDB = new StatsDB(getActivity());
                }

                // Delete old data so that you can refresh the local
                // database with the network data.
                mStatsDB.deleteStats();

                // Also, add to the local database
                for (int i = 0; i < mStatsList.size(); i++) {
                    Stats stats = mStatsList.get(i);
                    mStatsDB.insertStats(stats.getUsername(),
                            stats.getGames(),
                            stats.getWon(),
                            stats.getLost());
                }


                //mFirstCourse = mStatsList.get(0);
                mRecyclerView.setAdapter(new LeaderboardRecyclerViewAdapter(mStatsList, mListener));
            }
        }



    }

}
