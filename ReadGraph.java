import java.io.*;
import java.util.*;

	/**
	{@link ReadGraphBanana}
	class that contains a ColEdge instance, with endpoints u and v.
	@author Noah Croes
	It now also contains colors colU and colV, initialized to -1 and a method that checks wether the ColEdge is legal
	*/
	class ColEdge {
		int u;
		int v;
		
		//colors assigned to V and U, starts with -1 being blank
		int colU = -1;
		int colV = -1;
		
		/**
		@author Noah Croes
		Method that decides whether two colors assigned to a ColEdge instance is legal
		Uses variables colU and colV of ColEdge class
		@return boolean for edge on whether its legal  
		*/
		public boolean legal() {
			boolean legal = true;
			if(colV != -1 && colV == colU){legal = false;}
			return legal;
		}
	}
		
	public class ReadGraphBanana
		{
		
		public final static boolean DEBUG = true;
		
		public final static String COMMENT = "//";
		
		public static void main( String args[] )
			{
			if( args.length < 1 )
				{
				System.out.println("Error! No filename specified.");
				System.exit(0);
				}

				
			String inputfile = args[0];
			
			boolean seen[] = null;
			
			//! n is the number of vertices in the graph
			int n = -1;
			
			//! m is the number of edges in the graph
			int m = -1;
			
			//! e will contain the edges of the graph
			ColEdge e[] = null;
			
			try 	{ 
			    	FileReader fr = new FileReader(inputfile);
			        BufferedReader br = new BufferedReader(fr);

			        String record = new String();
					
					//! THe first few lines of the file are allowed to be comments, staring with a // symbol.
					//! These comments are only allowed at the top of the file.
					
					//! -----------------------------------------
			        while ((record = br.readLine()) != null)
						{
						if( record.startsWith("//") ) continue;
						break; // Saw a line that did not start with a comment -- time to start reading the data in!
						}
	
					if( record.startsWith("VERTICES = ") )
						{
						n = Integer.parseInt( record.substring(11) );					
						if(DEBUG) System.out.println(COMMENT + " Number of vertices = "+n);
						}

					seen = new boolean[n+1];	
						
					record = br.readLine();
					
					if( record.startsWith("EDGES = ") )
						{
						m = Integer.parseInt( record.substring(8) );					
						if(DEBUG) System.out.println(COMMENT + " Expected number of edges = "+m);
						}

					e = new ColEdge[m];	
												
					for( int d=0; d<m; d++)
						{
						if(DEBUG) System.out.println(COMMENT + " Reading edge "+(d+1));
						record = br.readLine();
						String data[] = record.split(" ");
						if( data.length != 2 )
								{
								System.out.println("Error! Malformed edge line: "+record);
								System.exit(0);
								}
						e[d] = new ColEdge();
						
						e[d].u = Integer.parseInt(data[0]);
						e[d].v = Integer.parseInt(data[1]);

						seen[ e[d].u ] = true;
						seen[ e[d].v ] = true;
						
						if(DEBUG) System.out.println(COMMENT + " Edge: "+ e[d].u +" "+e[d].v);
				
						}
									
					String surplus = br.readLine();
					if( surplus != null )
						{
						if( surplus.length() >= 2 ) if(DEBUG) System.out.println(COMMENT + " Warning: there appeared to be data in your file after the last edge: '"+surplus+"'");						
						}
					
					}
			catch (IOException ex)
				{ 
		        // catch possible io errors from readLine()
			    System.out.println("Error! Problem reading file "+inputfile);
				System.exit(0);
				}

			for( int x=1; x<=n; x++ )
				{
				if( seen[x] == false )
					{
					if(DEBUG) System.out.println(COMMENT + " Warning: vertex "+x+" didn't appear in any edge : it will be considered a disconnected vertex on its own.");
					}
				}

			//! At this point e[0] will be the first edge, with e[0].u referring to one endpoint and e[0].v to the other
			//! e[1] will be the second edge...
			//! (and so on)
			//! e[m-1] will be the last edge
			//! 
			//! there will be n vertices in the graph, numbered 1 to n
			
			// m = edges amount
			// n = vertices amount
			// 

			//! INSERT YOUR CODE HERE!
			int[][] vMat = vertexMatrix(e, m, n);
			matrixPrinter(vMat);
			e = colorFiller(0, vMat[0][0], e, vMat); 
			e = colorFiller(0, 2, e, vMat);
			matrixPrinter(vMat);
			System.out.println(legalGraph(e));
		}
		
		/**
		This method generates a matrix with the following elements:
		- column 0 contains all the vertex numbers in ascending order (starting from 1)
		- column 1 contains the respective weight of each vertex
		- column 2 contains the respective color of each vertex (initially set to -1)
		@author Noah Croes
		@param e array of ColEdge class instance. In this case it is always e object from main method
		@param m edges amount
		@param n vertices amount
		@return matrix containing the nodes, their respective weights and color
		*/
		public static int[][] vertexMatrix(ColEdge[] e, int m, int n){
			int[][] vMat = new int[n][3];
			for(int i = 0; i<n; i++){
				vMat[i][0] = i + 1;
			}
			for(int vertexNumber = 1; vertexNumber <= n; vertexNumber++){
				for(int i = 0; i<m; i++){
					if(e[i].u == vertexNumber || e[i].v == vertexNumber){
						vMat[vertexNumber-1][1] += 1;
					}
				}
			}
			for(int i = 0; i < n; i++){
				vMat[i][2] = -1; //all colors are set to -1, meaning blank. If >=0, then the vertex has a color.
			}
			return vMat;
		}
		/**
		Method for printing out the instructions of how the matrix from {@link #vertexMatrix(ColEdge[], int, int)} should be interpreted
		@author Noah Croes
		@param vMat is the matrix generated by {@link #vertexMatrix(ColEdge[], int, int)}
		*/
		public static void matrixPrinter(int[][] vMat){
			System.out.println();
			for(int i = 0; i < vMat.length; i++){
				System.out.println("The nodeweight of vertex "+(i+1)+" is: "+vMat[i][1]+". And the color is: "+vMat[i][2]);
			}
		}
		/**
		method that assigns a color to a specific node on ColEdge[] e, 
		while also updating the color in the matrix previously generated by {@link #vertexMatrix(ColEdge[], int, int)}.
		@author Noah Croes
		@param color specific color that you want to assign
		@param nodeNumber the specific node (vertex) that you want to asssign the color to
		@param e array of ColEdge class instance. In this case it is always e object from main method
		@param vMat is the matrix generated by {@link #vertexMatrix(ColEdge[], int, int)}, which will be updated
		@return e which contains the newly assigned color on each concerned ColEdge e[n], where n is the edge number of the graph
		*/
		public static ColEdge[] colorFiller(int color, int nodeNumber, ColEdge[] e, int[][] vMat){
			for(int i = 0; i<e.length; i++){
				if(e[i].u == nodeNumber){
					e[i].colU = color;
				}
				if(e[i].v == nodeNumber){
					e[i].colV = color;
				}	
			}
			vMat[nodeNumber-1][2] = color;
			return e;
		}
		/**
		Method that decides whether the whole graph is currently legal or not (regarding coloring). 
		It uses the {@link ColEdge#legal()} method, which checks one specific edge. This method does it for all edges.
		@author Noah Croes
		@param e array of ColEdge class instance. In this case it is always e object from main method
		@return boolean on whether the whole graph is legal
		*/
		public static boolean legalGraph(ColEdge[] e){
			int i;
			System.out.println();
			for(i = 0; (e[i].legal() == true) && (i < e.length-1); i++){}
			if(e[i].legal() == false){return false;}
			return true;
		}	
}
