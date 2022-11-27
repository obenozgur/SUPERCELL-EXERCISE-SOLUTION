import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONObject;

public final class Database 
{
    private static ConcurrentHashMap<String, User> users;
    private static Database database = null;

    
    private Database()
    {
        users = new ConcurrentHashMap<String, User>();
    }

    public static void initialize()
    {
        database = new Database();
    }
    
    public static Database getDatabase()
    {
        return database;
    }

    public static void printAllUsers()
    {
        JSONObject userJsonObject = new JSONObject();

        for (Map.Entry<String, User> set: users.entrySet())
        {
            String key = set.getKey();
            User user = set.getValue();
            
            String username = user.getUsername();
            LinkedHashMap<String, String> values = user.getValues();

            userJsonObject.put(username, values);
        }

        String userString = userJsonObject.toString();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.xt"));
            writer.write(userString);
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public User getUser(String username)
    {
        return users.get(username);
    }

    public void addUser(User user)
    {
        String username = user.getUsername();
        users.put(username, user);
    }

    public void updateUser(User user)
    {
        users.replace(user.getUsername(), user);
    }

    public boolean isNewUser(String username)
    {
        return !users.containsKey(username);
    }


    public void makeFriends(String username1, String username2)
    {
        User user1 = users.get(username1);
        User user2 = users.get(username2);

        user1.addFriend(username2);
        user2.addFriend(username1);

        users.replace(username1, user1);
        users.replace(username2, user2);
    }

    public void deleteFriends(String username1, String username2)
    {
        User user1 = users.get(username1);
        User user2 = users.get(username2);

        user1.deleteFriend(username2);
        user2.deleteFriend(username1);

        users.replace(username1, user1);
        users.replace(username2, user2);
    }
}
