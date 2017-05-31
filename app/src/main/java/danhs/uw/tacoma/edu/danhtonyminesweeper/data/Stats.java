package danhs.uw.tacoma.edu.danhtonyminesweeper.data;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Holds statistics for an account.
 */
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

    /**
     * Constructor
     * @param username The username for the account.
     * @param games The number of games played.
     * @param won The number of games won.
     * @param lost The number of games lost.
     */
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


    /**
     * Gets the username.
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    //public void setUsername(String username) {
    //    this.username = username;
    //}

    /**
     * Gets the number of games played.
     * @return The number of games played.
     */
    public String getGames() {
        return games;
    }

    /**
     * Sets the number of games played.
     * @param games Number of games played.
     */
    public void setGames(String games) {
        this.games = games;
    }

    /**
     * Sets the number of games played.
     * @param games Number of games played.
     */
    public void setGames(Integer games) {
        this.games = games.toString();
    }

    /**
     * Gets the number of games won.
     * @return The number of games won.
     */
    public String getWon() {
        return won;
    }
    /**
     * Sets the number of games won.
     * @param won Number of games won.
     */
    public void setWon(String won) {
        this.won = won;
    }
    /**
     * Sets the number of games won.
     * @param won Number of games won.
     */
    public void setWon(Integer won) {
        this.won = won.toString();
    }
    /**
     * Gets the number of games lost.
     * @return The number of games lost.
     */
    public String getLost() {
        return lost;
    }
    /**
     * Sets the number of games lost.
     * @param lost Number of games lost.
     */
    public void setLost(String lost) {
        this.lost = lost;
    }
    /**
     * Sets the number of games lost.
     * @param lost Number of games lost.
     */
    public void setLost(Integer lost) {
        this.lost = lost.toString();
    }


    /**
     * Gets a statistic given a key matching one of the fields.
     * Does not get the username.
     * @param key The statistic being requested.
     * @return The statistic.
     */
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


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Username: ");
        sb.append(username);
        sb.append(", Games: ");
        sb.append(games);
        sb.append(", Won: ");
        sb.append(won);
        sb.append(", Lost: ");
        sb.append(lost);
        sb.append('\n');
        return sb.toString();
    }
}
