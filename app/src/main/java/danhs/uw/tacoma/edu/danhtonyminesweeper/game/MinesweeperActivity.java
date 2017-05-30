package danhs.uw.tacoma.edu.danhtonyminesweeper.game;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

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
                mGame.tapCell(xPos, yPos);
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

}
