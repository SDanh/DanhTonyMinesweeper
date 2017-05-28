package danhs.uw.tacoma.edu.danhtonyminesweeper.game;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import danhs.uw.tacoma.edu.danhtonyminesweeper.R;

/**
 * Created by Computer User on 5/28/2017.
 */

public class Game_Cell_Adapter extends BaseAdapter {

    private final Context mContext;


    private String[][] board;
    private int xPos;
    private int yPos;
    private int length;
    private int width;

    public final String HIDDEN = "X";
    public final String MINE = "*";
    public final String EMPTY = ".";

    /**
     * Public Constructor
     * @param context - the context which in this case is Game_Board_Activity.
     */
    public Game_Cell_Adapter(Context context, Game theGame){
        this.mContext = context;
        board = theGame.getPlaying_board();
        length = theGame.getLength();
        width = theGame.getWidth();
    }

    /**
     *Returns the number of Game Cells for the given game.
     * @return an int - the number of Game Cells
     */
    @Override
    public int getCount() {
        return 0;
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

        xPos = position % length;
        yPos = (position - xPos) / width;

        final Button cell = new Button(mContext);
        cell.setText(position);
        cell.setVisibility(View.VISIBLE);

        switch(board[xPos][yPos]){
            case HIDDEN:
                cell.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
                break;
            case MINE:
                cell.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
            case EMPTY:
                cell.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                break;
            default:
                cell.setText(board[xPos][yPos]);
                cell.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                break;

        }
        return cell;


    }
}
