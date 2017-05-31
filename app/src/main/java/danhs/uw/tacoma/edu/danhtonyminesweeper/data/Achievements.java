package danhs.uw.tacoma.edu.danhtonyminesweeper.data;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Holds the details of an Achievement
 */
public class Achievements {
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String CONDITION = "condition";

    private String name;
    private String description;
    private String condition;
    private Map<String, Integer> conditionMap;

    /**
     * Constructor
     * @param name Name of Achievement.
     * @param description Description of achievement.
     * @param condition JSON object containing the conditions for the achievement to be earned.
     */
    public Achievements(String name, String description, String condition) {
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.conditionMap = new HashMap<String, Integer>();
        parseConditionJSON(condition, conditionMap);
    }
/**
 * Parses the json string, returns an error message if unsuccessful.
 * Returns course list if success.
 * @param achievementsJSON
 * @return reason or null if successful.
 */
    /**
     * Parses json string, returns an error message if unsuccessful.
     * @param achievementsJSON A JSON object containing a list of all achievements.
     * @param achievementsList A list that the elements of the json list are put into.
     * @return the error message.
     */
    public static String parseAchievementsJSON(String achievementsJSON, List<Achievements> achievementsList) {
        String reason = null;
        if (achievementsJSON != null) {
            try {
                JSONArray arr = new JSONArray(achievementsJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Achievements achievements = new Achievements(obj.getString(Achievements.NAME),
                            obj.getString(Achievements.DESCRIPTION),
                            obj.getString(Achievements.CONDITION));
                    achievementsList.add(achievements);
                }
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }

        }
        return reason;
    }

    /**
     * Parses a json string. Returns an error message if unsuccessful.
     * @param theCondition JSON object containing the conditions for the achievement to be earned.
     * @param theConditionMap A map for the conditions to be put into.
     * @return The error message.
     */
    public static String parseConditionJSON(String theCondition, Map<String, Integer> theConditionMap) {
        String reason = null;

        if (theCondition != null) {
            try {
                JSONObject object = new JSONObject(theCondition);
                Iterator<?> keys = object.keys();
                while(keys.hasNext()) {
                    String key = (String)keys.next();
                    Integer value = object.getInt(key);
                    theConditionMap.put(key, value);
                }


            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }

        }
        return reason;
    }


    /**
     * Gets the name field.
     * @return The name field.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description field.
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the condition field.
     * @return The condition field.
     */
    public String getCondition() { return condition; }

    /**
     * Gets the condition map field.
     * @return The condition map.
     */
    public Map<String, Integer> getConditionMap() {
        return conditionMap;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ");
        sb.append(name);
        sb.append(", Description: ");
        sb.append(description);
        sb.append(", Condition: ");
        sb.append(condition);
        sb.append('\n');
        return sb.toString();
    }
}
