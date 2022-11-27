import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.simple.JSONObject;

public final class ThreadManager 
{
    private static AtomicInteger NUMBER_OF_THREADS;
    private static AtomicBoolean ALL_COMMANDS_READ;

    private static ArrayList<UpdaterThread> updaterThreads;
    private static ArrayList<Queue<JSONObject>> threadCommandQueues;
    private static ConcurrentHashMap<String, Integer> activeUsersThreadIndex;
    private static ArrayList<AtomicBoolean> threadCompleteStatus;
    private static AtomicInteger completedTasks;

    private static ArrayList<ReadWriteLock> threadReadWriteLocks;

    private static ReceiverThread receiverThread;
    
    private static ThreadManager threadManager = null;

    private ThreadManager(String filepathString)
    {        
        NUMBER_OF_THREADS = new AtomicInteger();
        NUMBER_OF_THREADS.set(0);
        ALL_COMMANDS_READ = new AtomicBoolean();
        ALL_COMMANDS_READ.set(false);

        threadCompleteStatus = new ArrayList<AtomicBoolean>();

        updaterThreads = new ArrayList<UpdaterThread>();
        receiverThread = new ReceiverThread(filepathString);
        threadCommandQueues = new ArrayList<Queue<JSONObject>>();
        threadReadWriteLocks = new ArrayList<ReadWriteLock>();
        activeUsersThreadIndex = new ConcurrentHashMap<String, Integer>();

        completedTasks = new AtomicInteger();
        completedTasks.set(0);
    }

    public static void incrementTask()
    {
        System.out.println(completedTasks.incrementAndGet());
    }

    public static void printCompletedTasks()
    {
        System.out.println(completedTasks.get());
    }

    public static void receiverEndCycle()
    {
        ALL_COMMANDS_READ.set(true);
        while (!areAllThreadsComplete())
        {
            ;
        }
        System.out.println("All threads complete");
        printCompletedTasks();
    }

    public static boolean areAllThreadsComplete()
    {
        for (int i = 0; i < threadCompleteStatus.size(); i++)
        {
            if (threadCompleteStatus.get(i).get() == false)
            {
                return false;
            }
        }

        return true;
    }

    public static boolean areAllCommandsReceived()
    {
        return ALL_COMMANDS_READ.get();
    }

    public static void initialize(String filepathString)
    {
        threadManager = new ThreadManager(filepathString);
    }

    public static void start()
    {
        receiverThread.start();
    }

    public static boolean hasCommands(int threadId) throws InterruptedException
    {
        return threadCommandQueues.get(threadId).size() != 0;
    }

    public static JSONObject peekNextCommand(int threadId)
    {
        return threadCommandQueues.get(threadId).peek();
    }

    public static void completeCommand(int threadId)
    {
        threadCommandQueues.get(threadId).remove();
    }

    public static ThreadManager getThreadManager()
    {
        return threadManager;
    }

    public static void initializeThreadForUser(String username)
    {
        int currentIndex = NUMBER_OF_THREADS.get();
        activeUsersThreadIndex.put(username, currentIndex);
        updaterThreads.add(new UpdaterThread(currentIndex));
        threadCommandQueues.add(new LinkedList<JSONObject>());
        threadReadWriteLocks.add(new ReadWriteLock());
        NUMBER_OF_THREADS.incrementAndGet();
        threadCompleteStatus.add(new AtomicBoolean());
        updaterThreads.get(currentIndex).start();
    }

    public static void setThreadStatusComplete(int threadId)
    {
        threadCompleteStatus.get(threadId).set(true);
    }

    public static void addCommandToThread(JSONObject command)
    {
        int threadIndexOfUser = activeUsersThreadIndex.get((String) command.get("user"));
        threadCommandQueues.get(threadIndexOfUser).add(command);
    }

    public static int getThreadIndexOfUser(String username)
    {
        return activeUsersThreadIndex.get(username);
    }

    public static void tryAddNewUser(String username)
    {
        if (!isActiveUser(username))
        {
            Database database = Database.getDatabase();
            database.addUser(new User(username));
            initializeThreadForUser(username);
        }
    }

    private static boolean isActiveUser(String username)
    {
        return activeUsersThreadIndex.containsKey(username); 
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
}
