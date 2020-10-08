
import java.util.Arrays;

//-------------------------------------------------------

public class DSATUR 
{

	private final boolean DEBUG = false;

	//-------------------------------------------------------
	/**
	 * The loop method of the DSatur Algorithm. 
	 * The algorithm is an optimisation of the Greedy Algorithm, it will colour the most frequent vertex first.
	 * Subsequently the next vertex is chosen by the most saturated (i.e. the vertex with most adjacent coloured vertices).
	 * If none exists the algorithm will revert back to the most frequent.
	 * @param e	- ColEdge - Array of edges.
	 * @param m	- Int - Number of vertices.
	 * @param n - Int - Number of edges.
	 * @return 	- Int - The chromatic number
	 */
	public int run(ColEdge[] e, int m, int n)
		{
			System.out.println("Running DSATUR");
			// Start the clock!
			long start = System.nanoTime();
			// Colour the most frequent vertex
			colour(e, mostFreq(e, n), 0);
			int chromeNumb = 0;
			while(!complete(e))
			{
				int col = 0;
				int lastColoured = 0;
				boolean legal = false;
				int mostSat = mostSaturated(e, n);
				if(mostSat == lastColoured)
					mostSat = mostFreq(e, n);
				while(!legal)
				{
					// Find the next most saturated and colour it on a copy of the graph
					ColEdge[] eCopy = Arrays.copyOf(e, e.length);
					colour(eCopy, mostSat, col);
					
					// Check legality
					if(legal(eCopy))
						legal = true;		
					else
						col++;
				}
				// Commit colouring of copy to 'e' and record the last colour used
				colour(e, mostSat, col);
				lastColoured = col;
				if(col > chromeNumb)
					chromeNumb = col;
				
			}
			if(DEBUG) {printGraph(e);}
			
			System.out.println("Chomatic Number = " + (chromeNumb + 1)); // Add one due to counting colour 0
			System.out.println("The time needed to perform this analysis was: " + (System.nanoTime()-start)/1000000.0 + " ms.\n");
			return chromeNumb + 1;
		}
		
		//-------------------------------------------------------
		/**
		 * A method that colours the vertices within the ColEdge objects 
		 * @param e 		- the array of edges
		 * @param vertex	- the vertex to be coloured
		 * @param colour	- the colour to be added
		 * @return ColEdge	- the amended array of edges
		 */
		private ColEdge[] colour(ColEdge[] e, int vertex, int colour)
		{
			for(ColEdge edge: e)
			{
				if(edge.v == vertex)
					edge.colV = colour;
				if(edge.u == vertex)
					edge.colU = colour;
			}	
			if(DEBUG) {System.out.println("Coloured V" + vertex + " colour " + colour);}
			return e;
		}
		
		//-------------------------------------------------------
		/**
		 * A method that counts the stauration (number of adjacent vertices that have been coloured) 
		 * and returns the highest saturated.
		 * @param e  		- the edges for the graph
		 * @param vertices 	- the number of vertices
		 * @return int[] 	- the number of edges to each vertex
		 */	
		private int mostSaturated(ColEdge[] e, int vertices)
		{
			int[] sat = new int[vertices+1];
			for(int i = 0; i < e.length; i++)
			{
				if(e[i].colU > -1 && e[i].colV == -1)
					sat[e[i].v]++;
				if(e[i].colV > -1 && e[i].colU == -1)
					sat[e[i].u]++;
			}
			
			if(DEBUG){
				for(int i = 0; i < sat.length; i++){
					System.out.println("V" + i + ": adjacent to " + sat[i] + " vertices");}}
			
			int mostSat = 0;						//TODO - implement a method - this code is repeated twice
			int sum = 0;
			for(int i = 0; i < sat.length; i++)
			{
				if(sat[i] > sat[mostSat])
					mostSat = i;
				sum += sat[i];
			}
			
			if(DEBUG){System.out.println("Saturation: " + sum) ;}
			return mostSat;
		}
		//-------------------------------------------------------
		/**
		 * A method that counts the frequency of all of the vertices and returns the count of edges to each vertex
		 * @param e  		- the edges for the graph
		 * @param vertices 	- the number of vertices
		 * @return int[] 	- the number of edges to each vertex
		 */
		private int mostFreq(ColEdge[] e, int vertices)
		{
			int[] freq = new int[vertices+1];
			
			// Count how many edges each vertex has coming to it
			for(int i = 0; i < e.length; i++)
			{
				if(e[i].colV == -1)
					freq[e[i].v]++;
				if(e[i].colU == -1)
					freq[e[i].u]++;
			}		
			
			if(DEBUG){
				for(int i = 0; i < freq.length; i++){
					System.out.println("V" + i + ": appeared " + freq[i] + " times");}}
			
			int mostFreq = 0;							//TODO - implement a method - this code is repeated twice
			for(int i = 0; i < freq.length; i++)
			{
				if(freq[i] > freq[mostFreq])
					mostFreq = i;
			}	
			return mostFreq;
		}
		
		//-------------------------------------------------------
		/**
		 * Checks each edge for legality using a method within the ColEdge object
		 * @param e - ColEdge - Array of edges in the graph
		 * @return  - Boolean - {@code true} if the entire graph is legal 
		 */
		private boolean legal(ColEdge[] e)
		{
			boolean legal = true;
			for(int i = 0; i < e.length; i++)
			{
				if(!e[i].legal())
					legal = false;
			}
			if(DEBUG) {System.out.println("Legal: " + legal);}
			return legal;
		}
		
		//-------------------------------------------------------
		/**
		 * Checks each edge values for colU and colV are not '-1'.  If the entire graph is coloured
		 * returns {@code true}
		 * @param e - ColEdge - Array of edges in the graph
		 * @return  - Boolean - If the entire graph has been coloured
		 */
		private boolean complete(ColEdge[] e)
		{
			boolean complete = true;
			for(int i = 0; i < e.length; i++)
			{
				if(e[i].colV == -1)
					complete = false;
				if(e[i].colU == -1)
					complete = false;
			}
			if(DEBUG) {System.out.println("complete: " + complete);}
			return complete;
		}
		
		//-------------------------------------------------------
		/**
		 * Prints each edge (v then u) to the terminal.
		 * @param e - ColEdge - Array of edges in the graph
		 */
		private void printGraph(ColEdge[] e)
		{
			for(ColEdge edge: e)
			{
				System.out.println("V" + edge.v +" (" + edge.colV +") -> U" + edge.u + "(" + edge.colU + ")");
			}
		}
}
