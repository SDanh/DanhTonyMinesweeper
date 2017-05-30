package danhs.uw.tacoma.edu.danhtonyminesweeper.game;

import android.content.Context;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import danhs.uw.tacoma.edu.danhtonyminesweeper.R;

public class MinesweeperActivity extends AppCompatActivity {

    Game mGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minesweeper);



        mGame = new Game(10,10,0.1);
        mGame.NewGame();

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
        GridView gv = (GridView) findViewById(R.id.gv);
        final TextView tv = (TextView) findViewById(R.id.tv);

        final ArrayAdapter<String> GameBoardAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,AdaptPlayingBoard());

        gv.setAdapter(GameBoardAdapter);

        //gv.setAdapter(new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1,plantsList));

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the GridView selected/clicked item text
                int xPos = position % mGame.getLength();
                int yPos = (position - xPos) / mGame.getWidth() + 1;
                xPos++;

                Log.d("Cell", position+1 + "("+ xPos + ", " +yPos + "): " + mGame.getSolution()[yPos-1][xPos-1]);
                if(mGame.getToggle()){
                    if(mGame.isFlagged(xPos,yPos)){
                        mGame.removeFlag(xPos,yPos);
                    }
                    else mGame.flagCell(xPos,yPos);
                }
                else mGame.tapCell(xPos, yPos);
                GameBoardAdapter.clear();
                GameBoardAdapter.addAll(AdaptPlayingBoard());
                GameBoardAdapter.notifyDataSetChanged();

                String selectedItem = parent.getItemAtPosition(position).toString();

                // Display the selected/clicked item text and position on TextView
                tv.setText("GridView item clicked : " +selectedItem
                        + "\nAt index position : " + position + " (" + xPos + "," + yPos + ")");
            }
        });

    }

    public List<String> AdaptPlayingBoard(){
        String[][]temp = mGame.getPlaying_board();
        ArrayList<String> singleDArray = new ArrayList<String>();
        for (String[] array :temp) {
            singleDArray.addAll(Arrays.asList(array));
        }
        return singleDArray;
    }
    public List<ImageView> AdaptPlayingBoard2(){
        String[][]temp = mGame.getPlaying_board();
        ArrayList<String> singleDArray = new ArrayList<String>();
        ArrayList<ImageView> ImgArray = new ArrayList<ImageView>();

        for (String[] array :temp) {
            singleDArray.addAll(Arrays.asList(array));
        }
        for(String cell: singleDArray){
            ImageView tempIV = new ImageView(this);
            if(cell.equals(Game.HIDDEN)){
                //tempTV.s
            }
            if(cell.equals(Game.EMPTY)){
                //
            }
            if(cell.equals(Game.MINE)){
                //
            }
            if(cell.equals(Game.FLAG)){
                //
            }
            ImgArray.add(tempIV);
        }
        return ImgArray;
    }

}
