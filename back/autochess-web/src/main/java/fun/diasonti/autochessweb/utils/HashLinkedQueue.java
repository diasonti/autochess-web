package fun.diasonti.autochessweb.utils;

import java.util.*;
import java.util.function.Predicate;

public class HashLinkedQueue<E> {

    private final Set<E> set = new HashSet<>();
    private final Queue<E> queue = new LinkedList<>();

    public int size() {
        return queue.size();
    }

    public boolean add(E t) {
        if (!set.contains(t)) {
            set.add(t);
            queue.add(t);
        }
        return true;
    }

    public E remove() {
        final E element = queue.remove();
        set.remove(element);
        return element;
    }

    public boolean remove(E t) {
        if (set.contains(t)) {
            set.remove(t);
            queue.remove(t);
        }
        return true;
    }

    public boolean removeIf(Predicate<E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = queue.iterator();
        while (each.hasNext()) {
            final E element = each.next();
            if (filter.test(element)) {
                each.remove();
                set.remove(element);
                removed = true;
            }
        }
        return removed;
    }
}
