package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    private class BSTNode {
        private K key;
        private V value;
        private BSTNode left;
        private BSTNode right;
        private int size;
        public BSTNode(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }
    }
    private BSTNode root;

    public int size() {
        return size(root);
    }
    private int size(BSTNode x) {
        if (x == null) return 0;
        else return x.size;
    }
    public V get(K key) {
        return find(root,key);
    }
    private V find(BSTNode T, K key){
        if (T == null) {
            return null;
        }
        if (key.equals(T.key)) {
            return T.value;
        }
        else if (key.compareTo(T.key) < 0) {
            return find(T.left, key);
        }
        else {
            return find(T.right, key);
        }
    }
    public void put(K key, V value) {
        root = insert(root, key, value);
    }
    private BSTNode insert(BSTNode T, K key, V value) {
        if (T == null) {
            return new BSTNode(key, value,1);
        }
        if (key.compareTo(T.key) < 0) {
            T.left = insert(T.left, key, value);
            T.size += 1;
        }
        else if (key.compareTo(T.key) > 0) {
            T.right = insert(T.right, key, value);
            T.size += 1;
        }
        return T;
    }
    public void clear() {
        root = null;
    }
    public boolean containsKey(K key) {
        return containskeyhelper(root, key);
    }
    private boolean containskeyhelper (BSTNode T, K key) {
        if (T == null) {
            return false;
        }
        if (key.equals(T.key)) {
            return true;
        }
        else if (key.compareTo(T.key) < 0) {
            return containskeyhelper(T.left, key);
        }
        else {
            return containskeyhelper(T.right, key);
        }
    }
    public Set<K> keySet() {
       throw new UnsupportedOperationException();
    }
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
    public void printInOrder() {
        printvalue(root);
        //printvalue(root.left);
        //printvalue(root.right);
    }
    private void printvalue(BSTNode T) {
        if (T != null) {
            System.out.println(T.value);
            printvalue(T.left);
            printvalue(T.right);
        }
    }
}
