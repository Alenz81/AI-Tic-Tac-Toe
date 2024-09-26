import java.util.ArrayList;

public class Board { // get rid of nulls and replace with space
    char board[][];
    ArrayList<Character> players;
    private int size;

    public Board() {
        this(3);
    }
    public Board(int size) {
        this.size = size;
        board = new char[size][size];
        restart();
    }
    public Board(char board[][]) {
        this.board = board;
        size = this.board[0].length;
    }
    
    public boolean checkWin(char player) {
        return checkWin(player, this.board);
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

    public boolean checkWin() {
        for (char p : players) {
            if (checkWin(p)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkWin(char player, char[][] board) {
        return checkDiagonalWin(player, board) || checkVerticalWin(player, board) || checkHorizontalWin(player, board);
    }

    public boolean checkTie(char[][] board) {
        return false;
    }
    private boolean checkDiagonalWin(char player, char[][] board) {
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) || (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    private boolean checkHorizontalWin(char player, char[][] board) {
        for (char[] row : board) {
            if (row[0] == player && row[1] == player && row[2] == player) {
                return true;
            }
        }
        return false;
    }

    private boolean checkVerticalWin(char player, char[][] board) {
        for (int i = 0; i < 3; ++i) {
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }
        return false;
    }
    
    public char[][] getBoard() {
        return board;
    }
    
    public char[] getFlatBoard() {
        char flat[] = new char[size*size];
        int i = 0;
        for (char row[] : board) {
            for (char square : row) {
                flat[i++] = square;
            }
        }
        return flat;
    }


    public void makeMove(int row, int column, char player) {
        board[row][column] = player;
    }

    public void makeMove(int location, char player) {
        int[] coordinates = unflattenMoveLocation(location);
        makeMove(coordinates[0], coordinates[1], player);
    }

    public int flattenMoveLocation(int row, int column) {
        return row * size + column;
    }
    public int[] unflattenMoveLocation(int location) {
        return new int[]{location / size, location % size};
    }

    public boolean checkTie() {
        if (!isFilled()) {
            return false;
        }
        for (char player : players) {
            if (checkWin(player)) {
                return false;
            }
        }
        return true;
    }

    public boolean isFilled() {
        return getValidMoves().isEmpty();
    }

    public ArrayList<Integer> getValidMoves() {
        ArrayList<Integer> moves = new ArrayList<Integer>();
        char current[] = getFlatBoard();
        for (int i = 0; i < current.length; ++i) {
            if (current[i] == ' ') {
                moves.add(i);
            }
        }
        return moves;
    }

    public void display() {
        int r = 0;
        System.out.print("  ");
        for (int i = 0; i < size; ++i) {
            System.out.print(i + " ");
        }

        System.out.println();

        for (char row[] : board) {
            System.out.print(r++ + " ");
            for (char square : row) {
                System.out.print(square + " ");
            }
            System.out.println();
        }
    }

    public void setSymbols(ArrayList<Character> symbols) {
        this.players = new ArrayList<Character>(symbols);
    }

    public char proclaimWinner() {
        for (char p : players) {
            if (checkWin(p)) {
                return p;
            }
        }
        return ' ';
    }

    public void restart() {
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                board[i][j] = ' ';
            }
        }
    }

    public boolean isEmpty() {
        for (char row[] : board) {
            for (char square : row) {
                if (square != ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}
