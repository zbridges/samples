/**
 * Given a sorted list of characters, the program sorts
 * an unsorted list of strings containing the characters. 
 * Assumes that the sorting of the unsorted list is unambiguous. 
 */
public final class CodeSort {
    
    /**
     * private constructor to appease checkstyle. 
     */
    private CodeSort() {
        
    }
    
    /**
     * Sorts the unsorted list passed in the arguments.
     * @param args Array containing the input and output file names.
     **/
    public static void main(String[] args) {
        String inSortedf = args[0];
        String inUnSortedf = args[1];
        String outf = args[2];

        CodeSortGraph sortTest = 
                new CodeSortGraph(inSortedf, inUnSortedf, outf);
        
        sortTest.sort();
    }
}