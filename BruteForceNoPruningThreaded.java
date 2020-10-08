
/**
 * A Simple brute force algorithm that runs over every single possibility of how to color the graph.
 * It is extremely slow, however the chromatic number is guaranteed to be correct.
 * This version uses threading, which increases efficiency significantly.
 * 
 * @author I. Heijnens
 * @version 1.0
 */

import java.util.*;

class Vertex  {
    ArrayList<Integer> connections = new ArrayList<>();
	int color = -1;
        int id;
        /**
         * Creates a Vertex object with id {@code id}
         * @param id
         */
        public Vertex(int id) {
            this.id = id;
        }

        /**
         * Adds the id of a vertex to the list of connections
         * @param add
         */
        public void append(Integer add) {
            connections.add(add);
        }

        /**
         * Recursive method of the brute force algorithm
         * 
         * @param vertices Array of Vertex objects
         * @param maxColors Number of colors to use
         * @return {@code true} if it is possible to color the graph using {@code maxColors} colors
         * {@code false} if it is not possible to color the graph using {@code maxColors} colors
         * 
         */

        public boolean next(Vertex[] vertices, int maxColors) {
            color = 0; // Reset the color, all possibilities have to be checked
            boolean solved = false;
            while (color <= maxColors) {
                if (verify(vertices, 1)) return true;                                                                           // If the entire graph is valid, then stop and report
                if (this != vertices[vertices.length-1]) solved = vertices[id+1].next(vertices, maxColors);                     // If this is not the last vertex, run this method on the next vertex               
                if (solved) return true;                                                                                        // If the entire graph is reported to be valid, then stop
                color++;
                if (id == 1) return false; // id 1 always has to be color 0, because colors are interchangeable (i.e. Graph {0,1,2,0} is the same as {1,2,0,1})
            }
            if (color > maxColors) color = 0;
            return false;

        }

        /**
         * Verify whether a coloring is valid
         * 
         * @param vertices Array of colored Vertex objects
         * @return {@code true} if the graph is valid
	     * {@code false} if the graph is invalid
         */
        public static boolean verify(Vertex[] vertices, int start) {
            boolean isVerified = true;
            // If none of the vertices have a connected vertex with the same color, then it is valid.
			for (int i = start; i < vertices.length && isVerified; i++) {
				Vertex vertex = vertices[i];
				if (vertex.color == -1) return false;
				for (Integer edge : vertex.connections) {
					if (vertices[edge].id >= start && vertex.color == vertices[edge].color) return false;
				}
			}
			return true;
        }

        /**
         * Method for getting the next possibility for base graph of a certain thread
         * @param maxColors Maximum number of colors
         * @param vertices Array of Vertex objects
         */
        public void nextPossibility(int maxColor, Vertex[] vertices) {
	    	if (color == maxColor) {
            	color = 0;
            	if (this != vertices[vertices.length-1]) vertices[id+1].nextPossibility(maxColor, vertices); // Pass on to the next vertex
            } else color++;
        }
        

    }
    

public class BruteForceNoPruningThreaded {
    /**
     * Main method of the brute force algorithm
	 * 
	 * @param e Array of ColEdge objects
	 * @param n Number of vertices
     * @return The chromatic number
	 */
    public int run(ColEdge[] e, int n) {
        long startTime = System.nanoTime();
        int maxColors = 0;
        if (e.length != 0) { // Just in case
            Vertex[] vertices = toVertexArray(e, n);
            int i = 0;
            boolean ok = false;
            // Set all the colors to 0
            for (Vertex v : vertices) {
                if (v == null) v = vertices[i] = new Vertex(i);
                v.color = 0;
                i++;
            }
            vertices[0].color = -2; // This is to prevent any potential issues with the verification
            while (!ok) {
                i = 0;
                
                // Try to solve using maxColors colors, if it fails, try again with maxColors+1
                ok = attemptBruteForce(maxColors, vertices);

                if (!ok)
                    maxColors++;
            }
        }
        double time = (System.nanoTime() - startTime)/1000000.0;
        System.out.println("Chromatic number: " + (maxColors + 1));
        System.out.println("Time needed: " + (time + " ms"));
        Logger.logResults("BruteForceNoPruning", "graph04_2020.txt" , maxColors + 1, time);
        return 0;
    }

