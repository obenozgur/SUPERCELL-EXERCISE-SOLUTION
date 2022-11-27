import java.io.IOException;

import org.json.simple.JSONObject;

public class UpdaterThread extends Thread
{
    private int threadId;

    public UpdaterThread(int threadId)
    {
       this.threadId = threadId;
    }

    public void setThreadId(int threadId)
    {
        this.threadId = threadId;
    }

    @Override
    public void run()
    {
        while(true)
        {
            try {
                ThreadManager.lockRead(threadId);
    
                while(!ThreadManager.isThreadBusy(threadId))
                {
                    ThreadManager.unlockRead(threadId);
                    ThreadManager.lockRead(threadId);
                }
    
                JSONObject commandJsonObject = ThreadManager.getThreadCommand(threadId);
                ThreadManager.unlockRead(threadId);
    
                Command command = parseCommand(commandJsonObject);
                command.process();
    
                ThreadManager.lockWrite(threadId);
                ThreadManager.setThreadFree(threadId);
                ThreadManager.unlockWrite(threadId);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
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
