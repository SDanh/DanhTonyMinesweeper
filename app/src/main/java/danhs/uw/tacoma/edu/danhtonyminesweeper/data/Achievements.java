package danhs.uw.tacoma.edu.danhtonyminesweeper.data;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Achievements {
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String CONDITION = "condition";

    private String name;
    private String description;
    private String condition;
    private Map<String, Integer> conditionMap;

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
     *
     * @param theCondition A JSON object.
     * @return
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



    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCondition() { return condition; }

    public Map<String, Integer> getConditionMap() {
        return conditionMap;
    }
}
