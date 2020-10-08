
/**
 * Implementation of the 3-SATISFIABILITY algorithm from S.Kelk implemented by E.Hortal to solve satisfiability of k-Colouring of a graph
 * Heavily influenced by the paper "Polynomial Encoding of k-Color Graphs to SAT" of unknown author and
 * "Polynomial 3-SAT Encoding for K-Colorability of Graph" by P.Sharma and N.Chaudhari of the Indian Institute of Technology
 * 
 * @author L.Debnath
 * @version 1.0
 */

import java.util.*;

public class SAT3
	{
	
	final boolean DEBUG = false;
	
	/**
	 * 
	 * @param E - Array of ColEdge Objs
	 * @param v - Number of Vertices
	 * @param e - Number of Edges
	 * @param k - Number of Colours
	 */
	public SAT3(ColEdge[] E, int e, int v,  int k, String inputfile) 
	{
		// Create a list of colours represented as strings of letters
		String[] colours = {"a","b","c","d","e","f","g","h","i","j","k","l","m","o","p","q","r","s","t","u","v","w","x","y","z",
							"ab","ab","ac","ad","ae","af","ag","ah","ai","aj","ak","al","am","ao","ap","aq","ar","as","at","au","av","aw","ax","ay","az"}; 
		System.out.println("Running Satisfiability for " + k + " colours");
		
		// Create strings for L for At Least One Colour(ALOC), At Most One Colour (AMOC) and Different Colour (DCOL)
		// Calculate the total number of String[] arrays required
		int totalClauses = 0;
		if(k <= 2)
			totalClauses = (2 * v + (e * k));
		else
		{			// performs this (v + ( v * k * tc) + ( e * k)) as a double and the casts to int 
			double tc = (double) k;
			tc -= 1;
			tc = tc / 2;
			tc = tc * k * v;
			tc = tc + v + (e * k);
			totalClauses = (int) tc;
		}
		
		//	ALOC string[] = {V + K1, V + K2 ... V + Kn}  for  String[V][K]			|v|
		String[][] L = new String[totalClauses][];
		Boolean[][] L_values = new Boolean[totalClauses][];
		for(int i = 0; i < v; i++)
		{
			L[i] = new String[k];
			L_values[i] = new Boolean[k];
			for(int j = 0; j < k; j++)
			{
				int temp = i+1;
				L[i][j] = temp + colours[j];
				L_values[i][j] = null;
			}
		}
		
		//AMOC String[] = {V + K1, V + K2} {V + K1, V + K3}  for String[V][K]	   |v|*k*(k-1)/2
		int Lpntr = v;
		for(int i = 1; i <= v; i++)
		{
			for(int j = 0; j < k-1; j++)
			{
				for(int l = 0; l < k; l++)
				{
					// This ensures that you don't get two of the same colour or repetition 
					if(l != j && l > j )						
					{
						// Create two more elements
						L[Lpntr] = new String[2];
						L_values[Lpntr] = new Boolean[2];
						// Add clause (Vi + Kj + n ^ Vi + Kl + n) 
						L[Lpntr][0] = i + colours[j] + "n";
						L[Lpntr][1] = i + colours[l] + "n";
						// Add two new corresponding values to the L_Values 
						L_values[Lpntr][0] = null;
						L_values[Lpntr][1] = null;
						// Increment to the next pointer location
						Lpntr++;
					}
				}
			}
		}
			
		// For each edge V - U and colour K
		// DCOL string[] = {V + K + n, U + K + n}		for String[E][K]			e*k
		for(int j = 0; j < k; j++)
		{
			for(int i = 0; i < E.length; i++)
			{
				// Create two more elements
				L[Lpntr] = new String[2];
				L_values[Lpntr] = new Boolean[2];
				// Add clause (Ui + Kj + n ^ Vi + Kj + n)
				L[Lpntr][0] = E[i].u + colours[j] + "n";
				L[Lpntr][1] = E[i].v + colours[j] + "n";
				// Add two new corresponding values to the L_Values 
				L_values[Lpntr][0] = null;
				L_values[Lpntr][1] = null;
				// Increment to the next pointer location
				Lpntr++;
			}
		}
		
		// Encode V variables and A values to the arrays	
		String[] A = new String[v*k];
		Boolean[] A_values = new Boolean[v*k];
		int Apntr = 0;
		for(int vertex = 1; vertex <= v; vertex++)
		{
			for(int colour = 0; colour < k; colour++)
			{
				A[Apntr] = vertex + colours[colour];
				A_values[Apntr] = null;
				Apntr++;
			}
		}

/*		
		System.out.println("Running Satisfiability for 3 colours")
		String[][] L = { // ALOC |V|
						{"1a", "1b", "1c"}, 	
						{"2a", "2b", "1c"}, 
						{"3a", "3b", "3c"}, 
						{"4a", "4b", "4c"}, 
						{"5a", "5b", "5c"}, 
						{"6a", "6b", "6c"}, 
						// AMOC |v|*k*(k-1)/2 
		                {"1an", "1bn"}, {"1an", "1cn"}, {"1bn", "1cn"},
						{"2an", "2bn"},	{"2an", "2cn"}, {"2bn", "2cn"},
						{"3an", "3bn"},	{"3an", "3cn"}, {"3bn", "3cn"},
						{"4an", "4bn"},	{"4an", "4cn"}, {"4bn", "4cn"},
						{"5an", "5bn"},	{"5an", "5cn"}, {"5bn", "5cn"},
						{"6an", "6bn"},	{"6an", "6cn"}, {"6bn", "6cn"},
	
						// DCOL |E|*K where K = available colours
						{"1an", "2an"},	{"1bn", "2bn"},	{"1cn", "2cn"},
						{"2an", "3an"},	{"2bn", "3bn"},	{"2cn", "3cn"},
						{"3an", "1an"},	{"3bn", "1bn"},	{"3cn", "1cn"},
						{"1an", "4an"},	{"1bn", "4bn"},	{"1cn", "4cn"},
						{"4an", "5an"},	{"4bn", "5bn"},	{"4cn", "5cn"},
						{"5an", "6an"},	{"5bn", "6bn"},	{"5cn", "6cn"},
						{"6an", "4an"},	{"6bn", "4bn"},	{"6cn", "4cn"} };
		Boolean[][] L_values = {
						{null, null, null}, 	
						{null, null, null}, 
						{null, null, null}, 
						{null, null, null}, 
						{null, null, null}, 
						{null, null, null},
						// AMOC  
						{null, null},	{null, null},	{null, null},
						{null, null},	{null, null},	{null, null},
						{null, null},	{null, null},	{null, null},
						{null, null},	{null, null},	{null, null},
						{null, null},	{null, null},	{null, null},
						{null, null},	{null, null},	{null, null},
						// DCOL |E|*m where m = available colours
						{null, null},	{null, null},	{null, null},
						{null, null},	{null, null},	{null, null},
						{null, null},	{null, null},	{null, null},
						{null, null},	{null, null},	{null, null},
						{null, null},	{null, null},	{null, null},
						{null, null},	{null, null},	{null, null},
						{null, null},	{null, null},	{null, null}};
		String[] A = {"1a", "2a", "3a", "4a", "5a", "6a",
					  "1b", "2b", "3b", "4b", "5b", "6b",
					  "1c", "2c", "3c", "4c", "5c", "6c",};
		Boolean[] A_values = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
*/
		
		ReturnObject output = new ReturnObject(true, A_values);
		// Measure the time needed to find the solution
		long start = System.nanoTime();
		
		output = isSatisfiable(L, L_values, A, A_values, null);

		System.out.print("3-SAT returns \"" + output.out + "\" for " + k + " colours");
		if (output.out)
			System.out.print(" with values: " + Arrays.toString(A) + " = " + Arrays.toString(output.A_values));
					
		double time = (System.nanoTime()-start)/1000000.0;
		System.out.println("\nThe time needed to perform this analysis was: " + time + " ms.\n");
		Logger.logResults("3-SAT", inputfile , k, time);
	}
	
	//-------------------------------------------------------
	
	/** Method to check the index of a targeted value in a one-dimensional array (-1 if it is not found)
	 * @param array Array of elements where a concrete value will be checked 
	 * @param value Value to be found (or not) in the provided array
	 * @return The method returns the index in <i>array</i> where <i>value</i> was found, -1 otherwise (or if array is null)
	 */
	public int findIndex(String array[], String value) 
	{ 	  
		int out = -1;
		for(int i = 0; i < array.length; i++)
		{
			if(array[i].equalsIgnoreCase(value))
				out = i;
		}
		return out;
	}
	
	//-------------------------------------------------------
	
	/**
	 * Method to fill in all the instances of <i>primitive</i> in the array <i>L</i> with <i>value</i>
	 * @param L List of logical AND clauses
	 * @param L_values Known values of the logical AND clauses
	 * @param primitive Primitive to be updated
	 * @param value Value to be updated in the given <i>primitive</i>
	 * @return New version of <i>L_values</i>
	 */
	public Boolean[][] fillIn(String[][] L, Boolean[][] L_values, String primitive, Boolean value) 
	{
		// Iterate through L
		for(int i = 0; i < L.length; i++)
		{	
			for(int j = 0; j < L[i].length; j++)
			{
				if(L[i][j].equalsIgnoreCase(primitive))
					L_values[i][j] = value;
			}
		}
		return L_values;
	}
	
	//-------------------------------------------------------

	// Method to check if an array contains a given value
	public Boolean contains(int[] array, int value) 
	{
		Boolean found = false;
		for(int i: array)
		{
			if( i == value)
				found = true;
		}
		return found;
	}
	
	//-------------------------------------------------------
	
	// Method to check if there are any null values remaining
	public Boolean containsNull(Boolean[] L_values) 
	{
		Boolean contNull = true;
		for(int i = 0; i < L_values.length; i++)
		{
				if(L_values[i] == null)	
					contNull = false;
		}
		return contNull;
	}
	
	//-------------------------------------------------------
	
	public Boolean evaluate(Boolean[] L_Values)
	{
		Boolean flag = false;
		for(boolean b: L_Values)
		{
			if(b)
				flag = true;
		}
		return flag;
	}

	//-------------------------------------------------------
	
	/** 
	 * This method is the core of the branching algorithm. It goes through all the possible combinations of the input to look for 
	 * a solution to the 3-SAT problem.
	 * @param L List of logical AND clauses
	 * @param L_values Known values of the logical AND clauses
	 * @param A Array storing the unique values of the primitives to be evaluated
	 * @param A_values Current values for each of the unique primitives being evaluated
	 * @param next_p Primitive to be analysed
	 * @return The method returns a boolean value (whether the logical expression is satisfiable or not), and the current values of each primitive
	 */
	public ReturnObject isSatisfiable(String[][] L, Boolean[][] L_values, String[] A, Boolean[] A_values, String next_p) 
	{		
		// 3. If there are no clauses left (i.e. they have all been satisfied and thus discarded), then return TRUE.
		if (L.length == 0) 
		{
			  if(DEBUG) System.out.println("return: empty L");
			  return new ReturnObject(true, A_values);
		}

		// 1. Fill in truth values for all the primitives that already have been given True or False status.
		if (next_p != null) 
		{
			int next = findIndex(A, next_p); 
			L_values = fillIn(L, L_values, next_p, A_values[next]);
			L_values = fillIn(L, L_values, next_p+'n', !(A_values[next]));				// Also the negative ones
		    
			for (int a=0; a<A.length; a++) 												// Delete values from previous branches
			{
				if (A_values[a] == null) {
					L_values = fillIn(L, L_values, A[a], null);
					L_values = fillIn(L, L_values, A[a]+'n', null);
				}
			}
			
			if(DEBUG) System.out.println("A: " + Arrays.toString(A) + " = " + Arrays.toString(A_values));
			if(DEBUG) System.out.println("L: " + Arrays.deepToString(L));
			if(DEBUG) System.out.println("L_values: " + Arrays.deepToString(L_values));
		}	     

	    // 2. For each clause in L,
	    int[] toRemove = new int[L_values.length];		// Modified to dynamically adjust to the maximum length of array
	    for(int i: toRemove)
	    {
	    	i = -1;
	    }
	    int numIndicesToRemove = 0;
	    
	    for (int i=0; i<L.length; i++) 
	    {   	
	    	// a. If the clause has had all its primitives filled in, and the clause is false, then return FALSE
	    	if(containsNull(L_values[i]) && !evaluate(L_values[i]))
	    	{
	    		if(DEBUG) System.out.println("return: L_values[" + i + "] all False");
	    		return new ReturnObject(false, A_values);	
	    	}
	        
	    	/* b. If the clause has had all its primitives filled in, and the clause is true, 
	    	 * (or there are still primitives in the clause that have not yet been filled in, but we can see that it is definitely true),
	    	 * throw the clause away from L
	    	 */
	    	if(containsNull(L_values[i]) && evaluate(L_values[i])) 
	    	{
	    		toRemove[numIndicesToRemove++] = i;
	    		if(DEBUG) System.out.println("Index " + i + " to be removed");
	    	}
	    }
	    
	    // NOTE: We just consider 3 variables per clause but a different number of variables (or even varying number of variables per clause) could be considered
	    String[][] L_new = new String[L.length-numIndicesToRemove][3];
	    Boolean[][] L_values_new = new Boolean[L.length-numIndicesToRemove][3];
	    int pos = 0;
	    for (int i=0; i<L.length; i++) 
	    {
	    	if (!contains(toRemove, i)) 
	    	{
	    		L_new[pos] = L[i];
	    		L_values_new[pos] = L_values[i];
	    		pos++;
	    		if(DEBUG) System.out.println("Index " + i + " is kept");
	    	} else if(DEBUG) System.out.println(next_p + ": removing..." + Arrays.toString(L[i]) + " = " + Arrays.toString(L_values[i]));
	    }
	    if(DEBUG) System.out.println(Arrays.deepToString(L_new));
	    
	    // 3. If there are no clauses left (i.e. they have all been satisfied and thus discarded), then return TRUE.
	    if(L_values_new.length == 0) 
	    {
	    	if(DEBUG) System.out.println("return: empty L after removal");
	    	return new ReturnObject(true, A_values);
	    }
	    
	    // 4. Select a proposition p that still has 'don't know' status.
	    // NOTE: This part could be improved. For example, it could start looking for the variable which appears more often. 
	    int a = -1;
	    for (int i=0; i<A_values.length; i++) 
	    {
	    	if (A_values[i] == null) 
	    	{
	    		a = i;
	    	}
	    }
	    if (a!=-1) 
	    {
		    A_values[a] = true;
		    // 5. Boolean branch1 = isSatisfiable( L, A augmented with 'p is true' ); 
		    		    
		    String[][] br1_L = Arrays.copyOf(L, L.length);
		    Boolean[][] br1_L_values = Arrays.copyOf(L_values, L_values.length);
		    String[] br1_A = Arrays.copyOf(A, A.length);
		    Boolean[] br1_A_values = Arrays.copyOf(A_values, A_values.length);
		   		    
		    ReturnObject output = isSatisfiable(br1_L, br1_L_values, br1_A, br1_A_values, A[a]);
		    
		    
		    if (output.out) // 6. If branch1 == TRUE, return TRUE
		    {
		    	if(DEBUG) System.out.println("return: positive branch " + A[a]);
			    	return new ReturnObject(true, output.A_values);
		    }
			
		    // 7. Boolean branch2 = Solve isSatisfiable( L, A augmented with 'p is false');
		    A_values[a] = false;
		    		    
		    String[][] br2_L = Arrays.copyOf(L, L.length);
		    Boolean[][] br2_L_values = Arrays.copyOf(L_values, L_values.length);
		    String[] br2_A = Arrays.copyOf(A, A.length);
		    Boolean[] br2_A_values = Arrays.copyOf(A_values, A_values.length);
		   		    
		    ReturnObject output2 = isSatisfiable(br2_L, br2_L_values, br2_A, br2_A_values, A[a]);
		    
		    
		    if (output2.out) // 8. If branch2 == TRUE, return TRUE;
		    {
		    	if(DEBUG) System.out.println("return: negative branch " + A[a]);
		    		return new ReturnObject(true, output2.A_values);
		    }
	    }
	  
	    // 9. Return FALSE (i.e. both branches failed!)
	    return new ReturnObject(false, A_values);
	  }
}

