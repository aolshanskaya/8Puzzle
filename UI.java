import java.util.*;

/**
 * The UI class interacts with the user, prompting them for how to proceed running the program and then returning their
 * selections back to inform the rest of the program. 
 */
public class UI {
	private static UI ui = null;
	private Scanner kb;
	private UI() {
		kb = new Scanner(System.in);
	}
	public static UI getUI(){
		if(ui == null)
			ui = new UI();
		return ui;
	}

	/**
	 * Displays welcome message and triggers the user selection prompt, promptUserHowToSolve(), returning the selection. 
	 */
	public int[] welcomeMessage() {
		System.out.println("Welcome to the 8-Puzzle solver.");
		return promptUserHowToSolve();
	}
	
	/**
	 * Prompts user to choose how to execute the program from the main menu and acts accordingly. 
	 * Returns an array indicating the user's choice.
	 * 		If user chooses (1) returns an array representing the custom puzzle they entered. 
	 * 		If user chooses (2) or (3) returns an array with length of 1 indicating which option was selected.
	 * 		If user chooses (4) triggers goodbyeMessage() and exits the program. 
	 */
	public int[] promptUserHowToSolve() {
		String answer;
		int decision;
		
		do {
			mainMenu();
			answer = kb.next();
			try {
				decision = Integer.parseInt(answer);
			}catch(Exception e){
				decision = 0;
			}
			if(decision < 1 || decision > 4)
				System.out.println("That was unclear, please try again.");
		} while(decision < 1 || decision > 4);
		
		int[] answ = {-1};
		if(decision == 1) {
			return testCustomInput();
		}else if(decision == 2){
			answ[0] = 2;
		}else if(decision == 3) {
			answ[0] = 3;
		}else{
			goodbyeMessage();
		}
		return answ;
	}
	
	/**
	 * 
	 */
	public static void printStateStats(int depth , int heuristic , int eval) {
		System.out.println("depth: " + depth);
		System.out.println("heuristic: " + heuristic);
		System.out.println("evaluation: " + eval);
	}
	
	/**
	 * Prints which heuristic is being used. 
	 */
	public static void printHeuristicChoice(int h) {
		if(h == 1)
			System.out.println("\nSearch using the first heuristic:\n");
		else
			System.out.println("\nSearch using the second heuristic:\n");
	}
	
	/**
	 * Takes in an array with length of 5.
	 * 		Result[0] is the solution depth. 
	 * 		The following values are heuristic costs and runtimes.
	 * Displays the stats if the puzzle was solvable and a message if not. 
	 */
	public static void printSinglePuzzleStats(double[] result) {
		if(result[0] < 0) {
			System.out.println("This puzzle is unsolvable! Try again.");
		}else {
			System.out.println("--------------------------");
			System.out.println("Depth: " + result[0]);
			System.out.println("h1 Cost: " + result[1] + " states expanded");
			System.out.println("h2 Cost: " + result[2] + " states expanded");
			System.out.println("h1 Runtime: " + result[3] + " milliseconds");
			System.out.println("h2 Runtime: " + result[4] + " milliseconds");
		}
	}
	
	/**
	 * Prints out stats from the hashmap returned by the getRandomCases() method in Main.
	 * This method is called iff the user chooses to run bulk testing. 
	 */
	public static void printStats(HashMap<Integer , Double[]> stats_by_depth) {
		System.out.print("DEPTH \t NUMBER OF TESTS \t COST USING H1 \t COST USING H2 \t RUNTIME H1 \t RUNTIME H2");
		
		for(Integer depth : stats_by_depth.keySet()) {
			Double[] stats = stats_by_depth.get(depth);
			
			System.out.print("\n" + depth + "\t" + stats[0].intValue() + "\t\t");
			for(int i = 1 ; i < stats.length ; i++) {
				stats[i] /= stats[0];
				System.out.print(stats[i] + "\t\t");
			}
			
		}
	}
	
	/**
	 * Displays the 8-puzzle in a visually appealing manner. 
	 */
	public static void printPuzzle(int[] puzzle) {
		System.out.println("+ - - - +");
		System.out.println("| " + puzzle[0] + " " + puzzle[1] + " " + puzzle[2] + " |");
		System.out.println("| " + puzzle[3] + " " + puzzle[4] + " " + puzzle[5] + " |");
		System.out.println("| " + puzzle[6] + " " + puzzle[7] + " " + puzzle[8] + " |");
		System.out.println("+ - - - +");
	}
	
	/**
	 * Displays the main menu for the user to choose what they want to program to do. 
	 */
	private void mainMenu() {
		System.out.println("Please choose how you'd like to proceed:");
		System.out.println("1) Enter my own puzzle to solve and see solution.");
		System.out.println("2) Generate a random puzzle and see solution.");
		System.out.println("3) Generate an array of 400 random puzzles and output stats. (This will take a while)");
		System.out.println("4) Exit.");
	}
	
	/**
	 * Called if the user decides to solve their own puzzle. 
	 * 
	 * Reads in the user's custom 8-puzzle configuration, prints the correlating puzzle and returns an array containing the board state.  
	 */
	private int[] testCustomInput() {
		int[] user_input = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
		int[] unique_values = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
		
		boolean valid_puzzle = true;
		do{
			valid_puzzle = true;
			System.out.println("Test a custom 8-puzzle!\nEnter the numbers 0-8 in this format\n1 2 4 0 5 6 8 3 7 below:");
			for(int i = 0; i < 9 ; i++) {
				int tile = -1;
				try {
					tile = kb.nextInt();
					if(tile < 0 || tile > 8) {
						valid_puzzle = false;
						System.out.println("Oops! Please only enter numbers between 0 and 8!");
						break;
					}else if(unique_values[tile] > 0) {
						valid_puzzle = false;
						System.out.println("Oops! Please make sure your numbers are unique!");
						break;
					}
				}catch(InputMismatchException e){
					System.out.println("Oops! Please make sure you only input integer values!");
					valid_puzzle = false;
					break;
				}
				user_input[i] = tile;
				unique_values[tile] = 1;
			}
			kb.reset();
		}while(valid_puzzle == false);
		
		System.out.println("This is the puzzle you entered:");
		printPuzzle(user_input);
		return user_input;
	}
	
	/**
	 * Exits if user chose not to have the program execute anything. 
	 */
	private void goodbyeMessage() {
		kb.close();
		System.out.println("Good-Bye!");
	}
}
