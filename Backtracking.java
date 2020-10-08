public class Backtracking {

public static void main (String[] args) {

	//This is the variable of the vertex
    int v = 1; 

    //Here I gave random values to the graph 
    int [][] graph  = { {0, 1, 1, 1},
                        {1, 0, 1, 0},
                        {1, 1, 0, 1}, 
                        {0, 1, 0, 0}, };

    //This is the colour array
    //In this case 4 is the number of verices of the graph
    int [] colour = new int[4];
    colour[0] = 1;

    //Here the result is printed 
    graphColour(v, colour, graph);

}

//this is the method that actually color the vertices 
//v is the vertex that we are going to colour 
//colour[v] is the array that holds the current colour at each vertex
public static void graphColour(int v, int[] colour, int[][] graph) {

	// cr it the colour that needs to be tested
    for (int cr = 1; cr <= graph.length; cr++) {

        //Here I call the isAvailble method in order to make sure that a color can be used
        if (isAvailable (v, colour, cr, graph)) {
        	//a colour is assigned to each index of the array
            colour[v] = cr;

            if ((v+1) < graph.length) {
                /*This is the recursive call of the method 
                that goes through all the other verices that still need to be coloured*/
                graphColour(v+1, colour, graph); }

        } else { //if all vertices are coulored

            //the recursive call stops when all the vetices are coloured
            for (int i = 0; i < colour.length; i++) {
            //Here I print all the combinations of colours 
            	System.out.println(colour[i]);

        	}
	    }
    }
   return; 
}

	
//This method checks if that colour is available to place
//int v is the vertex to be coloured 
//int colour is a possible colour that can be associated to vertex k 
//
public static boolean isAvailable (int v, int[] colour, int cr, int[][] graph) {

    for (int i = 0; i < graph.length; i++) {

        //Here I check whether the vertex v is adjacent to the vertex i that the for loop is checking 
        if (graph[v][i] == 1 && cr == colour[i]) {

            /*I return false if the colour of the two adjacent vertices match 
            because I cannot use that colour*/ 
            return false; 
        }
    }
        //I return true if it is safe to place that colour 
        return true; 
    }

}
