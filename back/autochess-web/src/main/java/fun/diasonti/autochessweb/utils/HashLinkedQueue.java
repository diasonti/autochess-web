package fun.diasonti.autochessweb.utils;

import java.util.*;

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

    public E peek(E t) {
        return set.stream().filter(e -> Objects.equals(e, t)).findFirst().orElse(null);
    }

    public boolean contains(E object) {
        return set.contains(object);
    }
}
