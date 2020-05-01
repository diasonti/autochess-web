package fun.diasonti.autochessweb.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ListUtils {
    private ListUtils() {
    }

    public static  <T> List<T> sortingMerge(List<T> listOne, List<T> listTwo, Comparator<T> comparator) {
        final List<T> mergedList = new ArrayList<>(listOne.size() + listTwo.size());
        mergedList.addAll(listOne);
        mergedList.addAll(listTwo);
        mergedList.sort(comparator);
        return mergedList;
    }
}
