/**
 * A MaxPQ implementation. 
 * @param <K> 
 *      the type of keys stored in the HashMap
 * @param <V>
 *      the values associated with the key     
 */ 
public class MaxPQ<K extends Comparable<? super K>, V> {
    /** Default size of the MinPQ is 16. */
    private static final int DEFAULT_CAPACITY = 16;
    
    /** Ranked array representation of MaxPQ. */
    private Entry<K, V> [] rankedArray;
    /** Number of items in the maxPQ. */
    private int currentSize;

    /** 
     * Default constructor for a MaxPQ.
     */
    public MaxPQ() {
        this(DEFAULT_CAPACITY);
    }
    
    
    /**
     * Constructor that takes the parameters below.
     * @param capacity
     *      initial capacity of the array
     */
    public MaxPQ(int capacity) {
        this.currentSize = 0;
        if (capacity < 0) {
            throw new IllegalArgumentException();
        } else {
            this.rankedArray = new Entry [capacity + 1];
        }
    }

    /**
     * Insert into the priority queue, maintaining heap order.
     * Duplicates are allowed.
     * @param x the item to insert.
     * @param v the value associated with the key
     */
    public void insert(K x, V v) {
        if (this.currentSize == this.rankedArray.length - 1) {
            this.enlargeArray(this.rankedArray.length * 2 - 1);
        }

        int hole = ++this.currentSize;
        Entry<K, V> newEnt = new Entry<K, V>(x, v);

        for (this.rankedArray[0] = newEnt; 
            newEnt.key.compareTo(this.rankedArray[hole / 2].key) > 0;
            hole /= 2) {
            this.rankedArray[hole] = this.rankedArray[hole / 2];
        }
        this.rankedArray[hole] = newEnt;
    }

    /** 
     * Increases array size. 
     * @param newSize new size of the array
     */
    private void enlargeArray(int newSize) {
        Entry[] old = this.rankedArray;
        this.rankedArray = new Entry[newSize];
        for (int i = 0; i < old.length; i++) {
            this.rankedArray[i] = old[i];
        }
    }

    /**
     * Returns the number of entries in the MaxPQ.
     * @return
     *      integer of the number of filled positions of the MaxPQ
     */
    public int size() {
        return this.currentSize;
    }

    /**
     * Find the largest key in the priority queue.
     * @return the largest key, or throw an UnderflowException if empty.
     */
    public K findMaxKey() {
        return this.getKeyAt(1);
    }

    /** 
     * Find the largest item in the MaxPQ based on key.
     * @return returns the value of it
     */
    public V findMaxValue() {
        return this.getValueAt(1);
    }

    /**
     * Find the key in the priority queue ranked array position.
     * @param index is the index in the PQ
     * @return the key at the inputed position, 
     *      or throw an UnderflowException if empty.
     */
    public K getKeyAt(int index) {
        if (this.isEmpty()) {
            throw new NullPointerException();
        }
        
        return this.rankedArray[index].key;     
    }

    /**
     * Find the value in the priority queue ranked array position.
     * @param index is the index in the PQ
     * @return the value at the inputed position, 
     *      or throw an UnderflowException if empty.
     */
    public V getValueAt(int index) {
        if (this.isEmpty()) {
            throw new NullPointerException();
        }
        
        return this.rankedArray[index].value;
    }
    
    /**
     * Remove the largest item from the priority queue.
     */
    public void deleteMax() {
        this.deletePos(1);
    }
    /**
     * Removes the item at the index from the priority queue.
     * Re-heapifies if necessary
     * @param index index to delete item
     */
    public void deletePos(int index) {
        if (this.isEmpty()) {
            throw new NullPointerException();
        }
        
        this.rankedArray[index] = this.rankedArray[this.currentSize--];
        this.percolateDown(index);
    }

    /**
     * Returns whether the heap is empty. 
     * @return true if there are no elements in the heap. 
     */
    public boolean isEmpty() {
        return this.currentSize == 0;
    }

    /** 
     * Makes the heap empty.
     */
    public void makeEmpty() {
        this.currentSize = 0;
    }

    /**
     * Internal method to percolate down in the heap.
     * @param hole the index at which the percolate begins.
     */
    private void percolateDown(int hole) {
        int child;
        Entry<K, V> tmp = this.rankedArray[hole];

        for ( ; hole * 2 <= this.currentSize; hole = child) {
            child = hole * 2;
            if (child != this.currentSize 
                && this.rankedArray[child + 1].key.compareTo(
                this.rankedArray[child].key) > 0) {
                child++;
            }

            if (this.rankedArray[child].key.compareTo(tmp.key) > 0) {
                this.rankedArray[hole] = this.rankedArray[child];
            } else {
                break;
            }
        }
        
        this.rankedArray[hole] = tmp;
    }
    
    /**
     * Private class for the elements of the minPQ.
     * @param <K>
     *      Type of key entered
     * @param <V>
     *      Type of value associated with the key
     */
    private class Entry<K, V> {
        /**Key to apply heap property.**/
        public K key;
        /**Value associated with the key. */
        public V value;

        /**
         * Default constructor for an Entry object. 
         * @param keyv key to sort MaxPQ by
         * @param valueV value associated with key
         */
        public Entry(K keyv, V valueV) {
            this.key = keyv;
            this.value = valueV;
        }
    }
}       
