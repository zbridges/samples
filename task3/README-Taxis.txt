achen70, zbridge1
README-Taxis

For this problem, we applied Dijkastra's algorithm where the source
is the requested pickup location. The problem could be represented with 
an undirected graph with weighted edges. Since the sole purpose of our TaxiGraph
class was to perform Dijkastra's algorithm, we used data structures that 
would increase the efficiency of Dijkastra's. 

We used an adjacency list to represent the graph. Each street was assigned a
vertex number based on the input file, and the data was stored for quick reference
using a HashMap. 

To get the unfound vertex with the smallest distance, we used an adaptable MinHeap as the smallest
element could be accessed in logN time. 

While Dijkastra's algorithm calculates the distance from the source to all vertices, 
to find the k closest drivers, we called the distance array for each of the drivers, and stored them
in a MaxPQ of size k. This way, it was constant time to print out the heap, but logk to process
each driver. 