    /**
     * Start the brute force with threading algorithm
     * @param maxColors Maximum number of colors
     * @param vertices Array of Vertex objects
     * @return {@code true} if it is possible to color the graph using {@code maxColors} colors
     * {@code false} if it is not possible to color the graph using {@code maxColors} colors
     */
    private static boolean attemptBruteForce(int maxColors, Vertex[] vertices) {
        boolean result = threadedBruteForce(maxColors, vertices);
        if (ReadGraph.DEBUG) Logger.log("Tried " + (maxColors + 1) + " colors: " + result);
        System.out.println("Tried " + (maxColors + 1) + " colors: " + result);
        return result;
    }

    /**
     * Runs the brute force algorithm spread across multiple threads
     * Start the brute force with threading algorithm
     * @param maxColors Maximum number of colors
     * @param vertices Array of Vertex objects
     * @return {@code true} if it is possible to color the graph using {@code maxColors} colors
     * {@code false} if it is not possible to color the graph using {@code maxColors} colors
     */
    private static boolean threadedBruteForce(int maxColors, Vertex[] vertices) {    
        int threadlevels;
        if (maxColors >= 1) threadlevels = (int) (Math.log10(Runtime.getRuntime().availableProcessors() * 2) / Math.log10(maxColors+1)); // Max 2 threads per processor
        else threadlevels = 1;
        if (threadlevels + 2 > vertices.length) threadlevels = Math.max(1,vertices.length - 2);
        BruteForceThread[] threads = new BruteForceThread[(int) (Math.pow(maxColors + 1, threadlevels))];
        Boolean[] threadResults = new Boolean[threads.length];
        int i = 0;
        for (int j = 0; j < threads.length; j++) {
            (threads[i] = new BruteForceThread(maxColors,  fullCopy(vertices), threadlevels + 1, i, threadResults)).start();
            i++;
            if (i < threads.length) vertices[2].nextPossibility(maxColors, vertices);
        }
        for (int j = 0; j < vertices.length; j++) {
            while (!allFalse(threadResults)) if (hasTrue(threadResults)) return true; // Unless a thread has evaluated to true, wait for all threads to evaluate to false
        }
        return false;
    }
    

    /**
     * Creates a full copy of a vertex with all vertices copied
     * @param vertices Array of Vertex objects
     * @return A full copy of a vertex with all vertices copied
     */
    private static Vertex[] fullCopy(Vertex[] vertices) {
        Vertex[] nv = new Vertex[vertices.length];
        int i = 0;
        for (Vertex vertex : vertices) {
            nv[i] = new Vertex(i);
            nv[i].color = vertex.color;
            nv[i].connections = vertex.connections;
            i++;
        }
        return nv;
    }

    /**
     * Checks whether an array of Booleans has a true value
     * @param arr Array of Boolean objects
     * @return {@code true} if {@code arr} contains a true value
     * {@code false} if {@code arr} does not contain a true value
     */
    private static boolean hasTrue(Boolean[] arr) {
        for (Boolean boolean1 : arr) {
            if (boolean1 != null && boolean1 == true) return true;
        }
        return false;
    }
    /**
     * Checks whether an array of Booleans has a true value
     * @param arr Array of Boolean objects
     * @return {@code true} if all values in {@code arr} are false
     * {@code false} if not all values in {@code arr} are false
     */
    private static boolean allFalse(Boolean[] arr) {
        for (Boolean boolean1 : arr) {
            if (boolean1 == null || boolean1 != false) return false;
        }
        return true;
    }

    /**
     * Recursive method of the brute force algorithm
     * 
     * @param e Array of ColEdge objects
     * @param n Number of vertices
     * @return An array of Vertex objects
     * 
     */
    private static Vertex[] toVertexArray(ColEdge[] e, int n) {
        // Create an array of vertices, each consisting of a list of connected vertices
        Vertex[] vertices = new Vertex[n + 1];

        // For each edge, connect both vertices
        for (ColEdge edge : e) {
            if (vertices[edge.u] == null)
                vertices[edge.u] = new Vertex(edge.u);
            vertices[edge.u].append(edge.v);
            if (vertices[edge.v] == null)
                vertices[edge.v] = new Vertex(edge.v);
            vertices[edge.v].append(edge.u);
        }
        return vertices;
    }
}
