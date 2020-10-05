package proj1;

import java.util.Arrays;

/*
!!! check that this code is updated in the ReadGraph class BEFORE running !!!

	class ColEdge
{
	int u;
	int v;
	int colU = -1;
	int colV = -1;
	
	public boolean legal()
	{
		boolean legal = true;
		if(colV != -1 && colV == colU)
			legal = false;	
		return legal;
	}
}
*/

//-------------------------------------------------------

public class DSATUR 
{

	private final boolean DEBUG = false;

	//-------------------------------------------------------

	public int run(ColEdge[] e, int m, int n)
		{
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
		
		private void printGraph(ColEdge[] e)
		{
			for(ColEdge edge: e)
			{
				System.out.println("V" + edge.v +" (" + edge.colV +") -> U" + edge.u + "(" + edge.colU + ")");
			}
		}
}
