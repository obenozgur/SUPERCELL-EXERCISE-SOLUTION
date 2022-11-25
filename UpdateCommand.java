import org.json.simple.JSONObject;

public class UpdateCommand implements Command 
{
    private String username;
    private Integer timestamp;


    public UpdateCommand(JSONObject jsonObject)
    {
        username = (String) jsonObject.get("user");
        timestamp = (Integer) jsonObject.get("timestamp");
        //TODO convert values Java data
    }

    @Override
    public void process()
    {
        if (Database.isNewUser(username))
        {
            //TODO add user to database
        }
    }
}
