import java.util.Stack;
/**
 * UnionFindQuickUnions uses quick unions and path compression.
 */
public class UnionFindQuickUnions implements UnionFind {
    /** Default capacity.*/
    private static final int DEFAULT_CAPACITY = 10;
    /** Integer array that holds sizes and roots.*/
    private int[] array;

    
    /** 
     * Default constructor that sets an array of 10. 
     */
    public UnionFindQuickUnions() {
        this(DEFAULT_CAPACITY);
    }
    
    /** 
     * Constructor that initializes the integer array.
     * @param size size of the array. Also number of subsets. 
     */
    public UnionFindQuickUnions(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException();
        } else {
            this.array = new int[size];
            for (int i = 0; i < size; i++) {
                this.array[i] = -1;
            }
        } 
    }
    
    /**
     * Determine the name of the set containing the specified element.
     * @param x the element whose set we wish to find
     * @return the name of the set containing x
     */
    public int find(int x) {
        int parent = x; 
        Stack<Integer> traveled = new Stack<Integer>();
        
        while (this.array[parent] >= 0) {
            traveled.push(parent);
            parent = this.array[parent];
        }
        
        while (!traveled.isEmpty()) {
            int nextInPath = traveled.pop();
            this.array[nextInPath] = parent;
        }
        
        return parent;
    }

    /**
     * Merge two sets if they are not already the same set.
     * @param a an item in the first set to be merged (need not be set name)
     * @param b an item in the second set to be merged (need not be set name)
     */
    public void union(int a, int b) {
        if (this.find(a) != this.find(b)) {
            int asize = Math.abs(this.array[this.find(a)]);
            int bsize = Math.abs(this.array[this.find(b)]);           
            
            if (asize >= bsize) {
                // a is greater than or equal b
                // a absorbs b
                this.array[this.find(a)] = -(asize + bsize);
                this.array[this.find(b)] = this.find(a);
                
            } else if (asize < bsize) {
                // a is smaller than b
                // b absorbs a
                this.array[this.find(b)] = -(asize + bsize); 
                this.array[this.find(a)] = this.find(b);
            }
        }
    }

    /**
     * Return the number of subsets in the structure.
     * @return the number of subsets
     */
    public int getNumSubsets() {
        int countSubsets = 0;
        
        for (int i = 0; i < this.array.length; i++) {
            if (this.array[i] < 0) {
                countSubsets++;
            }
        }
        return countSubsets;
    }

    /**
     * Returns a String representation of the implementation.  Normally
     * this would never be part of an interface like this, but will help us
     * test your implementation in a consistent way.  See assignment handout.
     * @return a String representing the current state of the structure
     */
    public String getCurrentState() {
        String printStatement = "";
        for (int i = 0; i < this.array.length; i++) {
            printStatement += i + ": " + this.array[i] + "\n";
        }
        return printStatement;
    }

}
