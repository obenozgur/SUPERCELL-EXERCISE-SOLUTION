import java.util.LinkedHashSet;
import java.util.LinkedHashMap;

import org.json.simple.JSONObject;

public class UpdateCommand implements Command 
{
    private String username;
    private Integer timestamp;
    private LinkedHashMap<String, String> values;


    public UpdateCommand(JSONObject jsonObject)
    {
        username = (String) jsonObject.get("user");
        timestamp = (Integer) jsonObject.get("timestamp");
        values = (LinkedHashMap<String, String>) jsonObject.get("values");
    }

    @Override
    public void process()
    {
        Database database = Database.getDatabase();

        if (database.isNewUser(username))
        {
            User user = new User(username, timestamp, values);
            database.addUser(user);
        } else {
            User user = database.getUser(username);
            LinkedHashMap<String, String> updatedValues = user.updateValues(timestamp, values);
            tryBroadcast(user, updatedValues);
            database.updateUser(user);
        }
    }

    private void tryBroadcast(User user, LinkedHashMap<String, String> updatedValues)
    {
        if (!user.hasFriends() || updatedValues.size() == 0)
        {
            return;
        } 
            
        LinkedHashSet<String> friends = user.getFriends();

        JSONObject broadcast = new JSONObject();

        broadcast.put("broadcast", friends);
        broadcast.put("user", user.getUsername());
        broadcast.put("timestamp", timestamp);
        broadcast.put("values", updatedValues);

        String broadcastMessage = broadcast.toString();
        System.out.println(broadcastMessage);
    }
}
