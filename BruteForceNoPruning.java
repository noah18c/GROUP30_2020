package proj1;
/**
 * A Simple brute force algorithm that runs over every single possibility of how to color the graph.
 * It is extremely slow, however the chromatic number is guaranteed to be correct.
 * 
 * @author I. Heijnens
 * @version 1.0
 */

import java.util.*;

class Vertex  {
    ArrayList<Integer> connections = new ArrayList<>();
	int color = -1;
        int id;
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
            // Reset the color
            color = 0;
            boolean solved = false;
            while (color <= maxColors) {
                // If the graph is valid, then stop.
                if (verify(vertices)) return true;
                // If this is not the last vertex, run this method on the next vertex
                if (this != vertices[vertices.length-1]) solved = vertices[id+1].next(vertices, maxColors);
                // If the graph is reported to be valid, then stop.
                if (solved) return true;
                color++;
            }
            if (color > maxColors) color = 0;
            return false;

        }

        /**
         * Verify whether a coloring is valid
         * 
         * @param vertices Array of colored Vertex objects
         * @return If the 
         * 
         */
        public static boolean verify(Vertex[] vertices) {
		Vertex[] verifyVertices = new Vertex[vertices.length];
		System.arraycopy(vertices, 0, verifyVertices, 0, vertices.length);
		boolean isVerified = true;
		// If none of the vertices have a connected vertex with the same color, then it is valid.
		for (int i = 0; i < verifyVertices.length && isVerified; i++) {
			Vertex vertex = verifyVertices[i];
			if (vertex.color == -1) return false;
			for (Integer edge : vertex.connections) {
				if (vertex.color == verifyVertices[edge].color) return false;
			}
		}
		return true;
	}

    }
    

public class BruteForceNoPruning {
    /**
     * Main method of the brute force algorithm
     * 
     * @param e Array of ColEdge Objects
     * @param n Number of vertices
     * @return Chromatic number
     */
    public static int run(ColEdge[] e, int n) {
        Vertex[] vertices = toVertexArray(e, n);
        long startTime = System.nanoTime();
        int[] colors = new int[n + 1];
        int maxColors = 0;
        int i = 0;
        boolean ok = false;
        // Set all the colors to 0
        for (Vertex v : vertices) {
            if (v == null) v = vertices[i] = new Vertex(i);
            v.color = 0;
            i++;
        }
        while (!ok) {
            i = 0;
            
            // Try to solve using maxColors colors, if it fails, try again with maxColors+1
            ok = attemptBruteForce(maxColors, vertices);

            if (!ok)
                maxColors++;
        }
        System.out.println("Chromatic number: " + (maxColors + 1));
        System.out.println("Time needed: " + (System.nanoTime() - startTime)/1000000.0 + " ms");
        return 0;
    }

    /**
     * 
     * @param maxColors
     * @param vertices
     * @return  {@code true} if it is possible to color the graph using {@code maxColors} colors
     * {@code false} if it is not possible to color the graph using {@code maxColors} colors
     */
    private static boolean attemptBruteForce(int maxColors, Vertex[] vertices) {
        boolean end = false;
        end = vertices[1].next(vertices, maxColors);
        boolean result = Vertex.verify(vertices);
        if (ReadGraph.DEBUG) System.out.println("Tried " + (maxColors + 1) + " colors: " + result);
        if (maxColors > vertices.length) System.exit(1);
        return result;
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
