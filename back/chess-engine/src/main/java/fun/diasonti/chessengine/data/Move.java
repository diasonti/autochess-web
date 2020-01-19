package fun.diasonti.chessengine.data;

import java.util.Objects;

public class Move {

    public long from;
    public long to;

    public Move(long from, long to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;
        Move move = (Move) o;
        return from == move.from &&
                to == move.to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "Move{" +
                "from=" + Long.numberOfTrailingZeros(from) +
                ", to=" + Long.numberOfTrailingZeros(to) +
                '}';
    }
}
