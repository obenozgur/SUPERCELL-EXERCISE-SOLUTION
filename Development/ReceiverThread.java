import org.json.simple.parser.*;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import org.json.simple.JSONObject;

public class ReceiverThread extends Thread
{
    private String filepathString;

    public ReceiverThread(String filepathString)
    {
        this.filepathString = filepathString;
    }

    @Override
    public void run()
    {
        try 
        {
            File file = new File(filepathString);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) 
            {
                String line = scanner.nextLine();
                try
                {
                    JSONParser jsonParser = new JSONParser();  
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(line);
                    if (jsonObject == null)
                    {
                        System.out.println("NULL GELDİ");
                    }

                    String username = (String) jsonObject.get("user");
                    ThreadManager.tryAddNewUser(username);

                    ThreadManager.lockWrite(ThreadManager.getThreadIndexOfUser(username));
                    ThreadManager.addCommandToThread(jsonObject);
                    ThreadManager.unlockWrite(ThreadManager.getThreadIndexOfUser(username));
                } catch (Exception e) {
                    System.out.println("Parsing error.");
                    e.printStackTrace();
                }   
            }
            scanner.close();
            ThreadManager.receiverEndCycle();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }
}


