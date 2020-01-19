package fun.diasonti.chessengine.data;

public enum Color {
    WHITE, BLACK;

    public Color getOpposite() {
        if (this == WHITE)
            return BLACK;
        else
            return WHITE;
    }
}
