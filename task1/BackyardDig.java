/**
 * Runner file for Task 1 HW_6. 
 */
public final class BackyardDig {
    
    /**
     * Empty constructor to resolve Checkstyle.
     */
    private BackyardDig() {
        
    }
    
    /**
     * Create an output file with the result to the problem.
     * @param args The input file's name and the output file's name.
     */
    public static void main(String[] args) {
        String inputf = args[0];
        String outputf = args[1];
    
        BackyardDigGraph dig  =  new BackyardDigGraph(inputf, outputf);

        dig.kruskals();               
    }

}
