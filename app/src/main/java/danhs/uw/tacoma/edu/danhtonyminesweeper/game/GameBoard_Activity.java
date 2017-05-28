package danhs.uw.tacoma.edu.danhtonyminesweeper.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;

import danhs.uw.tacoma.edu.danhtonyminesweeper.R;

public class GameBoard_Activity extends AppCompatActivity {

    /**
     * The GridView used to represent the 5 column Minesweeper Gameboard with A rows.
     */
    GridView gridView;

    Game mGame;

    /**
     * Overwrite of OncCreate. Uses the Game_Cell_Adapter to populatre the gridview with
     * clickable buttons.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_board);

        gridView = (GridView) findViewById(R.id.game_board);
        mGame = new Game(10, 10, 0.2);
        mGame.NewGame();



        String[][]temp = mGame.getPlaying_board();
        ArrayList<String> singleDArray = new ArrayList<String>();
        for (String[] array :temp) {
            singleDArray.addAll(Arrays.asList(array));
        }
        String[] sd = singleDArray.toArray(new String[singleDArray.size()]);

        final testAdapter adapter = new testAdapter(this, sd);


        mGame.tapCell(1,1);
        adapter.updateAdapter(mGame.getPlaying_board());

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int xpos= position/mGame.getLength();
                int ypos=position%mGame.getWidth();

                mGame.tapCell(xpos, ypos);

                adapter.updateAdapter(mGame.getPlaying_board());

            }
        });


    }
}
