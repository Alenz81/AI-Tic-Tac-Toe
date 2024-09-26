import java.util.ArrayList;

public class Game {

    private int size = 3;
    Board board;
    boolean displayBoard;
    ArrayList<Player> players;
    public Game() {
        this(new ArrayList<Player>());
    }
    public Game(ArrayList<Player> players) {
        displayBoard = true;
        this.players = new ArrayList<>(players); this.board = new Board(size);
    }



    public void startGame() {
        if (!board.isEmpty()) {
            board.restart();
        }
        while (!board.checkWin() && !board.checkTie()) {
            for (Player p : players) {
                board.display();
                doTurn(p);
                if (board.checkWin(p.getSymbol()) || board.isFilled()) {
                    break;
                }
            }
        }
        board.display();
        if (board.checkTie()) {
            System.out.println("\nIts a tie!");
        }
        else {
            for (Player p : players) {
                if (p.getSymbol() == board.proclaimWinner()) {
                    System.out.println("\n" + p.getName() + " wins!");
                    break;
                }
            }
        }
    }
    public void doTurn(Player p) {
        board.makeMove(p.getMove(board), p.getSymbol());
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = new ArrayList<Player>(players);
        ArrayList<Character> symbols = new ArrayList<>();
        for (Player p : this.players) {
            symbols.add(p.getSymbol());
        }
        board.setSymbols(symbols);
    }

    public Board getBoard() {
        return this.board;
    }


}
