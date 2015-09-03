import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Given a sorted list of characters, the program sorts
 * an unsorted list of strings containing the characters. 
 * Assumes that the sorting of the unsorted list is unambiguous. 
 */
public class CodeSortGraph {
    /** Establishes initial capacity for the hashtable. */
    private static final int INITIAL_CAPACITY = 100;
    /** Name of the output file. */
    private String outputf;
    /** File name of the unsorted file. */
    private String unsortedf;
    /** Total number of vertices/characters. */
    private int numVertices;
    /** 
     * Hashmap of the characters with key as the character
     * and value of the assigned vertex number. 
     */
    private HashMap<Character, Integer> vertices; 
    /** Stores a list of vertices with edges pointing towards
     * the vertex number corresponding to a character. */
    private ArrayList<ArrayList<Integer>> indegList;
    /** Stores a list of vertices with edges pointing out from 
     * the array postion corresponding to the character's assigned
     * vertex value. 
     */
    private ArrayList<ArrayList<Integer>> outdegList;
    /** Stores characters at the assigned vertex value. */
    private ArrayList<Character> numtoChar;
    /** Stores the sorted order of characters (identified by vertices). */
    private ArrayList<Integer> sortedV;
    /** 1D array containing the sorted strings. */
    private String[] results;

    
    /** 
     * Constructor that instantiates all variables but results. 
     * Reads the file and builds the adjacency lists. 
     * @param smallsortfile file name containing the sorted strings
     * @param unsortedfile file name containing strings to sort
     * @param outputfile file name of sorted output
     */
    public CodeSortGraph(String smallsortfile, String unsortedfile, 
            String outputfile) {
        this.outputf = outputfile;
        this.unsortedf = unsortedfile;
        this.numVertices = 0;
        this.vertices = new HashMap<>(INITIAL_CAPACITY);
        this.indegList = new ArrayList<>();
        this.outdegList = new ArrayList<>();
        this.numtoChar = new ArrayList<>();
        this.sortedV = new ArrayList<>();

        try {
            Scanner in = new Scanner(new File(smallsortfile));
            String previous;
            String current = in.nextLine();
            this.addVertices(current);
            while (in.hasNextLine()) {
                previous = current;
                current = in.nextLine();
                this.addVertices(current);
                this.addEdges(previous, current);   
            }
            in.close();
    
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file input.");
            System.exit(0);
        }
    }

    /**
     * Helper method to add vertices to the vertex ArrayList.
     * @param current String with characters to add to vertex list. 
     */
    private void addVertices(String current) {
        for (int i = 0; i < current.length(); i++) {
            char currentChar = current.charAt(i);
            if (!this.vertices.containsKey(currentChar)) {
                this.vertices.put(currentChar, this.numVertices);
                this.numtoChar.add(currentChar);
                this.indegList.add(new ArrayList<Integer>());
                this.outdegList.add(new ArrayList<Integer>());
                this.numVertices++;
            }
        }
    }

    /** 
     * Helper method to establish relationships by updating adjacency lists.
     * @param prev previous string to compare too. 
     * @param curr current string to analyze new ordering properties. 
     */
    private void addEdges(String prev, String curr) {
        int currIndex = 0;
        while (prev.length() > currIndex + 1 
                && prev.charAt(currIndex) == curr.charAt(currIndex)) {
            currIndex++;
        }
    
        if (prev.length() > currIndex) {
            int fromIndex = this.vertices.get(prev.charAt(currIndex));
            int toIndex = this.vertices.get(curr.charAt(currIndex));
            this.indegList.get(toIndex).add(fromIndex);
            this.outdegList.get(fromIndex).add(toIndex);
        }
    }

    /**
     * Private method that uses a topological sort to get character ordering.
     */
    private void  topological() {
        //Create Set of inDegree 0; should have size 1
        ArrayList<Integer> zeroInDegree = new ArrayList<>();
        for (int i = 0; i < this.indegList.size(); i++) {
            if (this.indegList.get(i).size() == 0) {
                zeroInDegree.add(i);
            }
        }
    
        while (zeroInDegree.size() > 0) {
            int temp = zeroInDegree.remove(0);
            this.sortedV.add(temp);
            for (int i = 0; i < this.outdegList.get(temp).size(); i++) {
                int vertexValue = this.outdegList.get(temp).get(i);
                this.indegList.get(vertexValue).remove(new Integer(temp));
                if (this.indegList.get(vertexValue).size() == 0) {
                    zeroInDegree.add(vertexValue);
                }
            }
        
        }
        
    }

    /**
     * Sorts the unsorted file and writes to output file. 
     */
    public void sort() {
        this.topological();
        this.results = this.getUnsortedArray();
        this.quickSort(0, this.results.length - 1);
    
        try {
            File output = new File(this.outputf);
                    
            if (!output.exists()) {
                output.createNewFile();
            }
            
            FileWriter out = new FileWriter(output);
            for (int i = 0; i < this.results.length; i++) {
                out.write(this.results[i] + "\n");
            }
        
            out.close();
        } catch (IOException e) {
            System.out.println("Invalid output file name.");
            System.exit(0);
        }    
    }
    
