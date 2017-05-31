package danhs.uw.tacoma.edu.danhtonyminesweeper.leaderboard;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import danhs.uw.tacoma.edu.danhtonyminesweeper.R;
//import danhs.uw.tacoma.edu.danhtonyminesweeper.account.Account;
import danhs.uw.tacoma.edu.danhtonyminesweeper.data.Stats;
import danhs.uw.tacoma.edu.danhtonyminesweeper.leaderboard.LeaderboardFragment.LeaderboardInteractionListener;

/**
 * A RecyclerViewAdapter for the leaderboard.
 */
public class LeaderboardRecyclerViewAdapter extends RecyclerView.Adapter<LeaderboardRecyclerViewAdapter.ViewHolder> {

    private final List<Stats> mValues;
    private final LeaderboardInteractionListener mListener;

    /**
     * Constructor
     * @param items A list of Stats.
     * @param listener A listener.
     */
    public LeaderboardRecyclerViewAdapter(List<Stats> items, LeaderboardInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mUsernameView.setText(mValues.get(position).getUsername());
        holder.mGamesView.setText(mValues.get(position).getGames());
        holder.mWonView.setText(mValues.get(position).getWon());
        holder.mLostView.setText(mValues.get(position).getLost());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.leaderboardInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mUsernameView;
        public final TextView mGamesView;
        public final TextView mWonView;
        public final TextView mLostView;

        public Stats mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUsernameView = (TextView) view.findViewById(R.id.username);
            mGamesView = (TextView) view.findViewById(R.id.games);
            mWonView = (TextView) view.findViewById(R.id.won);
            mLostView = (TextView) view.findViewById(R.id.lost);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mGamesView.getText() + "'";
        }
    }
}