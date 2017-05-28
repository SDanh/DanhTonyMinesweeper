package danhs.uw.tacoma.edu.danhtonyminesweeper.game;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Computer User on 5/28/2017.
 */

public class testAdapter extends BaseAdapter {

    private final Context mContext;
    private String[] Game_Cells;

    /**
     * Public Constructor
     * @param context - the context which in this case is Game_Board_Activity.
     * @param Cells - the list of names/postions used to create the cells.
     */
    public testAdapter(Context context, String[] Cells){
        this.mContext = context;
        this.Game_Cells = Cells;

    }

    /**
     *Returns the number of Game Cells for the given game.
     * @return an int - the number of Game Cells
     */
    @Override
    public int getCount() {
        return Game_Cells.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Overwrite of getView. Game_Cell_Adapter's getView returns a button assigned to a specific
     * name/position. The button also gets the behavior to disappear when clicked.
     * @param position - the position of the Cell.
     * @param convertView
     * @param parent
     * @return button - a button assigned to the specific name/position that will disappear when clicked.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Button button = new Button(mContext);
        button.setText(Game_Cells[position]);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext, button.getText(), Toast.LENGTH_SHORT).show();
                //button.setVisibility(View.GONE);
            }
        });
        return button;

    }

    public class CoordPair {
        private final int x, y;
        public CoordPair(int x, int y) {
            this.x=x;this.y=y;
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }
        public boolean equals(Game.CoordPair cp){
            return (this.getX() == cp.getX() && this.getY() == cp.getY());
        }
    }

    public void updateAdapter(String[][] newBoard){
        String[][]temp = newBoard;
        ArrayList<String> singleDArray = new ArrayList<String>();
        for (String[] array :temp) {
            singleDArray.addAll(Arrays.asList(array));
        }
        String[] sd = singleDArray.toArray(new String[singleDArray.size()]);
        this.Game_Cells = sd;
        this.notifyDataSetChanged();
    }
}
