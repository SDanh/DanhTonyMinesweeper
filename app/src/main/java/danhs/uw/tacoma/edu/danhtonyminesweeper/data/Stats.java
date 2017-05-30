package danhs.uw.tacoma.edu.danhtonyminesweeper.data;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Stats {
    public static final String USERNAME = "username";
    public static final String GAMES = "played";
    public static final String WON = "won";
    public static final String LOST= "lost";

    private String username;
    // string representation of an integer
    private String games;
    private String won;
    private String lost;

    public Stats(String username, String games, String won, String lost) {
        this.username = username;
        this.games = games;
        this.won = won;
        this.lost = lost;
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns course list if success.
     * @param statsJSON
     * @return reason or null if successful.
     */
    public static String parseStatsJSON(String statsJSON, List<Stats> statsList) {
        String reason = null;
        if (statsJSON != null) {
            try {
                JSONArray arr = new JSONArray(statsJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Stats stats = new Stats(obj.getString(Stats.USERNAME), obj.getString(Stats.GAMES)
                            , obj.getString(Stats.WON), obj.getString(Stats.LOST));
                    statsList.add(stats);
                }
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }

        }
        return reason;
    }



    public String getUsername() {
        return username;
    }

    //public void setUsername(String username) {
    //    this.username = username;
    //}

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }
    public void setGames(Integer games) {
        this.games = games.toString();
    }

    public String getWon() {
        return won;
    }

    public void setWon(String won) {
        this.won = won;
    }
    public void setWon(Integer won) {
        this.won = won.toString();
    }

    public String getLost() {
        return lost;
    }

    public void setLost(String lost) {
        this.lost = lost;
    }
    public void setLost(Integer lost) {
        this.lost = lost.toString();
    }



    public Integer get(String key) {
        Integer i = null;
        switch (key) {
            case "games":
                return Integer.parseInt(games);
            case "won":
                return Integer.parseInt(won);
            case "lost":
                return Integer.parseInt(lost);
            default:
                return null;
        }
    }
}
