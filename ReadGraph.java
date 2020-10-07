import java.io.*;
import java.util.*;

	class ColEdge {
		int u;
		int v;
		
		//colors assigned to V and U, starts with -1 being blank
		int colU = -1;
		int colV = -1;
		//legality check
		public boolean legal() {
			boolean legal = true;
			if(colV != -1 && colV == colU){legal = false;}
			return legal;
		}
	}
		
	public class ReadGraph
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
			int[][] vMat = vertexMatrix(e, m, n); //this makes a matrix to contain data about the vertices
			matrixPrinter(vMat);//prints interpretation of vMat
			e = colorFiller(0, vMat[0][0], e, vMat); //assign color 0 to node 1 (located in vMat[0][0], but you can also just use the number 1) using array of ColEdge objects, updating vMat
			e = colorFiller(0, 2, e, vMat);//assign color 0 to node 2 (you can also use vMat[1][0]) using array of ColEdge objects, updating vMat
			matrixPrinter(vMat);//prints updated interpretation of vMat
			System.out.println(legalGraph(e)); //prints whether the graph is legal
		}
		/*this returns a 2 dimensional array with in the first column the vertex number, 
		in the second column the weight of that vertex and in the third the color of that vertex
		Note: node 1 == vertexMatrix[0][0], node 2 == vertexMatrix[1][0], etc...
		*/
		public static int[][] vertexMatrix(ColEdge[] edge, int edgesAmount, int verticesAmount){
			int[][] vertices = new int[verticesAmount][3];
			ColEdge node = new ColEdge();
			for(int i = 0; i<verticesAmount; i++){
				vertices[i][0] = i + 1;
			}
			for(int vertexNumber = 1; vertexNumber <= verticesAmount; vertexNumber++){
				for(int i = 0; i<edgesAmount; i++){
					if(edge[i].u == vertexNumber || edge[i].v == vertexNumber){
						vertices[vertexNumber-1][1] += 1;
					}
				}
			}
			for(int i = 0; i < verticesAmount; i++){
				vertices[i][2] = -1; //all colors are set to -1, meaning blank. If >=0, then the vertex has a color.
			}
			return vertices;
		}
		public static void matrixPrinter(int[][] vertex){
			System.out.println();
			for(int i = 0; i < vertex.length; i++){
				System.out.println("The nodeweight of vertex "+(i+1)+" is: "+vertex[i][1]+". And the color is: "+vertex[i][2]);
			}
		}
		public static ColEdge[] colorFiller(int color, int nodeNumber, ColEdge[] edge, int[][] vertexMatrix){
			for(int i = 0; i<edge.length; i++){
				if(edge[i].u == nodeNumber){
					edge[i].colU = color;
				}
				if(edge[i].v == nodeNumber){
					edge[i].colV = color;
				}	
			}
			vertexMatrix[nodeNumber-1][2] = color;
			return edge;
		}
		
		public static boolean legalGraph(ColEdge[] edge){
			int i;
			System.out.println(edge.length);
			for(i = 0; (edge[i].legal() == true) && (i < edge.length-1); i++){}
			if(edge[i].legal() == false){return false;}
			return true;
		}
		

				
}
