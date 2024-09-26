import java.util.Scanner;
public class HumanPlayer extends Player {
    //call getName function in the default constructor so it gets done automatically

    HumanPlayer() {
        setName(getName());
    }
    @Override
    public int getMove(Board board) {
        int move[] = getMove();
        while (!board.getValidMoves().contains(board.flattenMoveLocation(move[0], move[1]))) {
            System.out.println("Invalid move!");
            move = getMove();
        }
        return board.flattenMoveLocation(move[0], move[1]);
    }
    public String getName() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter name: ");
        return in.next();
    }
    public int[] getMove() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter row: ");
        int row = in.nextInt();
        System.out.print("Enter column ");
        int col = in.nextInt();
        return new int[]{row, col};
    }
}
