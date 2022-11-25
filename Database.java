import java.util.LinkedHashMap;

public final class Database 
{
    private static LinkedHashMap<String, User> users;
    private static Database database = null;
    
    private Database(){};    
    
    public static Database getDatabase()
    {
        if (database == null)
        {
            return new Database();
        }
        else
        {
            return database;
        }
    }

    public static void addUser(User user)
    {
        String username = user.getUsername();
        users.put(username, user);
    }

    public static boolean isNewUser(String username)
    {
        return !users.containsKey(username);
    }

    public static void makeFriends(String username1, String username2)
    {
        User user1 = users.get(username1);
        User user2 = users.get(username2);

        user1.addFriend(username2);
        user2.addFriend(username1);

        users.replace(username1, user1);
        users.replace(username2, user2);
    }

    public static void deleteFriends(String username1, String username2)
    {
        User user1 = users.get(username1);
        User user2 = users.get(username2);

        user1.deleteFriend(username2);
        user2.deleteFriend(username1);

        users.replace(username1, user1);
        users.replace(username2, user2);
    }
}
