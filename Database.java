import java.util.LinkedHashMap;

public final class Database 
{
    private static LinkedHashMap<String, User> users;
    private static Database database = null;

    
    private Database()
    {
        users = new LinkedHashMap<String, User>();
    }

    public static void initialize()
    {
        database = new Database();
    }
    
    public static Database getDatabase()
    {
        return database;
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

    public void startUpdate(String username)
    {
        User user = users.get(username);
        user.startUpdate();
        updateUser(user);
    }

    public void stopUpdate(String username)
    {
        User user = users.get(username);
        user.stopUpdate();
        updateUser(user);
    }

    public boolean isNewUser(String username)
    {
        return !users.containsKey(username);
    }

    public boolean isUserOnUpdate(String username)
    {
        return users.get(username).isOnUpdate();
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
