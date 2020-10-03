# Notes For Proj 1-1

## Solving Graph Coloring Problem Using an Enhanced Binary Dragonfly Algorithm

>"The two most constructive algorithms employed to solve the GCP are the recursive largest first algorithm (RLF) proposed by Leighton (Leighton, 1979) and the largest saturation degree algorithm (DSATUR) developed by Brélaz (Brélaz, 1979). These methods are based on greedy approach which colors the nodes of the graph one at time using a predefined greedy function. Recently, these approaches have been used to generate initial solutions for advanced evolutionary algorithms.

>Local search heuristics have been widely proposed to solve the GCP. Tabu Search Algorithm proposed by Hertz and de Werra (Hertz & de Werra, 1987) was the first local search algorithm applied to solve the graph coloring problem. It is called TABUCOL and has been improved by several researchers and used as a subcomponent of more graph coloring algorithms."

*International Journal of Swarm Intelligence Research (IJSIR) - DOI:10.4018/IJSIR.2019070102*

//--------------------------------------------------

## The Greedy Algorithm (or first fit algorithm)

1. Pick a vertex V

2. Colour it with the next legal colour

3. Move to next vertex

### Pseudocode

	for(Vertex V: Set_Of_Vertecies)
	{
		While(!allColoured())
		{
			if(legalColouring())		
			{
				setColour(Vertex V, Colour current);
			}		
			else
			{
				incrementColour();
			}
		}
	}
	
	


 **References:**
 *Dr Rhyd Lewis - Cardiff University School of Mathematics - https://www.youtube.com/watch?v=L2csXWQMsNg ; Accessed 03-10-2020*

//--------------------------------------------------

## DSATUR

1. List all vertecties in order of frequency (High to low)

2. For the first vertex, assign colour 0.

3. For all subsequent vertecies, pick the vertex with the most adjacent colours, iterate through the colours until a viable one is found
	
### Field variables required
	
	Private vertex[] vertecies			// An array of vertex objects 
	
	
### Psuedocode

	while(vertecies.length != 0)
	{
		vertex V = vertecies[nextMostSaturated()]; 	// pick the next highest stauration	
		while(!legalColouring())			// Iterate through the colours until a legal colouring can be found
		{
			colour++;
		}
		setColour(Vertex V, Colour current);		// Set the selected colour to V
		orderBySaturation();				// Order by saturation 
	}


### Methods required

	Boolean legalColouring(int colour)		// check that the colouring doesn't invalidate the rules	
							// Return true or false

	void orderByFrequency()				// order vertecies[] by highest frequency first

	void orderBySaturation()			// order vertecies[] by saturation i.e. the vertex that is next to the most coloured vertecies is highest

	int nextMostSaturated(vertex[] vertecies)	// pick the next vertex in the array that is not coloured, but is highest in saturation
							// Return an int index of the vertex
	
//--------------------------------------------------

## Recursive Largest First Algorithm

1. List all the vertex frequencies largest to smallest

2. Starting at the largest node, colour it and recursively call the colouring function on each adjacent node, incrementing the colour number

3. At the base case (no more uncoloured nodes) Return the chromatic number, comparing over the backpropogation.

### Field variables required

### Psuedocode

	public int colour(vertex[] vertecies, int vertex, int colour)
	{
		vertex[] vtxs = Array.copyOf(vertecies);				// Create a copy of the graph
		
		if(pickNextVertex() == -1)						// If the vertex is the last to be coloured, return the chromatic number
			return colour;
		
		while(!legal(colour)							// Pick the next legal colour
		{
			colour++;
		}
		
		int numberOfSubGraphs = howManyAdjacentVertecies();			// Calculate all of the possible next vertecies (using DSATUR)
		vtxs[][] subGraphs = new vertex[numberOfSubGraphs][vertecies.length];	// Create a matrix of each sub Graph
	
		int[] chromaticNumber = new int[numberOfSubGraphs];			// Create a list to hold the returned values from the sub graphs
		for(int i; i < subGraphs.length; i++)
		{
			chromaticNumber[i] = colour(subGraphs[i], colour);		// Recursive call
		}
		
		int lowest = 0;
		for(int i: chomaticNumber)
		{
			if( i < lowest)
				lowest = i;
		}
		return lowest;
	}

### Methods required

**References:**
New Methods to Color the Vertices of a Graph - D.Brelaz - April 1979 - file:///tmp/mozilla_lsd0/Brelaz79.pdf

*A new efficient RLF-like Algorithm for the Vertex Coloring Problem - M.Adegbindin, A.Hertx, M, Bell ̈ıche - November 2, 2015  - file:///tmp/mozilla_lsd0/RLFPaper.pdf*

*Discrete Optimization - M.Chiarandini - file:///tmp/mozilla_lsd0/dm841-lec13.pdf *

//--------------------------------------------------

## TABUCOL

//--------------------------------------------------

## Bounding:

	Upper bounds: for every connected graph G, the upper bound chromatic number X(G) <= |G| + 1

	Lower bound: 




