package proj1;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

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

//-------------------------------------------------------
		
public class ReadGraph
{
	
	public final static boolean DEBUG = false;
	public final static String COMMENT = "//";
	
//	!enable for the driver class to run multiple tests!	
//	public ReadGraph(String file)
//	{
//		run(file);
//	}
		
	//-------------------------------------------------------
	
	public static void main( String args[] )
	
	// !enable for the driver class to run multiple tests!	
	// public static void run(String file)
	{
		if( args.length < 1 )
		{
			System.out.println("Error! No filename specified.");
			System.exit(0);
		}

			
		String inputfile =  args[0]; //file; !enable for the driver class to run multiple tests	
		
		boolean seen[] = null;
		
		int n = -1;					//! n is the number of vertices in the graph
		int m = -1;					//! m is the number of edges in the graph
		ColEdge e[] = null;			//! e will contain the edges of the graph
		
		try 
		{ 
			FileReader fr = new FileReader(inputfile);
			BufferedReader br = new BufferedReader(fr);

		        String record = new String();
				
				//! The first few lines of the file are allowed to be comments, staring with a // symbol.
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
					record = br.readLine();												// Pull a new string from the file
					String data[] = record.split(" ");									// Split into array of strings at each " "
					if( data.length != 2 )												// If data[] has more than two points (bad read)
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
	
	//-------------------------------------------------------
		
		if(args.length == 4)
		{
			int times = Integer.parseInt(args[2]);
			int xg = Integer.parseInt(args[3]);
			if(args[1].equalsIgnoreCase("-s"))
			{
				ColEdge[] eCopy = Arrays.copyOf(e, e.length);
				for(int i = 0; i < times; i++)
				{
					SAT3 sat3 = new SAT3(e, m, n, xg);
				}
			}
		}
		else if(args.length == 3)
		{
			int times = Integer.parseInt(args[2]);
			
			if(args[1].equalsIgnoreCase("-d"))
			{
				for(int i = 0; i < times; i++)
				{
					ColEdge[] eCopy = copyEdges(e);
					DSATUR dsatur = new DSATUR();
					dsatur.run(eCopy, m, n);
				}
			}
			if(args[1].equalsIgnoreCase("-g"))
			{
				System.out.println("Greedy: Still to be implemented");
			}
			if(args[1].equalsIgnoreCase("-bt"))
			{
				System.out.println("Backtracking: Still to be implemented");
			}
			if(args[1].equalsIgnoreCase("-bf"))
			{
				for(int i = 0; i < times; i++)
				{
					ColEdge[] eCopy = Arrays.copyOf(e, e.length);
					DSATUR dsatur = new DSATUR();
					dsatur.run(eCopy, m, n);
				}
			}
		}
		else if(args.length == 1)
		{
			System.out.println("File only: Still to be implemented");
		}
		else
		{
			System.out.println("Please enter a vaild set of arguments. For more information"
					+ " run ReadGraph -help");
			System.exit(0);
		}
	}
	
	/**
	 * Make a deep copy of a ColEdge array
	 * @param e - An array of edges
	 * @return  - A deep copy of the array
	 */
	public static ColEdge[] copyEdges(ColEdge[] e)
	{
		ColEdge[] eCopy = new ColEdge[e.length];
		for(int i = 0; i < e.length; i++)
		{
			eCopy[i] = new ColEdge();
			eCopy[i].u = e[i].u;
			eCopy[i].v = e[i].v;
			eCopy[i].colU = e[i].colU;
			eCopy[i].colV = e[i].colV;
		}
		return eCopy;
	}	
}
