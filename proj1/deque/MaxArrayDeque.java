package deque;


import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> maxADcomparator;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        maxADcomparator = c;
    }
    public T max() {
        /*T max = get(0);
        for (T i:this) {
            if (maxADcomparator.compare(i, max) > 0) {
                max = i;
            }
        }
        return max;*/
        return max(maxADcomparator);
    }
    public T max(Comparator<T> c) {
        if (size() == 0) {
            return null;
        }
        T max = get(0);
        for (T i:this) {
            if (c.compare(i, max) > 0) {
                max = i;
            }
        }
        return max;
    }
}
