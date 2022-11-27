import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

public final class ThreadManager 
{
    private static int NUMBER_OF_THREADS = 5;

    private static boolean[] threadStatusArray = new boolean[NUMBER_OF_THREADS];
    private static JSONObject[] threadCommandArray = new JSONObject[NUMBER_OF_THREADS];
    private static ArrayList<ReadWriteLock> threadReadWriteLocks;

    private static ArrayList<UpdaterThread> updaterThreads;
    private static ReceiverThread receiverThread;
    
    private static ThreadManager threadManager = null;

    private ThreadManager(String filepathString)
    {        
        updaterThreads = new ArrayList<UpdaterThread>();
        threadReadWriteLocks = new ArrayList<ReadWriteLock>();

        for (int i = 0; i < NUMBER_OF_THREADS; i++)
        {
            threadStatusArray[i] = false;
            threadCommandArray[i] = new JSONObject();
            updaterThreads.add(new UpdaterThread(i));
            threadReadWriteLocks.add(new ReadWriteLock());
        }
        
        receiverThread = new ReceiverThread(filepathString);
    }

    public static void initialize(String filepathString)
    {
        threadManager = new ThreadManager(filepathString);
    }

    public static void start()
    {
        try {
            for (int i = 0; i < NUMBER_OF_THREADS; i++)
            {
                updaterThreads.get(i).start();
                updaterThreads.get(i).join();
            }
            receiverThread.start();
            //receiverThread.join();
        } catch (InterruptedException e) {
            // TODO: handle exception
        }
    }

    public static ThreadManager getThreadManager()
    {
        return threadManager;
    }

    public static JSONObject getThreadCommand(int threadId)
    {
        return threadCommandArray[threadId];
    }

    public static int getAvailableThreadId() throws InterruptedException
    {
        for (int i = 0; i < NUMBER_OF_THREADS; i++)
        {
            lockRead(i);
            if (threadStatusArray[i] == false)
            {
                unlockRead(i);
                return i;
            }
            unlockRead(i);
        }

        return -1;
    }

    public static void setThreadBusy(int threadId)
    {
        threadStatusArray[threadId] = true;
    }

    public static void setThreadFree(int threadId)  
    {
        threadStatusArray[threadId] = false;
    }

    public static void setThreadCommand(int threadId, JSONObject command)  
    {
        threadCommandArray[threadId] = command;
    }

    public static void lockRead(int threadId) throws InterruptedException
    {
        threadReadWriteLocks.get(threadId).lockRead();
    }

    public static void unlockRead(int threadId) throws InterruptedException
    {
        threadReadWriteLocks.get(threadId).unlockRead();
    }

    public static void lockWrite(int threadId) throws InterruptedException
    {
        threadReadWriteLocks.get(threadId).lockWrite();
    }

    public static void unlockWrite(int threadId) throws InterruptedException
    {
        threadReadWriteLocks.get(threadId).unlockWrite();
    }
  
    public static boolean isThreadBusy(int threadId)
    {
        return threadStatusArray[threadId];
    }

    public static boolean areAllThreadsFree() throws InterruptedException
    {
        for (int i = 0; i < NUMBER_OF_THREADS; i++)
        {
            lockRead(i);
            if (isThreadBusy(i))
            {
                unlockRead(i);
                return false;
            }
            unlockRead(i);
        }

        return true;
    }    
}
