import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Given a file containing the toy coordinates, the digging paths, 
 * and the amount of work required for each path, the program outputs 
 * a file with the minimum amount of work required, and the paths to dig.
 */
public class BackyardDigGraph {
    /** 
     * 2D integer array that stores the 'vertex' number for each car
     * at its coordinates.
     */
    private int[][] carToNumber;
    /** ArrayList of Car coordinates at each assigned vertex number - 1. */
    private ArrayList<Car> numberToCar;
    /** MinPQ of edges with the weights as keys. */
    private MinPQ<Integer, Edge> edgePQ;
    /** Filename for the output file. */
    private String outputfname; 

    /**
     * Constructor that creates the data structures to solve the
     * backyard dig problem. 
     * @param inputfile name of the input file
     * @param outputfile name of the output file
     */
    public BackyardDigGraph(String inputfile, String outputfile) {
        this.outputfname = outputfile;
        
        try {
            Scanner inf = new Scanner(new File(inputfile));
            
            int row = inf.nextInt();
            int col = inf.nextInt();
            
            this.carToNumber = new int[row][col];
            this.numberToCar = new ArrayList<>();    
            this.edgePQ = new MinPQ<>();
            
            inf.nextLine(); //take out the /n
            inf.nextLine();

            //loop over each line
            int carCounter = 1;

            while (inf.hasNextLine()) {
                String carstr1 = inf.next();
                String carstr2 = inf.next();
                int weight = inf.nextInt();

                Car car1 = new Car(carstr1);
                Car car2 = new Car(carstr2);

                if (this.carToNumber[car1.x][car1.y] == 0) {
                    this.carToNumber[car1.x][car1.y] = carCounter;
                    this.numberToCar.add(car1);
                    carCounter++;   
                }   
                if (this.carToNumber[car2.x][car2.y] == 0) {
                    this.carToNumber[car2.x][car2.y] = carCounter;
                    this.numberToCar.add(car2);
                    carCounter++;   
                }

                Edge connE = new Edge(
                        this.carToNumber[car1.x][car1.y], 
                        this.carToNumber[car2.x][car2.y],
                        weight);    
                this.edgePQ.insert(connE.weight, connE);

                inf.nextLine();
            }

            inf.close();
        } catch (FileNotFoundException e) {
            System.out.println("Invald file. Please try again");
            System.exit(0);
        }
        
        
    }
    
    /**
     * Applies Kruskal's algorithm to find the minimum spanning tree, 
     * thus solving the backyard dig problem. 
     */
    public void kruskals() {
        UnionFindQuickUnions connected = 
                new UnionFindQuickUnions(this.numberToCar.size());
        ArrayList<Edge> results = new ArrayList<>();
        int totalWeight = 0;
        
        int count = 0;
        while (count < this.numberToCar.size() - 1) {
            Edge next = this.edgePQ.findMinValue();
            
            if (connected.find(next.car1 - 1) 
                    != connected.find(next.car2 - 1)) {
                results.add(next);
                connected.union(next.car1 - 1, next.car2 - 1);
                totalWeight += next.weight;
                count++;
            }
            
            this.edgePQ.deleteMin();
            
        }

        try {
            File output = new File(this.outputfname);
            
            if (!output.exists()) {
                output.createNewFile();
            }
            FileWriter out = new FileWriter(output);
            out.write(totalWeight + "\n\n");

            for (int i = 0; i < results.size(); i++) {
                Edge curr = results.get(i);
                Car c1 = this.numberToCar.get(curr.car1 - 1);
                Car c2 = this.numberToCar.get(curr.car2 - 1);
                out.write(c1 + " " + c2 + "\n");
            }

            out.close();
        } catch (IOException e) {
            System.out.println("Invalid output file name.");
            System.exit(0);
        }
        
    }

    /**
     * Private class that changes a string to coordinates. 
     */
    protected class Car {
        /** start index of y index substring.*/
        private static final int YPOSITION_LOCATOR = 3;
        /** final index of y index substring.*/
        private static final int YPOSITION_LOCATOR2 = 4;
        /** x coordinate of the toy.*/
        public int x;
        /** y coordinate of the toy.*/
        public int y;

        
        /**
         * Constructor for the car coordinates. 
         * @param form String of the form "(num1, num2)"
         */
        public Car(String form) {
            this.x = Integer.parseInt(form.substring(1, 2));
            this.y = Integer.parseInt(form.substring(YPOSITION_LOCATOR,
                    YPOSITION_LOCATOR2));
        }
        
        /** 
         * Prints coordinates in standard form. 
         * @return String format for coordinates
         */
        public String toString() {
            return "(" + this.x + "," + this.y + ")";
        }
    }

    /**
     * Private class that easily identifies the edge by the vertices
     * or cars connected. 
     */
    protected class Edge {
        /** vertex 1 of car. **/
        public int car1;
        /** vertex 2 of car. **/
        public int car2;
        /** weight of edge. **/
        public int weight;
        
        
        /** 
         * edge constructor.
         * @param c1 number associated with the car of edge
         * @param c2 number associated with the second car
         * @param w weight of the edge
         */
        public Edge(int c1, int c2, int w) {
            this.car1 = c1;
            this.car2 = c2;
            this.weight = w;
        }    
    }
}