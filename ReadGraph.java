import java.io.*;
import java.util.*;

class ColEdge {
	int u;
	int v;
	int colU = -1;
	int colV = -1;

	public boolean legal() {
		boolean legal = true;
		if (colV != -1 && colV == colU)
			legal = false;
		return legal;
	}
}

// -------------------------------------------------------

public class ReadGraph {

	public final static boolean DEBUG = false;
	public final static String COMMENT = "//";

	// -------------------------------------------------------

	public static void main(String args[]) {
		ALGORITHMS.add("g");
		ALGORITHMS.add("bt");
		ALGORITHMS.add("d");
		ALGORITHMS.add("bf");
		ALGORITHMS.add("s");
		ALGORITHMS.add("*");
		
		String inputfile = (String) request("File", "Enter the path to the graph file or the graph id");
		String alg = (String) request("Algorithm", "Select an algorithm:\nGreedy:\t\tg\nBacktracking:\tbt\nDSatur:\t\td\nBrute force:\tbf\n3-SAT:\t\ts\nAutomatic:\t*");
		int times = (int) request("Integer", "Run how many times?");
		if (times <= 1) times = 1;
		if (times > 100) times = 100;
		int vx = 0;
		if (alg.equals("s")) vx = (int) request("Integer", "How many colors to check?\n0 = Until valid");
		if (vx <= 1) vx = 0;
		if (vx > 100) vx = 100;

		boolean seen[] = null;

		int n = -1; // ! n is the number of vertices in the graph
		int m = -1; // ! m is the number of edges in the graph
		ColEdge e[] = null; // ! e will contain the edges of the graph

		try {
			FileReader fr = new FileReader(inputfile);
			BufferedReader br = new BufferedReader(fr);

			String record = new String();

			// ! The first few lines of the file are allowed to be comments, staring with a
			// // symbol.

			while ((record = br.readLine()) != null) {
				if (record.startsWith("//"))
					continue;
				break; // Saw a line that did not start with a comment -- time to start reading the
						// data in!
			}

			if (record.startsWith("VERTICES = ")) {
				n = Integer.parseInt(record.substring(11));
				if (DEBUG)
					System.out.println(COMMENT + " Number of vertices = " + n);
			} else {
				System.out.println("Error! Problem reading file " + inputfile);
				System.exit(0);
			}

			seen = new boolean[n + 1];
			record = br.readLine();

			if (record.startsWith("EDGES = ")) {
				m = Integer.parseInt(record.substring(8));
				if (DEBUG)
					System.out.println(COMMENT + " Expected number of edges = " + m);
			} else {
				System.out.println("Error! Problem reading file " + inputfile);
				System.exit(0);
			}

			e = new ColEdge[m];

			for (int d = 0; d < m; d++) {
				if (DEBUG)
					System.out.println(COMMENT + " Reading edge " + (d + 1));
				record = br.readLine(); // Pull a new string from the file
				String data[] = record.split(" "); // Split into array of strings at each " "
				if (data.length != 2) // If data[] has more than two points (bad read)
				{
					System.out.println("Error! Malformed edge line: " + record);
					System.exit(0);
				}
				e[d] = new ColEdge();

				e[d].u = Integer.parseInt(data[0]);
				e[d].v = Integer.parseInt(data[1]);

				seen[e[d].u] = true;
				seen[e[d].v] = true;

				if (DEBUG)
					System.out.println(COMMENT + " Edge: " + e[d].u + " " + e[d].v);
			}

			String surplus = br.readLine();
			if (surplus != null) {
				if (surplus.length() >= 2)
					if (DEBUG)
						System.out.println(
								COMMENT + " Warning: there appeared to be data in your file after the last edge: '"
										+ surplus + "'");
			}
		}

		catch (IOException ex) {
			// catch possible io errors from readLine()
			System.out.println("Error! Problem reading file " + inputfile);
			System.exit(0);
		}

		for (int x = 1; x <= n; x++) {
			if (seen[x] == false) {
				if (DEBUG)
					System.out.println(COMMENT + " Warning: vertex " + x
							+ " didn't appear in any edge : it will be considered a disconnected vertex on its own.");
			}
		}

		ColEdge[] eCopy = copyEdges(e);
		switch (alg) {
			case "s":		// 3SAT
			boolean solved = false;
			long start = System.nanoTime();
			if (vx <= 1) {
				int i = 2;
				while (!solved) {
					SAT3 sat3 = new SAT3(e, m, n, i, inputfile);
					solved = sat3.run();
					if (!solved) i++;
				}
				System.out.println("Colors needed: " + i);
				double time = (System.nanoTime()-start)/1000000.0;
				Logger.logResults("SAT-3", inputfile, i, time);
			} else {
				SAT3 sat3 = new SAT3(e, m, n, vx, inputfile);
				solved = sat3.run();
				System.out.println("Values returned: " + solved);
			}
			break;
			case "d":		// DSatur
			for (int i = 0; i < times; i++) {
				DSATUR dsatur = new DSATUR();
				eCopy = copyEdges(e);
				dsatur.run(eCopy, m, n, inputfile);
			}
			break;

			case "g":		// Greedy
			System.out.println("Greedy: Still to be implemented");
			break;
			case "bt":		// Backtracking
			System.out.println( "mBacktracking: Still to be implemented");
			break;
			case "bf":
			BruteForceNoPruningThreaded b = new BruteForceNoPruningThreaded();
			for (int i = 0; i < times; i++) {
				b.run(e, n, inputfile);
			}
			break;
			default:
			System.out.println( "Automatic: Still to be implemented");
		}
	}
	static final ArrayList<String> ALGORITHMS = new ArrayList<>();

	/**
	 * Asks the user for a specific type of input
	 * @param type A type: {@code Integer}, {@code String}, {@code Algorithm}, or {@code File}
	 * @param string The prompt given to the user
	 * @return When the input is valid, the input
	 */
	private static Object request(String type, String string) {
		System.out.println(string);
		Scanner in = new Scanner(System.in);
		Object output = null;
		while (output == null || (type == "Algorithm" && !ALGORITHMS.contains(output)) || (type == "File" && !fileExists((String) output))) {
			String inp = in.nextLine();
			try {
				if (type == "Integer") output = Integer.parseInt(inp);
				if (type == "String" || type == "Algorithm" || type == "File") {
					if (type == "File" && !inp.contains(".txt")) {
						if (inp.length() == 1) inp = "graph0" + inp + "_2020.txt";
						else inp = "graph" + inp + "_2020.txt";
					}
					output = inp;
				}
			} catch (Exception e) {
				System.out.println( inp + " is not a valid " + type );
			}
			if (type == "Algorithm" && !ALGORITHMS.contains(output) || (type == "File" && !fileExists((String) output))) System.out.println( inp + " is not a valid " + type );
		}
		return output;
	}

	/**
	 * Checks is a file exists
	 * @param path Path to file
	 * @return {@code true} if the file exists
	 * {@code false} if the file does not exist
	 */
	private static boolean fileExists(String path){
		File f = new File(path);
		return f.exists();
	}

	/**
	 * Make a deep copy of a ColEdge array
	 * 
	 * @param e - An array of edges
	 * @return - A deep copy of the array
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
