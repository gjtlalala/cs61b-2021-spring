package deque;


import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> MaxADcomparator;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        MaxADcomparator = c;
    }
    public T max() {
        T max = get(0);
        for (T i:this) {
            if (MaxADcomparator.compare(i, max) > 0) {
                max = i;
            }
        }
        return max;
    }
    public T max(Comparator<T> c) {
        T max = get(0);
        for (T i:this) {
            if (c.compare(i, max) > 0) {
                max = i;
            }
        }
        return max;
    }
}
