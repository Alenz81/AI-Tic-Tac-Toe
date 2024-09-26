import java.io.FileNotFoundException;
import java.util.Timer;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Gameplay threads[] = new Gameplay[8];
        Gameplay gameplay = new Gameplay();
        //gameplay.defaultConfigs(5);
        /*
        0: Human Config
        1: Human & Random AI
        2: Random AI
        3: Random AI & Learning AI
        4: Learning AI
        5: Human Config & Random AI
        */

        long startTime = System.currentTimeMillis();

        Gameplay g = new Gameplay();
        g.trainAI(3, 2, 20000);





/*
        for (int i = 0; i < threads.length; i++){
            threads[i] = new Gameplay();

        }

        for (int i = 0; i < threads.length; i++){
            threads[i].start();

        }

        while (threads[0].isAlive() || threads[1].isAlive() || threads[2].isAlive() || threads[3].isAlive() || threads[4].isAlive() || threads[5].isAlive() || threads[6].isAlive() || threads[7].isAlive()){

        }



*/

        LearnAI.exportAllData();
        Gameplay.printOutStats();
/*
        long elapsedTime = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedTime / 1000;
        long secondsDisplay = elapsedSeconds % 60;
        long elapsedMinutes = elapsedSeconds / 60;

        System.out.println("\nSeconds: " + elapsedSeconds);
*/

/*
        LearnAI ai = new LearnAI();
        ai.setSymbol('X');
        ai.importData("importDataTest.txt");
        ai.printStoredData();
        ai.exportAllData();
        System.out.println(ai.determineExportFileName());
        */











    }
}