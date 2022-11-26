import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;

import org.json.simple.JSONObject;

public class UpdateCommand implements Command 
{
    private String username;
    private Long timestamp;
    private LinkedHashMap<String, String> values;


    public UpdateCommand(JSONObject jsonObject)
    {
        username = (String) jsonObject.get("user");
        timestamp = (Long) jsonObject.get("timestamp");
        values = new LinkedHashMap<String, String>();

        Map<String, String> values = ((Map<String, String>) jsonObject.get("values"));
        for (Map.Entry<String, String> set: values.entrySet())
        {
            String key = set.getKey();
            String value = set.getValue();
            this.values.put(key, value);
        }
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
        List<String> friendsList = new ArrayList<String>(friends);

        broadcast.put("broadcast", friendsList);
        broadcast.put("user", user.getUsername());
        broadcast.put("timestamp", timestamp);
        broadcast.put("values", updatedValues);

        String broadcastMessage = broadcast.toString();
        System.out.println(broadcastMessage);
    }
}
