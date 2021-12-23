package deque;

import java.util.Iterator;
//import java.lang.Iterable;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private int nextfirst;
    private int nextlast;
    private T[] items;
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextfirst = 0;
        nextlast = 1;
    }
    /*public ArrayDeque(int capacity){
        items=(T[])new Object[capacity];
        size=0;
        nextfirst=0;
        nextlast=1;
    }*/
    private int minusOne(int index) {
        index -= 1;
        if (index < 0) {
            index = items.length - 1;
        }
        return index;
    }
    private void resize(int capcity) {
        T[] newarray = (T[]) new Object [capcity];
        /*if(nextlast>nextfirst) {
            for (int i = nextfirst + 1; i < size; i++) {
                newarray[i] = items[i];
            }
        }*/
        //System.arraycopy(items,0,newarray,0,size);

        for (int i = 0; i < size; i++) {
            //nextfirst=(nextfirst+1)%items.length;
            //newarray[i]=get(nextfirst);
            newarray[i] = get(i);
        }
        nextfirst = capcity - 1;
        nextlast = size;
        items = newarray;
    }
    public void addFirst(T item) {
        if (size == items.length) {
            resize(2 * size);
        }
        size += 1;
        items[nextfirst] = item;
        nextfirst = minusOne(nextfirst);

    }

    public void addLast(T item) {

        if (size == items.length) {
            resize(2 * size);
        }
        size += 1;
        items[nextlast] = item;
        nextlast = (nextlast + 1) % (items.length);
    }
    /*public boolean isEmpty(){
        return size==0;
    }*/
    public int size() {
        return size;
    }

    public T removeFirst() {
        if (items.length > 4 * size && size > 4) {
            resize(size * 2);
        }
        int first = (nextfirst + 1) % (items.length);
        T returnitem = items[first];
        if (returnitem != null) {
            nextfirst = first;
            items[first] = null;
            size -= 1;
        }
        return returnitem;
    }
    public T removeLast() {
        if (items.length > 4 * size && size > 4) {
            resize(size * 2);
        }
        int last = minusOne(nextlast);
        T returnitem = items[last];
        if (returnitem != null) {
            nextlast = last;
            items[last] = null;
            size -= 1;
        }
        return returnitem;
    }

    public T get(int index) {
        return items[(nextfirst + index + 1) % items.length];
    }
    private class ArraydequeIterator implements Iterator<T> {
        private int wizpos;
        private int num;
        ArraydequeIterator() {
            wizpos = (nextfirst + 1) % (items.length);
            num = 0;
        }
        public boolean hasNext() {
            return num < size;
        }
        public T next() {
            T returnitem = get(wizpos);
            wizpos = (wizpos + 1) % items.length;
            num += 1;
            return returnitem;
        }
    }
    public Iterator<T> iterator() {
        return new ArraydequeIterator();
    }
    public void printDeque() {
        for (T i:this) {
            System.out.println(i);
        }
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        /*if (o.getClass() != this.getClass()) {
            return false;
        }*/
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<?> other = (Deque<?>)  o;
        if (other.size() != this.size()) {
            return false;
        }
        for (int i = 0; i < this.size; i++) {
            if (!this.get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }
}
