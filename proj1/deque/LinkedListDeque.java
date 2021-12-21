package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>{

    public  class itemnode{
        public T value;
        public itemnode next;
        public itemnode prev;
        public itemnode(T x,itemnode n,itemnode p){
            value=x;
            next=n;
            prev=p;
        }
    }
    private itemnode sentinel;
    private int size;
    public LinkedListDeque(){
        size=0;
        sentinel=new itemnode(null,null,null);
        sentinel.prev=sentinel;
        sentinel.next=sentinel;

    }
    public class LLdequeIterator implements Iterator<T> {
        private int wizpos;
        public boolean hasNext(){
            return wizpos<size;
        }
        public T next(){
            T returnitem=get(wizpos);
            wizpos+=1;
            return returnitem;
        }
    }
    public Iterator<T> iterator(){
        return new LLdequeIterator();
    }
    public void addLast(T item){
        size+=1;
        itemnode p=sentinel.prev;//old last item
        sentinel.prev=new itemnode(item,sentinel,sentinel.prev);//new last item
        p.next=sentinel.prev;
    }
    public void addFirst(T item){
        size+=1;
        itemnode p=sentinel.next;
        sentinel.next=new itemnode(item,sentinel.next,sentinel);
        p.prev=sentinel.next;
    }

    public boolean isEmpty(){
        return size==0;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        for(T i:this){
            System.out.println(i);
        }

    }

    public T removeFirst(){
        itemnode item=sentinel.next;//old first
        if(item!=sentinel) {
            size -= 1;
            sentinel.next = item.next;//new first
            item.next.prev=sentinel;
        }
        return item.value;
    }
    public T removeLast(){
       itemnode item=sentinel.prev;
       if(item!=sentinel) {
           size -= 1;
           sentinel.prev = item.prev;
           item.prev.next=sentinel;
       }
        return item.value;
    }
    public T get(int index){
        itemnode p=sentinel.next;
        for(int i=0;i<size;i++){
            if(i==index)
                return p.value;
            p=p.next;
        }
        return null;
    }
    @Override
    public boolean equals(Object o){
        if(o==null)
            return false;
        if(this==o)
            return true;
        if(this.getClass()!=o.getClass())
            return false;
        LinkedListDeque<T> other=(LinkedListDeque<T>)o;
        if(this.size()!=other.size())
            return false;
        itemnode p=other.sentinel;
        for(T item :this){
            if(item!=p.next)
                return false;
            p=p.next;
        }
        return true;
    }
    public T getRecursive(int index){
        if(index==0)
            return
        return getRecursive(index--)
    }

}