    /**
     * Helper method to return the sorted ordering position of a character. 
     * @param c symbol to find ordering position
     * @return integer of priority. 
     */
    private int getSortedPosition(char c) {
        int vertNum = this.vertices.get(c);
        int sortedNum = 0;
        for (int i = 0; i < this.sortedV.size(); i++) {
            if (this.sortedV.get(i) == vertNum) {
                sortedNum = i;
            }
        }
        return sortedNum;
    }
    
    /**
     * Compares two characters based on ordering properties.
     * @param first first character
     * @param second second character
     * @return -1 if first comes after second
     *      1 if first comes before second
     *      0 if first equals second
     */
    private int compare(char first, char second) {
        //int indexVal1 = sortedV.get(vertices.get(first)); 
        //int indexVal2 = sortedV.get(vertices.get(second));
        int indexVal1 = this.getSortedPosition(first);
        int indexVal2 = this.getSortedPosition(second);
        if (indexVal1 == indexVal2) {
            return 0;
        } else if (indexVal1 > indexVal2) {
            return -1;
        } else {
            return 1;
        }
    }
    
    /**
     * Uses compare to compare two strings.
     * @param str1 First string to compare
     * @param str2 Second string to compare
     * @return -1 if first comes after second
     *          1 if first comes before second
     *          0 if first equals second
     */        
    private int compareString(String str1, String str2) {
        int currIndex = 0;
        while (str1.charAt(currIndex) == str2.charAt(currIndex)) {
            currIndex++;
            if (currIndex >= str1.length()) {
                if (currIndex >= str2.length()) {
                    return 0;
                }
                return 1; //str1 comes before str2
            } else if (currIndex >= str2.length()) {
                return -1; //str2 comes before str1
            }
        }
        return this.compare(str1.charAt(currIndex), str2.charAt(currIndex));
    }
    
    /**
     * Performs quicksort to sort the strings of the unsorted file.
     * @param lowInd lower index of array/subarray to quicksort
     * @param highInd upper index of array/subarray to quicksort
     */
    private void quickSort(int lowInd, int highInd) {
        if (lowInd < highInd) {
            int pivot = this.partition(lowInd, highInd);
            this.quickSort(lowInd, pivot - 1);
            this.quickSort(pivot + 1, highInd);
        }
    }

    /**
     * Helper method to sort subarrays. 
     * @param lo low index
     * @param hi high index
     * @return index of pivot
     */
    private int partition(int lo, int hi) {
        int pivotIndex = this.medianOfThree(hi, lo);
        String pivotValue = this.results[pivotIndex];
        this.swap(pivotIndex, hi);
        int storeIndex = lo;
        for (int i = lo; i < hi; i++) {
            if (this.compareString(this.results[i], pivotValue) > 0) {
                this.swap(i, storeIndex);
                storeIndex++;
            }
        }
        this.swap(storeIndex, hi);
        return storeIndex;
    }
    
    
    /**
     * Method to find the median of three for more efficient quicksort. 
     * @param hi greatest position in array. 
     * @param lo lowest position in array. 
     * @return the median value of highest, lowest, and middle position. 
     */
    private int medianOfThree(int hi, int lo) {
        //int pivot;
        int mid = (lo + hi) / 2;
        int loToMidComp = this.compareString(this.results[lo], 
                this.results[mid]);
        int midToHiComp = this.compareString(this.results[mid], 
                this.results[hi]);
        int loToHiComp = this.compareString(this.results[lo], this.results[hi]);
        if (loToMidComp > 0) {
            if (midToHiComp >= 0) {
                return mid;
            }
            if (loToHiComp <= 0) {
                return lo;
            }
        } else {
            if (midToHiComp <= 0) {
                return mid;
            }
            if (loToHiComp >= 0) {
                return lo;
            }
        }
        return hi;

    }
    
    /**
     * Swaps two values in the array. 
     * @param i position of first value to be swapped. 
     * @param j position of second value to be swapped. 
     */
    private void swap(int i, int j) {
        String temp = this.results[i];
        this.results[i] = this.results[j];
        this.results[j] = temp;
    }

    /**
     * Reads the unsorted file of strings and converts to a string array. 
     * @return String array containing unsorted strings
     */
    private String[] getUnsortedArray() {
        ArrayList<String> temp = new ArrayList<>();
        try {
            Scanner in = new Scanner(new File(this.unsortedf));
            while (in.hasNextLine()) {
                temp.add(in.nextLine());
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file name.");
            System.exit(0);
        }
        
        String[] unsorted = new String[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            unsorted[i] = temp.get(i);
        }

        return unsorted;
    }
}
