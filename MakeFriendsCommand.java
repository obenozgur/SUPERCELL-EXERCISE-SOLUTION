import javax.xml.crypto.Data;

import org.json.simple.JSONObject;

public class MakeFriendsCommand implements Command 
{
    private String username1;
    private String username2;

    public MakeFriendsCommand(JSONObject jsonObject)
    {
        username1 = (String) jsonObject.get("user1");
        username2 = (String) jsonObject.get("user2");
    }

    @Override
    public void process()
    {
        if (Database.isNewUser(username1))
        {
            User user1 = new User(username1);
            Database.addUser(user1);
        }

        if (Database.isNewUser(username2))
        {
            User user2 = new User(username2);
            Database.addUser(user2);
        }

        Database.makeFriends(username1, username2);
    }
}
