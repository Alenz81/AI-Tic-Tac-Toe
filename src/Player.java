
public abstract class Player {
    char symbol;
    String name;

    public abstract int getMove(Board board);
    public char getSymbol() {
        return symbol;
    }
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
