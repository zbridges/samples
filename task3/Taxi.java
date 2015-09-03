import java.util.Scanner;
/**
 * Runs an program that connects the nearest drivers to users.
 * 
 */
public final class Taxi {
    /**
     * Number of drivers to find.
     */
    private static final int KINDEX = 0;
    /**
     * File name of file with map locations.
     */
    private static final int MAPLOCATIONSINDEX = 1;
    /**
     * File name of file with map connections.
     */
    private static final int MAPCONNECTIONSINDEX = 2;
    /**
     * File name of file with driver ids and locations.
     */
    private static final int DRIVERLOCATIONSINDEX = 3;
    /**
     * Private constructor.
     */
    private Taxi() {
        
    }
    /**
     * The main class for the program.
     * 
     * @param args The input file names.
     */
    public static void main(String[] args) {
        Integer options = Integer.parseInt(args[KINDEX]);
        String mapLocOptions = args[MAPLOCATIONSINDEX];
        String mapConnections = args[MAPCONNECTIONSINDEX];
        String driverLoc = args[DRIVERLOCATIONSINDEX];

        Scanner scan = new Scanner(System.in);

        TaxiGraph uber = new TaxiGraph(options, mapLocOptions, 
                mapConnections, driverLoc);    
        System.out.println("Map locations are:");
        System.out.println();
        System.out.print(uber.printMapLocations());
        System.out.println();
        
        System.out.print("Enter number of recent client "
                + " pickup request location: ");
        int locationrequest = scan.nextInt() - 1;
        System.out.println();
        System.out.println();

        System.out.println("The " + options 
                + " drivers to alert about this pickup are:");
        System.out.println();
        uber.printNearestDrivers(locationrequest);
        System.out.println();

        System.out.print("Enter the ID number of the driver who responded: ");
        int inputID = scan.nextInt();
        System.out.println();
        System.out.println();
        System.out.println("The recommended route for driver " 
                + inputID + " is: ");
        System.out.println(uber.getPath(inputID));
    }
}
