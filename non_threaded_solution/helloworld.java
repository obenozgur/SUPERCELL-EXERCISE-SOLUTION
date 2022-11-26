import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;
import java.util.Scanner; // Import the Scanner class to read text files


class HelloWorld
{
    public static void main(String args[])
    {
        String filePath = "./tests/tests/ex2/input1.txt";
        
        try 
        {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) 
            {
                String line = scanner.nextLine();
                try
                {
                    JSONParser jsonParser = new JSONParser();  
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(line);  
                    Command command = parseCommand(jsonObject);
                    command.process();
                } catch (ParseException p) {
                    System.out.println("Parsing error.");
                    p.printStackTrace();
                } catch (IOException i) {
                    System.out.println("I/O error.");
                    i.printStackTrace();
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    private static Command parseCommand(JSONObject jsonObject) throws IOException
    {
        String commandType = (String) jsonObject.get("type");
        switch (commandType)
        {
            case "make_friends":
                return (Command) new MakeFriendsCommand(jsonObject);
            case "del_friends":
                return (Command) new DelFriendsCommand(jsonObject);
            case "update":
                return (Command) new UpdateCommand(jsonObject);
            default:
                throw new IOException("Invalid command type.");
        }
    }
}