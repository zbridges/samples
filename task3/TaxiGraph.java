import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
/**
 * Graph builder for HW6 Task 3.
 *
 */
public class TaxiGraph {
    /**
     * This will get the k nearest drivers.
     */
    private int k;
    /**
     * Name of the file with the driver ids and locations.
     */
    private String driverFile;
    /**
     * Maps the vertex numbers of this graph to a location name.
     */
    private ArrayList<String> numToRoad;
    /**
     * Maps the location names to vertex numbers in this graph.
     */
    private HashMap<String, Integer> roadToNum;
    /**
     * Adjacency list of the vertices, map locations, in this graph.
     */
    private ArrayList<ArrayList<Endpoint>> adjList;
    /** 
     * MinPQ for the Dijkstra implementation.
     * Key is the distance, value is the vertex/road number.
     **/
    private MinPQ<Integer, Integer> distancePQ;
    /**
     * Hash map used for implementation of Dijkstra's algorithm.
     * 
     **/
    private HashMap<String, Integer> heapPositionMap;
    /**
     * Array that holds the path from the requested 
     * location to all the vertices in this graph.
     **/
    private int[] previous;
    /**
     * Array that holds the final distances of the 
     * shortest paths from the requested location.
     */
    private int[] distances;
    /**
     *  Used to find the k drivers that are closest to the requested location.
     *  Key is the distance, value is the Driver.
     **/
    private MaxPQ<Integer, Driver> kDrivers;
    /**
     * Holds the k closest drivers to the requested location.
     */
    private ArrayList<Driver> closestDrivers;

