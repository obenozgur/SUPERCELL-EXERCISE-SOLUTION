import org.json.simple.JSONObject;

public class DelFriendsCommand implements Command 
{
    private String username1;
    private String username2;

    public DelFriendsCommand(JSONObject jsonObject)
    {
        username1 = (String) jsonObject.get("user1");
        username2 = (String) jsonObject.get("user2");
    }

    @Override
    public void process() 
    {
        Database database = Database.getDatabase();

        database.deleteFriends(username1, username2);
    }
    
}
