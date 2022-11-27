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
            int counter = 0;
            while (scanner.hasNextLine()) 
            {
                String line = scanner.nextLine();
                try
                {
                    JSONParser jsonParser = new JSONParser();  
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(line);  
                    assignCommand(jsonObject);
                    System.out.println(jsonObject);
                    counter++;
                    System.out.println(counter);
                } catch (ParseException | InterruptedException p) {
                    System.out.println("Parsing error.");
                    p.printStackTrace();
                }   
            }
            scanner.close();

            while (!ThreadManager.areAllThreadsFree()){;}
        } catch (FileNotFoundException | InterruptedException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    private static void assignCommand(JSONObject jsonObject) throws InterruptedException
    {
        while (true)
        {
            int availableThreadId = ThreadManager.getAvailableThreadId();
    
            if (availableThreadId == -1)
            {
                continue;
            } else {
                ThreadManager.lockWrite(availableThreadId);
                ThreadManager.setThreadCommand(availableThreadId, jsonObject);
                ThreadManager.setThreadBusy(availableThreadId);
                ThreadManager.unlockWrite(availableThreadId);
                return;
            }
        }
    }
}