    /**
     * Creates a graph with the given data.
     *
     * @param kin Number of nearest drivers to get.
     * @param mapLocFile Name of the file with the map locations.
     * @param mapConnFile Name of the file with the map connections.
     * @param driverLocFile Name of the file with the driver ids and locations.
     */
    public TaxiGraph(int kin, String mapLocFile, String mapConnFile, 
            String driverLocFile) {
        this.k = kin;
        this.makeMapLocationMap(mapLocFile);
        this.makeAdjacencyList(mapConnFile);
        this.distancePQ = new MinPQ<>();        
        this.driverFile = driverLocFile;
    }
    /**
     * Adds all of the map locations to roadToNum and numToRoad.
     * 
     * @param mapLocFile File with the map locations.
     */
    private void makeMapLocationMap(String mapLocFile) {
        try {
            System.out.println("Collecting map locations from " 
                    + mapLocFile + "...");
            int count = 0;
            this.roadToNum  = new HashMap<>();
            this.numToRoad = new ArrayList<>();
            Scanner in = new Scanner(new File(mapLocFile));
            while (in.hasNextLine()) {
                String currName = in.nextLine().trim();
                this.roadToNum.put(currName, count);
                this.numToRoad.add(currName);
                count++;
            }
            System.out.println(this.numToRoad.size() + " locations input.");
            System.out.println();
            in.close();    

        } catch (FileNotFoundException e) {
            System.out.println("Invalid file name.");
            System.exit(0);
        }
    }
    /**
     * Makes the adjacency list for this graph with the input file.
     * 
     * @param mapConnFile File with map connection.
     */
    private void makeAdjacencyList(String mapConnFile) {
        this.adjList = new ArrayList<ArrayList<Endpoint>>();
        for (int i = 0; i < this.roadToNum.size(); i++) {
            this.adjList.add(new ArrayList<Endpoint>());
        }
        try {
            System.out.println("Collecting map connections from " + mapConnFile 
                    + "...");
            Scanner in = new Scanner(new File(mapConnFile));
            int counter = 0;
            while (in.hasNextLine()) {
                String next = in.nextLine();
                String[] combined = next.split("\t");
                String locations = combined[0];
                String weightStr = "";
                for (int i = 1; i < combined.length; i++) {
                    weightStr += combined[i];
                }
                Integer weight = Integer.parseInt(weightStr.trim());
                String[] splitLocations = locations.split(",");
                String locName1 = splitLocations[0].trim().
                        substring(1, splitLocations[0].length());
                int backparen = splitLocations[1].trim().indexOf(')');
                String locName2 = splitLocations[1].trim().
                        substring(0, backparen);
                int locNum1 =
                        this.getVertexNumber(locName1);
                int locNum2 = 
                        this.getVertexNumber(locName2);
                this.adjList.get(locNum1).add(new Endpoint(locNum2, weight));
                this.adjList.get(locNum2).add(new Endpoint(locNum1, weight));
                counter++;
            }
            System.out.println(counter + " connections input.");
            System.out.println();
            in.close();

        } catch (FileNotFoundException e) {
            System.out.println("Invalid file name.");
            System.exit(0);
        }

    }
    /**
     * Gets the vertex number of the given location.
     * 
     * @param location Name of the location.
     * @return  Vertex number of the location.
     */
    private int getVertexNumber(String location) {
        return this.roadToNum.get(location);
    }
    /**
     * Gets the k drivers closest to the location given.
     * 
     * @param location The location given.
     */
    public void getNearestDrivers(int location) {
        this.dijkstra(location);
        this.kDrivers = new MaxPQ<>(this.k);
        this.closestDrivers = new ArrayList<>();
        try {
            System.out.println("Collecting driver locations from " 
                    + this.driverFile + "...");
            int counter = 0;
            Scanner in = new Scanner(new File(this.driverFile));
            while (in.hasNextLine()) {
                int driverID = in.nextInt();
                String driverLoc = in.nextLine().trim();
                Driver curr = new Driver(driverID, driverLoc);
                int pos = this.roadToNum.get(driverLoc);
                int driverDistance = this.distances[pos];
                //add to heap
                if (this.kDrivers.size() < this.k) {
                    this.kDrivers.insert(driverDistance, curr);
                } else if (driverDistance < this.kDrivers.findMaxKey()) {
                    this.kDrivers.deleteMax();
                    this.kDrivers.insert(driverDistance, curr);
                }
                counter++;        
            }

            for (int i = 1; i <= this.k; i++) {
                this.closestDrivers.add(this.kDrivers.getValueAt(i));
            }
            System.out.println(counter + " drivers input.");
            System.out.println();
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file name.");
            System.exit(0);
        }    
    }
    /**
     * Calculates then saves and prints the k drivers 
     * closest to the given location.
     * 
     * @param location Location that drivers distances will be calculated from.
     */
    public void printNearestDrivers(int location) {
        for (int i = 0; i < this.closestDrivers.size(); i++) {
            System.out.println(this.closestDrivers.get(i).driverID + " at " 
                    + this.closestDrivers.get(i).driverLoc); 
        }
    }
    /**
     * Prints a list of the map locations in this graph.
     * 
     * @return A string with the map locations.
     */
    public String printMapLocations() {
        String mapLocStr = "";
        for (int i = 0; i < this.numToRoad.size(); i++) {
            mapLocStr += "\t" +  (i + 1) + " " + this.numToRoad.get(i) + "\n";
        }
        return mapLocStr;
    }
    /**
     * Gets the path of the driver whose id was passed.
     * 
     * @param drID Driver whose path will be given.
     * @return String with the path of the driver id.
     */
    public String getPath(int drID) {
        String chosenLoc = "";
        for (int i = 0; i < this.closestDrivers.size(); i++) {
            Driver tempDriver = this.closestDrivers.get(i);

            if (tempDriver.driverID == drID) {
                chosenLoc = tempDriver.driverLoc;        
            }
        }
        return this.getPath(chosenLoc);
    }
    /**
     * Gets the path that was calculated from the given location.
     * 
     * @param chosenLoc Start of the path given.
     * @return String of complete path.
     */
    private String getPath(String chosenLoc) {
        int currRdValue = this.roadToNum.get(chosenLoc);
        String path = "";
        String currName = chosenLoc;
        while (this.previous[currRdValue] != -1) {
            String prevName = this.numToRoad.get(this.previous[currRdValue]);
            path += "\t(" + currName + ", " + prevName + ")\n";
            currRdValue = this.roadToNum.get(prevName);
            currName = prevName;
        }
        path += "Expected total time: " 
                + this.distances[this.roadToNum.get(chosenLoc)] 
                + " minutes\n";
        return path;
    }
    /**
     * Calculates the distances from every vertex to the given location.
     * 
     * @param location Destination of the shortest path.
     */
    private void dijkstra(int location) {
        this.heapPositionMap = new HashMap<>();
        
        //found and previous defaults;
        boolean[] found = new boolean[this.numToRoad.size()];
        this.distances = new int[this.numToRoad.size()];

        this.previous =  new int[this.numToRoad.size()];
        for (int i = 0; i < found.length; i++) {
            found[i] = false;
            this.previous[i] = -1;
        }
        //distance defaults;
        for (int i = 0; i < this.numToRoad.size(); i++) {
            this.distancePQ.insert(Integer.MAX_VALUE, i);
            this.distances[i] = Integer.MAX_VALUE;
        }
        this.createPositionHM();

        this.updatedistancePQ(0, location);
        this.distances[location] = 0;

        for (int i = 0; i < this.numToRoad.size(); i++) {
            int curr = this.distancePQ.findMinValue();
            found[curr] = true;
            ArrayList<Endpoint> temp = this.adjList.get(curr);
            for (int j = 0; j < temp.size(); j++) {
                int newDistance = this.distances[curr] + temp.get(j).weight;
                int currEndpoint = temp.get(j).endpoint;
                if (newDistance < this.distances[currEndpoint]) {
                    this.distances[currEndpoint] = newDistance;
                    this.updatedistancePQ(newDistance, currEndpoint);
                    this.previous[currEndpoint] = curr;
                }
                
            }    
            this.distancePQ.deleteMin();
        }

    }
    /**
     * Initializes the distancePQ.
     */
    private void createPositionHM() {
        for (int i = 1; i <= this.distancePQ.size(); i++) {
            String streetname = this.numToRoad.get(
                    Integer.parseInt(this.distancePQ.dispPQ(i)));
            this.heapPositionMap.put(streetname, i);
        }
    }
    /**
     * Updates distance heap when a distance is changed during Dijkstra's.
     * 
     * @param newKey New, shorter distance to rdValue.
     * @param rdValue Number of the vertex whose distance 
     *      will be updated in the graph.
     */
    private void updatedistancePQ(Integer newKey, int rdValue) {
        int currentArrayVal = this.heapPositionMap.get(
                this.numToRoad.get(rdValue));
        this.distancePQ.deletePos(currentArrayVal);
        this.distancePQ.insert(newKey, rdValue);
        this.createPositionHM();
    } 

    /**
     * Holds the endpoint of an edge and its weight.
     *
     */
    protected class Endpoint {
        /** weight of the edge connecting the position to the endpoint.*/
        public int weight;
        /** number representation of the road endpoint.*/
        public int endpoint;
        
        /** 
         * Default constructor for and endpoint object. 
         * @param e int representation of the road that is the endpoint
         * @param w int of the weight of the edge/time to travel
         */
        public Endpoint(int e, int w) {
            this.weight = w;
            this.endpoint = e;
        }
    }
    /**
     * Holds a driver's id and location.
     *
     */
    protected class Driver {
        /** number representing driver ID. */
        public int driverID;
        /** location of the driver. */
        public String driverLoc;

        /**
         * Constructor for a driver object. 
         * @param id int ID 
         * @param location int representation of the road.
         */
        public Driver(int id, String location) {
            this.driverID = id;
            this.driverLoc = location;
        }

    }

}
