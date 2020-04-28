package fun.diasonti.chessengine.data;

import java.util.Objects;

public class Move {

    public final long from;
    public final long to;
    public int score;

    private Move(long from, long to) {
        this.from = from;
        this.to = to;
        this.score = 0;
    }

    public static Move fromIndex(int from, int to) {
        return of(1L << from, 1L << to);
    }

    public static Move of(long from, long to) {
        return new Move(from, to);
    }

    public static Move empty() {
        return new Move(0L, 0L);
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
