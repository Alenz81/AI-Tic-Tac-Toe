import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;


public class LearnAI extends Player {
    private static final ReentrantLock lock = new ReentrantLock();
    static ArrayList<char[]> losses;
    char[] currentMove;

    public LearnAI() {
        currentMove = new char[9];
        losses = new ArrayList<>();
        System.out.println("No File");
    }

    public LearnAI(char symbol, String filename) throws FileNotFoundException {
        currentMove = new char[9];
        losses = new ArrayList<>();
        setSymbol(symbol);
        importData(filename);
    }

    @Override
    public void setSymbol(char symbol) {
        super.setSymbol(symbol);
        super.setName("LearnAI " + symbol);
    }
    @Override
    public int getMove(Board board) {
        //ArrayList<Integer> moves = board.getValidMoves();
        ArrayList<Integer> legalMoves = findLegalMoves(board);

        System.out.println(legalMoves);
        if (legalMoves.isEmpty()) {
            /**
            ArrayList<char[]> ties = new ArrayList<>();
            ties = getTies();
            for (int m : moves) {
                if (ties.contains(simulateMove(board.getFlatBoard(), m))) {
                    currentMove = simulateMove(board.getFlatBoard(), m);
                    return m;
                }
            }
             **/
            int move = board.getValidMoves().getFirst();
            currentMove = simulateMove(board.getFlatBoard(), move);
            addGameToLosses(currentMove);
            return move;
        }
        else {
            int move = legalMoves.get((int)(Math.random() * legalMoves.size()));
            currentMove = simulateMove(board.getFlatBoard(), move);
            return move;
        }

    }

    public void importData(String filename) throws FileNotFoundException {
        lock.lock();
        try {
            try {
                Scanner in = new Scanner(new File(filename));
                String stringArray;
                while (in.hasNextLine()) {
                    stringArray = in.nextLine();
                    addGameToLosses(stringToArray(stringArray));
                }
                in.close();
                System.out.println("Imported File");
            } catch (Exception e) {
                System.out.println("File Not Found");
            }
        } finally {
            lock.unlock();
        }
    }

    public char[] stringToArray(String string) {
        String trueString = "";
        for (int i = 1; i < string.length() - 1; i += 3){ //gets rid of []
            trueString += string.charAt(i);
        }
        //System.out.println(trueString);
        return trueString.toCharArray();
    }

    public static void exportAllData() throws FileNotFoundException {
        lock.lock();
        try {
            System.out.println("EXPORTING");
            //PrintWriter out = new PrintWriter(new FileOutputStream(determineExportFileName(), true));
            PrintWriter out = new PrintWriter(determineExportFileName());
            for (char[] loss : losses) {
                out.append(Arrays.toString(boardToExportFormat(loss)) + "\n");
            }
            out.close();
        } finally {
            lock.unlock();
        }
    }

    public static String determineExportFileName() {
        lock.lock();
        try {
            int gridSize = (int) Math.sqrt(losses.getFirst().length);
            return (findPlayerSymbols(losses.getFirst()).size() + " Players, Grid Size " + gridSize + ".txt");
        } finally {
            lock.unlock();
        }
    }

    public void exportAllData(ArrayList<char[]> arrayList) throws FileNotFoundException {

        PrintWriter out = new PrintWriter(new FileOutputStream(determineExportFileName(), true));
        for (char[] loss : arrayList) {
            out.append(Arrays.toString(boardToExportFormat(loss)) + "\n");
        }
        out.close();
    }

     public static char[] boardToExportFormat(char[] board) {
             char[] formatted = new char[board.length];
             ArrayList<Character> players = findPlayerSymbols(board);
             int i = 0;
             for (char square : board) {
                 if (square == 'A') {
                     formatted[i++] = 'A';
                 }
                 else if (players.contains(square)) {
                     formatted[i++] = 'B';
                 }
                     else {
                     formatted[i++] = ' ';
                 }
             }
             return formatted;


    }



    public ArrayList<char[]> getLosses() {
        lock.lock();
        try {
            return losses;
        } finally {
            lock.unlock();
        }
    }

    public void addGameToLosses(char[] loss) {
        lock.lock();
        try {
            if (!Gameplay.arrListContainArray(losses, loss)) {
                losses.add(loss);
                System.out.println("ADDING GAME TO LOSS");
            }
        } finally {
            lock.unlock();
        }
    }

    public char[] getCurrentMove() {
        return currentMove;
    }

    public static ArrayList<Character> findPlayerSymbols(char[] board) {
            ArrayList<Character> symbols = new ArrayList<>();
            symbols.add('A');
            for (int i = 0; i < board.length; i++) {
                if (!symbols.contains(board[i]) && board[i] != ' ') {
                    symbols.add(board[i]);
                }
            }
            //System.out.println(symbols);

            return symbols;
    }




    public boolean checkAgainstData(char[] board) { //check this one, definetly wroong
        lock.lock();
        try {
            for (char[] data : losses) {
                if (data == board) {
                    return true;
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public void printStoredData() {
        lock.lock();
        try {
            for (int i = 0; i < losses.size(); i++) {
                System.out.println(Arrays.toString(losses.get(i)));
            }
        } finally {
            lock.unlock();
        }
    }

    /**
    public char[][] getDataBoard(Scanner in) {
        //char[][]
        while (in.hasNextLine()) {

        }
        return new char[][]{{1, 2}};
    }
     **/

    public ArrayList<Integer> findLegalMoves(Board board) {
        lock.lock();
        try {
            ArrayList<Integer> moves = board.getValidMoves();
            ArrayList<Integer> legal = new ArrayList<>();
            for (int move : moves) {
                char[] sim = simulateMove(board.getFlatBoard(), move);
                boolean isLegal = true;
                for (char[] loss : losses) {
                    if (Arrays.equals(sim, loss)) {

                        isLegal = false;
                        break;
                    }
                }
                if (isLegal) {
                    legal.add(move);
                }
            }
            return legal;
        } finally {
            lock.unlock();
        }
    }

    public char[] simulateMove(char[] board, int move) {
        char[] sim = board.clone();
        sim[move] = getSymbol();
        //System.out.println(Arrays.toString(sim));
        return sim;
    }

    public ArrayList<char[]> getTies() {
        lock.lock();
        try {
            ArrayList<char[]> ties = new ArrayList<>();
            for (char[] loss : losses) {
                if (Gameplay.checkTie(Gameplay.unflattenBoard(loss))) {
                    ties.add(loss);
                }
            }
            return ties;
        } finally {
            lock.unlock();
        }
    }



}
