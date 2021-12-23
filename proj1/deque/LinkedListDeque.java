package deque;

import java.util.Iterator;
//import java.lang.Iterable;
public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    private  class Itemnode {
        private T value;
        private Itemnode next;
        private Itemnode prev;
        Itemnode(T x, Itemnode n, Itemnode p) {
            value = x;
            next = n;
            prev = p;
        }
    }
    private Itemnode sentinel;
    private int size;
    public LinkedListDeque() {
        size = 0;
        sentinel = new Itemnode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;

    }
    private class LLdequeIterator implements Iterator<T> {
        private int wizpos;
        LLdequeIterator() {
            wizpos = 0;
        }
        public boolean hasNext() {
            return wizpos < size;
        }
        public T next() {
            T returnitem = get(wizpos);
            wizpos += 1;
            return returnitem;
        }
    }
    public Iterator<T> iterator() {
        return new LLdequeIterator();
    }
    public void addLast(T item) {
        size += 1;
        Itemnode p = sentinel.prev; //old last item
        sentinel.prev = new Itemnode(item, sentinel, sentinel.prev); //new last item
        p.next = sentinel.prev;
    }
    public void addFirst(T item) {
        size += 1;
        Itemnode p = sentinel.next;
        sentinel.next = new Itemnode(item, sentinel.next, sentinel);
        p.prev = sentinel.next;
    }
/*
    public boolean isEmpty(){
        return size==0;
    }
*/
    public int size() {
        return size;
    }

    public void printDeque() {
        for (T i:this) {
            System.out.println(i);
        }

    }

    public T removeFirst() {
        Itemnode item = sentinel.next; //old first
        if (item != sentinel) {
            size -= 1;
            sentinel.next = item.next; //new first
            item.next.prev = sentinel;
        }
        return item.value;
    }
    public T removeLast() {
        Itemnode item = sentinel.prev;
       if (item != sentinel) {
           size -= 1;
           sentinel.prev = item.prev;
           item.prev.next = sentinel;
       }
        return item.value;
    }
    public T get(int index) {
        Itemnode p = sentinel.next;
        for (int i = 0; i < size; i++) {
            if (i == index) {
                return p.value;
            }
            p = p.next;
        }
        return null;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        /*if (this.getClass() != o.getClass()) {
            return false;
        }*/
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if (this.size() !=  other.size()) {
            return false;
        }
        /*Itemnode p = other.sentinel;
        for (T item :this) {
            if (item != p.next) {
                return false;
            }
            p = p.next;
        }*/
        for (int i = 0; i < this.size; i++) {
            if (!this.get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }

    public T getRecursive(int index) {
        Itemnode tmp = sentinel;
        if (index < 0 || index > size) {
            return null;
        }
        return getRecursivehelper(index, tmp);
    }
    private T getRecursivehelper(int index, Itemnode tmp) {
        if (index == 0) {
            return tmp.next.value;
        }
        tmp = tmp.next;
        index -= 1;
        return getRecursivehelper(index, tmp);
    }

}
