package danhs.uw.tacoma.edu.danhtonyminesweeper.achievement;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import danhs.uw.tacoma.edu.danhtonyminesweeper.R;
import danhs.uw.tacoma.edu.danhtonyminesweeper.data.Achievements;
import danhs.uw.tacoma.edu.danhtonyminesweeper.achievement.AchievementFragment.AchievementInteractionListener;

public class AchievementRecyclerViewAdapter extends RecyclerView.Adapter<AchievementRecyclerViewAdapter.ViewHolder> {

    private final List<Achievements> mValues;
    private final AchievementInteractionListener mListener;

    public AchievementRecyclerViewAdapter(List<Achievements> items, AchievementInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_achievement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mDescriptionView.setText(mValues.get(position).getDescription());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.achievementInteraction(holder.mItem);
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
        public final TextView mNameView;
        public final TextView mDescriptionView;

        public Achievements mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mDescriptionView = (TextView) view.findViewById(R.id.description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}