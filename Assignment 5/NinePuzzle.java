/* NinePuzzle.java
   CSC 225 - Fall 2014
   Lab - Template for the 9-puzzle

   This template includes some testing code to help verify the implementation.
   Input boards can be provided with standard input or read from a file.

   To provide test inputs with standard input, run the program with
	java NinePuzzle
   To terminate the input, use Ctrl-D (which signals EOF).

   To read test inputs from a file (e.g. boards.txt), run the program with
    java NinePuzzle boards.txt

   The input format for both input methods is the same. Input consists
   of a series of 9-puzzle boards, with the '0' character representing the
   empty square. For example, a sample board with the middle square empty is

    1 2 3
    4 0 5
    6 7 8

   And a solved board is

    1 2 3
    4 5 6
    7 8 0

   An input file can contain an unlimited number of boards; each will be
   processed separately.

   Fang Dong - 18/11/2014
*/

//import java.util.Queue;
import java.util.*;
import java.io.File;

public class NinePuzzle {


	//The total number of possible boards is 9! = 1*2*3*4*5*6*7*8*9 = 362880
	public static final int NUM_BOARDS = 362880;


	/*  SolveNinePuzzle(B)
		Given a valid 9-puzzle board (with the empty space represented by the
		value 0),return true if the board is solvable and false otherwise.
		If the board is solvable, a sequence of moves which solves the board
		will be printed, using the printBoard function below.
	*/
	public static boolean SolveNinePuzzle(int[][] B){


		int[][] adjacencyList = BuildNinePuzzleGraph();

		System.out.println( "Attempting to find a path for board: " );
		printBoard(B);
		int v = getIndexFromBoard(B);

		int[][] goalBoard = new int[][] {
			{ 1, 2, 3 },
			{ 4, 5, 6 },
			{ 7, 8, 0 }
		};
		int goal = getIndexFromBoard(goalBoard);


		if (findPath(v,goal,s))
			return true;

		return false;
	}

	private static boolean findPath(int origin, int goal, int[][] adjList) {

		Stack s = new Stack();




	}

	private static boolean doBFS (int curr, int goal, int[][] adjList, Stack s) {


	}
	/*	This builds the adjacency list for the nine-puzzle graph.
		The list is stored in a 2D array with four columns, each
		column corresponding to one of the four cardinal directions
		(up, down, left, right), as the blank space could possibly
		be moved in any of those four directions.
	*/
	public static int[][] BuildNinePuzzleGraph(){

		// adjacency list
		int[][] adjacencyList=new int[NUM_BOARDS][4];

		// initialize the adjacency list with negative values
		for (int i=0;i<adjacencyList.length;i++)
			for (int j=0;j<adjacencyList[0].length;j++)
				adjacencyList[i][j]=-1;

		// initialize the adjacency list with the indexes of adjacent boards
		for (int i = 0; i < NUM_BOARDS; i++) {
			int[][] currBoard = getBoardFromIndex(i);
			int blank = findBlank(currBoard);

			// Determine which moves are possible for the current board
			boolean up = false;
			boolean down = false;
			boolean left = false;
			boolean right = false;
			if (blank > 2) up = true;
			if (blank%3 != 0) left = true;
			if (blank%3 != 2) right = true;
			if (blank < 6) down = true;

			// Determine which indexes match the possible moves
			if (up)
				adjacencyList[i][0] = getIndexFromBoard(swapBlank(currBoard, blank, "up"));
			if (down)
				adjacencyList[i][1] = getIndexFromBoard(swapBlank(currBoard, blank, "down"));
			if (left)
				adjacencyList[i][2] = getIndexFromBoard(swapBlank(currBoard, blank, "left"));
			if (right)
				adjacencyList[i][3] = getIndexFromBoard(swapBlank(currBoard, blank, "right"));
		}

		return adjacencyList;
	}

	/*	This will return a single integer representing the location of the
		blank space represented by 0 in the given board. For example, if the
		0 is located in the middle of the board, findBlank() will return 4
	*/
	private static int findBlank(int[][] B) {
		int blankIndex = 0;
		for (int i = 0; i <= 3; i++) {
			for (int j = 0; j <= 3; j++) {
				if ( B[i][j] == 0)
					return blankIndex;
				else blankIndex++;
			}
		}
	}

