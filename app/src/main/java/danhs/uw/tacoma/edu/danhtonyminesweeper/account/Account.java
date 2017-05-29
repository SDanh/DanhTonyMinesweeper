package danhs.uw.tacoma.edu.danhtonyminesweeper.account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;


public class Account implements Serializable {
    private String username;
    private String password;

    public static final String USERNAME = "username", PASSWORD = "password";

    public Account(String username, String password){
        setUsername(username);
        setPassword(password);

    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns course list if success.
     * @param accountJSON
     * @return reason or null if successful.
     */
    public static String parseAccountJSON(String accountJSON, List<Account> accountList) {
        String reason = null;
        if (accountJSON != null) {
            try {
                JSONArray arr = new JSONArray(accountJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Account course = new Account(obj.getString(danhs.uw.tacoma.edu.danhtonyminesweeper.account.Account.USERNAME), obj.getString(danhs.uw.tacoma.edu.danhtonyminesweeper.account.Account.PASSWORD));
                    accountList.add(course);
                }
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }

        }
        return reason;
    }

    public String getUsername(){
        return this.username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = password;
    }

}
