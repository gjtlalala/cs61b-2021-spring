package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>{
    private int size;
    private int nextfirst;
    private int nextlast;
    private T[] items;
    public ArrayDeque(){
        items=(T[])new Object[8];
        size=0;
        nextfirst=0;
        nextlast=1;
    }
    public void resize(int capcity){
        T[] newarray=(T[])new Object [capcity];
        if(nextlast>nextfirst) {
            for (int i = nextfirst + 1; i < size; i++) {
                newarray[i] = items[i];
            }
        }
        //System.arraycopy(items,0,newarray,0,size);
        items=newarray;
    }
    public void addFirst(T item){
        if (size==items.length)
            resize(2*size);
        size += 1;
        items[nextfirst]=item;
        nextfirst-=1;
        if(nextfirst<0)
            nextfirst=size-1;

    }

    public void addLast(T item){

        if (size==items.length)
            resize(2*size);
        size+=1;
        items[nextlast]=item;
        nextlast=(nextlast+1)%size;
    }
    public boolean isEmpty(){
        return size==0;
    }
    public int size(){
        return size;
    }
    public void printDeque(){

    }
    public T removeFirst(){
        if(items.length>4*size)
            resize(size*2);
        nextfirst=(nextfirst+1)%size;
        T returnitem =items[nextfirst];
        items[nextfirst]=null;
        return returnitem;
    }
    public T removeLast(){
        if(items.length>4*size)
            resize(size*2);
        nextlast-=1;
        if (nextlast<0)
            nextlast=size-1;
        T returnitem=items[nextfirst];
        items[nextlast]=null;
        return returnitem;
    }
    public T get(int index){
        return items[index];
    }
    public class ArraydequeIterator implements Iterator<T> {
        private int wizpos;
        public void ArraydequeIterator(){
            wizpos=0;
        }
        public boolean hasNext(){
            return wizpos<nextlast;
        }
        public T next(){
            T returnitem=get(wizpos);
            wizpos+=1;
            return returnitem;
        }
    }
    public Iterator<T> iterator(){
        return new ArraydequeIterator();
    }
    /*@Override
    public boolean equals(Object o){

    }*/
}