	/*	This will return the board corresponding to the given move command.
		For example, moving the blank upwards involves swapping the value in
		that cell of the curr array with the one directly above it. To call
		this method, the available directions must have already been checked.
	*/
	private static int[][] swapBlank(int[][] curr, int blank, String dir) {

		if (dir.equals("up")) {
			int dest = blank-3;
			temp = curr[dest/3][dest%3];
			curr[dest/3][dest%3] = 0;
			curr[blank/3][blank%3] = temp;
		}

		if (dir.equals("down")) {
			int dest = blank+3;
			temp = curr[dest/3][dest%3];
			curr[dest/3][dest%3] = 0;
			curr[blank/3][blank%3] = temp;
		}

		if (dir.equals("left")) {
			int dest = blank-1;
			temp = curr[dest/3][dest%3];
			curr[dest/3][dest%3] = 0;
			curr[blank/3][blank%3] = temp;
		}

		if (dir.equals("right")) {
			int dest = blank+1;
			temp = curr[dest/3][dest%3];
			curr[dest/3][dest%3] = 0;
			curr[blank/3][blank%3] = temp;
		}
	}


	/*  printBoard(B)
		Print the given 9-puzzle board. The SolveNinePuzzle method above should
		use this method when printing the sequence of moves which solves the input
		board. If any other method is used (e.g. printing the board manually), the
		submission may lose marks.
	*/
	public static void printBoard(int[][] B){
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++)
				System.out.printf("%d ",B[i][j]);
			System.out.println();
		}
		System.out.println();
	}


	/* Board/Index conversion functions
	   These should be treated as black boxes (i.e. don't modify them, don't worry about
	   understanding them). The conversion scheme used here is adapted from
		 W. Myrvold and F. Ruskey, Ranking and Unranking Permutations in Linear Time,
		 Information Processing Letters, 79 (2001) 281-284.
	*/
	public static int getIndexFromBoard(int[][] B){
		int i,j,tmp,s,n;
		int[] P = new int[9];
		int[] PI = new int[9];
		for (i = 0; i < 9; i++){
			P[i] = B[i/3][i%3];
			PI[P[i]] = i;
		}
		int id = 0;
		int multiplier = 1;
		for(n = 9; n > 1; n--){
			s = P[n-1];
			P[n-1] = P[PI[n-1]];
			P[PI[n-1]] = s;

			tmp = PI[s];
			PI[s] = PI[n-1];
			PI[n-1] = tmp;
			id += multiplier*s;
			multiplier *= n;
		}
		return id;
	}

	public static int[][] getBoardFromIndex(int id){
		int[] P = new int[9];
		int i,n,tmp;
		for (i = 0; i < 9; i++)
			P[i] = i;
		for (n = 9; n > 0; n--){
			tmp = P[n-1];
			P[n-1] = P[id%n];
			P[id%n] = tmp;
			id /= n;
		}
		int[][] B = new int[3][3];
		for(i = 0; i < 9; i++)
			B[i/3][i%3] = P[i];
		return B;
	}


	public static void main(String[] args){
		/* Code to test your implementation */
		/* You may modify this, but nothing in this function will be marked */


		Scanner s;

		if (args.length > 0){
			//If a file argument was provided on the command line, read from the file
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			//Otherwise, read from standard input
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}

		int graphNum = 0;
		double totalTimeSeconds = 0;

		//Read boards until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading board %d\n",graphNum);
			int[][] B = new int[3][3];
			int valuesRead = 0;
			for (int i = 0; i < 3 && s.hasNextInt(); i++){
				for (int j = 0; j < 3 && s.hasNextInt(); j++){
					B[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < 9){
				System.out.printf("Board %d contains too few values.\n",graphNum);
				break;
			}
			System.out.printf("Attempting to solve board %d...\n",graphNum);
			long startTime = System.currentTimeMillis();
			boolean isSolvable = SolveNinePuzzle(B);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;

			if (isSolvable)
				System.out.printf("Board %d: Solvable.\n",graphNum);
			else
				System.out.printf("Board %d: Not solvable.\n",graphNum);
		}
		graphNum--;
		System.out.printf("Processed %d board%s.\n Average Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>1)?totalTimeSeconds/graphNum:0);

	}

}
