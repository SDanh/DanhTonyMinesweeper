package danhs.uw.tacoma.edu.danhtonyminesweeper.game;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadFactory;

import danhs.uw.tacoma.edu.danhtonyminesweeper.R;

/**
 * Minesweeper Activity holds the visual user interface for a functional game of Minesweeper.
 * This game allows you to tap or flag a cell and shows the effects it has on the board.
 */
public class MinesweeperActivity extends AppCompatActivity {

    Game mGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minesweeper);

        //create a new minesweeper game
        mGame = new Game(10,10,0.1);
        mGame.NewGame();

        //toggle button for flag
        ToggleButton toggle = (ToggleButton) findViewById(R.id.Flag_Toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                   mGame.setToggle(true);
                } else {
                   mGame.setToggle(false);
                }
            }
        });

        // Get the widgets reference from XML layout
        final GridView gv = (GridView) findViewById(R.id.gv);
        gv.setNumColumns(mGame.getWidth());

        final TextView tv = (TextView) findViewById(R.id.tv);
        final ArrayAdapter<String> GameBoardAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,AdaptPlayingBoard());
        gv.setAdapter(GameBoardAdapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the GridView selected/clicked item text
                int xPos = position % mGame.getLength();
                int yPos = (position - xPos) / mGame.getWidth() + 1;
                xPos++;

                Log.d("Cell", position+1 + "("+ xPos + ", " +yPos + "): " + mGame.getSolution()[yPos-1][xPos-1]);

                //If toggled. remove flag of flagged cells and add flage to unflagged cells.
                //Else tap cell
                if(mGame.getToggle()){
                    if(mGame.isFlagged(xPos,yPos)){
                        mGame.removeFlag(xPos,yPos);
                    }
                    else mGame.flagCell(xPos,yPos);
                }
                else mGame.tapCell(xPos, yPos);

                //update game and update adapter to display changed playing board
                GameBoardAdapter.clear();
                GameBoardAdapter.addAll(AdaptPlayingBoard());
                GameBoardAdapter.notifyDataSetChanged();

                String selectedItem = parent.getItemAtPosition(position).toString();

                // Display the selected/clicked item text and position on TextView
                tv.setText("GridView item clicked : " +selectedItem  + "\nAt index position : " + position + " (" + xPos + "," + yPos + ")");
                if(selectedItem.equals(Game.MINE)){
                    Toast.makeText(MinesweeperActivity.this, R.string.Game_Over_Text, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

    }

    //adapts the playing board as a string for Array Adapter
    public List<String> AdaptPlayingBoard(){
        String[][]temp = mGame.getPlaying_board();
        ArrayList<String> singleDArray = new ArrayList<String>();
        for (String[] array :temp) {
            singleDArray.addAll(Arrays.asList(array));
        }
        return singleDArray;
    }

}
