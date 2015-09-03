/**
 * A MinPQ implementation. 
 * Modified code based on Mark Allen Weiss' implementation
 * @param <K> 
 *      the type of keys stored in the HashMap
 * @param <V>
 *      the values associated with the key     
 */ 
public class MinPQ<K extends Comparable<? super K>, V> {
    /** Default size of the MinPQ is 16. */
    private static final int DEFAULT_CAPACITY = 16;
    
    /** Ranked array representation of MinPQ. */
    private Entry<K, V>[] rankedArray;
    /** Number of items in the minPQ. */
    private int currentSize;
       
    /** 
     * Default constructor for a MinPQ.
     */
    public MinPQ() {
       this(DEFAULT_CAPACITY);
    }
        
    /**
     * Constructor that takes the parameters below.
     * @param capacity
     *      initial capacity of the array
     */
    public MinPQ(int capacity) {
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
                newEnt.key.compareTo(this.rankedArray[hole / 2].key) < 0;
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
     * Returns the number of entries in the MinPQ.
     * @return
     *      integer of the number of filled positions of the minPQ
     */
    public int size() {
        return this.currentSize;
    }
    
    /**
     * Find the smallest item in the priority queue.
     * @return the smallest item, or throw an UnderflowException if empty.
     */
    public K findMin() {
        if (this.isEmpty()) {
            throw new NullPointerException();
        }
            
        return this.rankedArray[1].key;
    }
    
    /** 
     * Find the smallest item in the pq.
     * @return returns the value of it
     */
    public V findMinValue() {
        if (this.isEmpty()) {
            throw new NullPointerException();
        }
            
        return this.rankedArray[1].value;
    }
        
    /**
     * Prints the elements of the heap.
     * @param k the "position" in the ranked array
     * @return the value at a position in the heap.
     */
    public String dispPQ(int k) {        
        return this.rankedArray[k].value.toString();
    }

    /**
     * Remove the smallest item from the priority queue.
     * @return the smallest item, or throw an UnderflowException if empty.
     */
    public K deleteMin() {
        if (this.isEmpty()) {
            throw new NullPointerException();
        }     

        K minItem = this.findMin();
        this.rankedArray[1] = this.rankedArray[this.currentSize--];
        this.percolateDown(1);

        return minItem;
    }
    
    /** 
     * Deletes the item at position pos from the priority queue.
     * @param pos the position to delete in the PQ
     */
    public void deletePos(int pos) {
        if (this.isEmpty()) {
            throw new NullPointerException();
        }     

        this.rankedArray[pos] = this.rankedArray[this.currentSize--];
        this.percolateDown(pos);
    }
    

    /**
     * Test if the priority queue is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.currentSize == 0;
    }

    /**
     * Make the priority queue logically empty.
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
                            this.rankedArray[child].key) < 0) {
                child++;
            }

            if (this.rankedArray[child].key.compareTo(tmp.key) < 0) {
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
        /** Key to formulate heap property. */
        public K key;
        /** value associated with key. */
        public V value;
        
        /** 
         * Default entry constructor.
         * @param keyv
         *      key to associate with entry.
         * @param valuev
         *      value associated with key
         */
        public Entry(K keyv, V valuev) {
            this.key = keyv;
            this.value = valuev;
        }
    }
}
