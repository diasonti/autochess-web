package fun.diasonti.autochessweb.data.pojo;

public class MovablePiece {

    private final int id;
    private final char piece;
    private int position;

    public MovablePiece(int id, char piece) {
        this.id = id;
        this.piece = piece;
    }

    public int getId() {
        return id;
    }

    public char getPiece() {
        return piece;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
