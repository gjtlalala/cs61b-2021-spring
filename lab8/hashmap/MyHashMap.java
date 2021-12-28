package hashmap;

import org.apache.commons.collections.BagUtils;
import speed.BucketsSpeedTest;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    private double loadfactor;
    private int initialSize;
    private int size;

    /** Constructors */
    public MyHashMap() {

       initialSize = 16;
        loadfactor = 0.75;
        size = 0;
        buckets = createTable(initialSize);
    }

    public MyHashMap(int initialSize) {

        this.initialSize = initialSize;
        loadfactor = 0.75;
        size = 0;
        buckets = createTable(initialSize);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {

        this.initialSize = initialSize;
        loadfactor = maxLoad;
        size = 0;
        buckets = createTable(initialSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {

        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        //return new Collection[size];
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    private class Myhashmapiterator<K>implements Iterator<K>{
        private int wizpos;
        HashSet<Node> hashSet;
        public Myhashmapiterator(){
            wizpos = 0;
            hashSet = new HashSet<>();
            for (int i = 0; i < initialSize; i++) {
                for(Node node:buckets[i]){
                    hashSet.add(node);
                }
            }
        }
        public boolean hasNext() {
            return wizpos < hashSet.size();
        }
        public K next() {
            for (Node node:hashSet) {
                wizpos += 1;
                return (K) node.key;
            }
            return null;
        }
    }
    @Override
    public Iterator<K> iterator() {
        return new Myhashmapiterator<K>();
    }
    @Override
    public void clear() {
        buckets = createTable(16);
        size = 0;
        initialSize = 16;
    }
    @Override
    public boolean containsKey(K key) {
        for(Node node:buckets[Math.floorMod(key.hashCode(),initialSize)]) {
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;

    }
    @Override
    public V get(K key) {
        for(Node node:buckets[Math.floorMod(key.hashCode(),initialSize)]) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public void put(K key, V value) {
        for (Node node:buckets[Math.floorMod(key.hashCode(),initialSize)]){
            if(node.key.equals(key)) {
                buckets[Math.floorMod(key.hashCode(), initialSize)].remove(node);
                size -= 1;
                break;
            }
        }
        Node newnode = createNode(key ,value);
        buckets[Math.floorMod(key.hashCode(),initialSize)].add(newnode);
        size += 1;
        if ((double)size/(double)initialSize > loadfactor) {
            resize(2* initialSize);
        }
    }
    private void resize(int tablesize) {
        Collection<Node>[] table  = buckets;
        buckets = createTable(tablesize);
        for (int i = 0; i < initialSize; i++) {
            for(Node node:table[i]){
                buckets[Math.floorMod(node.key.hashCode(),tablesize)].add(node);
            }
        }
        initialSize = tablesize;
    }
    @Override
    public Set<K> keySet() {
        HashSet<K> Set = new HashSet<>();
        for (int i = 0; i < initialSize; i++) {
            for(Node node:buckets[i]){
                Set.add(node.key);
            }
        }
        return Set;
    }
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
}
