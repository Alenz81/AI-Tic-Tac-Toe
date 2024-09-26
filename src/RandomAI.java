import java.util.ArrayList;
public class RandomAI extends Player {

    @Override
    public void setSymbol(char symbol) {
        super.setSymbol(symbol);
        super.setName("RandomAI " + symbol);
    }
    @Override
    public int getMove(Board board) {
        ArrayList<Integer> moves = new ArrayList<Integer>(board.getValidMoves());
        return moves.get((int)(Math.random() * moves.size()));
    }
}
