import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Gameplay extends Thread {

    Game game;
    ArrayList<Player> players;
    int boardSize;

    static ArrayList<char[]> totalLoses;
    static int wins = 0, ties = 0, loss = 0;

    public Gameplay() {
        players = new ArrayList<>();
        totalLoses = new ArrayList<>();
        boardSize = 0;
        game = new Game();

    }

    public void run() {
        try {
            trainAI(3, 2, 10000);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void defaultConfigs(int config) throws FileNotFoundException {
        if (config == 1){
            System.out.println("Human & Random AI Config (1): Human Player X, Random AI O");
            players.add(new HumanPlayer());
            players.add(new RandomAI());
        } else if (config == 2){
            System.out.println("Random AI Config (2): Random AI X, Random AI O");
            players.add(new RandomAI());
            players.add(new RandomAI());
        } else if (config == 3){
            System.out.println("Random AI & Learning AI Config (3): Random AI X, Learning AI O");
            players.add(new RandomAI());
            players.add(new LearnAI('A', "2 Players, Grid Size 3.txt"));
        } else if (config == 4) {
            System.out.println("Learning AI Config (4): Learning AI X, Learning AI O");
            players.add(new LearnAI('A', "2 Players, Grid Size 3.txt"));
            players.add(new LearnAI('A', "2 Players, Grid Size 3.txt"));
        } else if (config == 5){
            System.out.println("Learning AI Config (4): Human Player X, Learning AI O");
            players.add(new LearnAI('A', "2 Players, Grid Size 3.txt"));
            players.add(new HumanPlayer());
        } else {
            System.out.println("Human Config (0): Human Player X, Human Player O");
            players.add(new HumanPlayer());
            players.add(new HumanPlayer());
        }

        System.out.println("Board Size: 3x3");

        boardSize = 3;
        players.get(0).setSymbol('A');
        players.get(1).setSymbol('B');
        game.setPlayers(players);
        game.startGame();
    }

    public void startGame() {
        int interaction = chooseMethodOfInteraction();
        if (interaction == 0) {
            int playerCount = choosePlayerCountConsole();
            choosePlayerTypesAndSymbolsConsole(playerCount);
            chooseGameModeConsole(playerCount);
        }
        System.out.println("Done");
        game.setPlayers(players);
        game.startGame();
    }

    public void trainAI(int boardSize, int playerCount, int games) throws FileNotFoundException {
        this.boardSize = boardSize;
        LearnAI ai = new LearnAI('A', "2 Players, Grid Size 3.txt");
        players.add(ai);
        for (int i = 1; i < playerCount; i++){
            players.add(new RandomAI());
            players.get(i).setSymbol((char) (65 + i));
        }
        game.setPlayers(players);

        for (int i = 0; i < players.size(); i++){
            System.out.println(players.get(i).getSymbol());
        }



        for (int i = 0; i < games; i++) {
            game.startGame();
            if (game.getBoard().proclaimWinner() != ai.getSymbol() /*|| game.getBoard().checkTie()*/) {
                //ai.addGameToLosses(ai.getCurrentMove());
                if (game.getBoard().checkTie()) {
                    ties++;
                }
                else {
                    loss++;
                    //ai.addGameToLosses(ai.getCurrentMove());
                }
            }
            else {
                wins++;
            }

        }
        //ai.printStoredData();

        //addLossesToTotalLosses(ai.getLosses());
        //exportAllData(totalLoses);
        System.out.println(ai.getLosses().size());
    }

    public static void printOutStats(){
        System.out.println("Wins: " + wins);
        System.out.println("Ties: " + ties);
        System.out.println("Losses: " + loss);
        double totalGames = wins + ties + loss;
        System.out.println("\nTotal Games: " + totalGames);
        System.out.println("Win Rate " + wins/totalGames);
    }

    public void addLossesToTotalLosses(ArrayList<char[]> losses) {
        for (int i = 0; i < losses.size(); i++){
            if (!totalLoses.contains(losses.get(i))){
                totalLoses.add(losses.get(i));
            }
        }
    }

    public int chooseMethodOfInteraction(){
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("How would you like to play? (0 for Console)");
            choice = scanner.nextInt();
            if (choice != 0){
                System.out.println("Invalid");
            }
        } while (choice != 0);
        return choice;
    }


    public int choosePlayerCountConsole(){
        Scanner scanner = new Scanner(System.in);
        int playerCount;
        do {
            System.out.println("How many players?");
            playerCount = scanner.nextInt();
            if (!(playerCount >= 2)){
                System.out.println("Invalid");
            }
        } while (playerCount < 2);
        return playerCount;

    }

    public void choosePlayerTypesAndSymbolsConsole(int playerCount) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Character> symbols = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            System.out.println("Is this player Human (0), LearningAI (1), or RandomAI (2)");
            int playerType;
            do {
                playerType = scanner.nextInt();
                if (playerType == 0) {
                    players.add(new HumanPlayer());
                } else if (playerType == 1) {
                    players.add(new LearnAI());
                } else if (playerType == 2) {
                    players.add(new RandomAI());
                } else {
                    System.out.println("Invalid");
                }
            } while (!(playerType >= 0 && playerType <= 2));
            System.out.println("What is this player's symbol (enter a character not used before)");

            char currentSymbol;
            boolean isFound;
            do {
                currentSymbol = scanner.next().charAt(0);
                isFound = symbols.contains(currentSymbol);
                if (isFound){
                    System.out.println("Invalid");
                }
            } while (isFound);
            symbols.add(currentSymbol);
            players.get(i).setSymbol(currentSymbol);
        }

    }
    public void chooseGameModeConsole(int playerCount) { //split this into functions
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose one odd number to represent a square board larger than or equal to " + (playerCount * 2 - 1) + "x" + (playerCount * 2 - 1));
        do {
            boardSize = scanner.nextInt();
            if (boardSize < playerCount * 2 - 1 || boardSize % 2 == 0) {
                System.out.println("Invalid");
            }
        } while (boardSize < playerCount * 2 - 1 || boardSize % 2 == 0);
    }

    public static char[][] unflattenBoard(char[] board) {
        char[][] unflatten = new char[(int)Math.sqrt(board.length)][(int)Math.sqrt(board.length)];
        int i = 0;
        for (char row[] : unflatten) {
            for (char square : row) {
                square = board[i++];
            }
        }
        return unflatten;
    }

    public void exportAllData(ArrayList<char[]> arrayList) throws FileNotFoundException {
                PrintWriter out = new PrintWriter(new FileOutputStream(determineExportFileName(), true));
                for (char[] loss : arrayList) {
                    out.append(Arrays.toString(boardToExportFormat(loss)) + "\n");
                }
                out.close();
    }

    public String determineExportFileName() {
        int gridSize = (int) Math.sqrt(totalLoses.getFirst().length);
        return (findPlayerSymbols(totalLoses.getFirst()).size() + " Players, Grid Size " + gridSize + ".txt");
    }

    public ArrayList<Character> findPlayerSymbols(char[] board) {
        ArrayList<Character> symbols = new ArrayList<>();
        //symbols.add(getSymbol());
        for (int i = 0; i < board.length; i++) {
            if (!symbols.contains(board[i]) && board[i] != ' ') {
                symbols.add(board[i]);
            }
        }
        //System.out.println(symbols);

        return symbols;
    }

    public char[] boardToExportFormat(char[] board) {
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

    public static boolean checkWin(char player, char[][] board) {
        return checkDiagonalWin(player, board) || checkVerticalWin(player, board) || checkHorizontalWin(player, board);
    }

    private static boolean checkDiagonalWin(char player, char[][] board) {
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) || (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    private static boolean checkHorizontalWin(char player, char[][] board) {
        for (char[] row : board) {
            if (row[0] == player && row[1] == player && row[2] == player) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkVerticalWin(char player, char[][] board) {
        for (int i = 0; i < 3; ++i) {
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkTie(char[][] board) {
        ArrayList<Character> players = getPlayerSymbols(board);
        for (Character p : players) {
            if (checkWin(p, board)) {
                return false;
            }
        }
        for (char[] row : board) {
            for (char square : row) {
                if (square == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public static ArrayList<Character> getPlayerSymbols(char[][] board) {
        ArrayList<Character> symbols = new ArrayList<>();
        for (char[] row : board) {
            for (char square : row) {
                if (!symbols.contains(square)) {
                    symbols.add(square);
                }
            }
        }
        return symbols;
    }

    public static boolean arrListContainArray(ArrayList<char[]> list, char[] element) {
        for (char[] l : list) {
            if (Arrays.equals(l, element))
                return true;
        }
        return false;
    }
}
