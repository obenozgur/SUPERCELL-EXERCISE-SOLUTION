class Main
{
    public static void main(String args[])
    {
        Database.initialize();
        //TO DO file pathi argüman ile al
        String filePath = "./tests/tests/ex2/input1.txt";
        ThreadManager.initialize(filePath);
        ThreadManager.start();
    }
}