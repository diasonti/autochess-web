package fun.diasonti.chessengine.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Move {

    private static final Map<Long, Map<Long, Move>> MOVE_CACHE = new HashMap<>();

    public final long from;
    public final long to;

    private Move(long from, long to) {
        this.from = from;
        this.to = to;
    }

    public static Move of(long from, long to) {
        return MOVE_CACHE.computeIfAbsent(from, key -> new HashMap<>()).computeIfAbsent(to, key -> new Move(from, to));
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
