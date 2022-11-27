import java.io.IOException;

import org.json.simple.JSONObject;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

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
        System.out.println("thread id: " + threadId);
        try {
            while (true)
            {
                ThreadManager.lockRead(threadId);
                if (ThreadManager.peekNextCommand(threadId) == null)
                {
                    ThreadManager.unlockRead(threadId);
                    if (ThreadManager.areAllCommandsReceived())
                    {
                        System.out.println(threadId + " has no task.");
                        ThreadManager.setThreadStatusComplete(threadId);
                        break;
                    }
                    continue;
                }

                
                try {

                    JSONObject commandJsonObject = ThreadManager.peekNextCommand(threadId);

                    ThreadManager.unlockRead(threadId);

                    Command command = parseCommand(commandJsonObject);
                    command.process();

                    ThreadManager.lockWrite(threadId);
                    ThreadManager.completeCommand(threadId);
                    ThreadManager.unlockWrite(threadId);


                    ThreadManager.incrementTask();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }    
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private static Command parseCommand(JSONObject jsonObject) throws Exception
    {
        try {
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
                    //System.out.println("DEFAULT");
                    return (Command) new UpdateCommand(jsonObject);
                    //throw new IOException("Invalid command type.");
            }
        } catch (Exception e) {
            //System.out.println(e);
            //System.out.println(jsonObject);
            // TODO: handle exception
        }
        return null;
    }
}
