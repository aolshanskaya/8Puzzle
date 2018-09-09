import java.util.*;

/**
 * Project: 8-Puzzle
 * 
 * This project takes scrambled 8-puzzle boards and solves them using an A* search with two heuristics. 
 * 
 * @author Anna Olshanskaya
 */

public class Main {
	private static UI ui;
	private static final int NUM_BULK_CASES = 400;
	
	/**
	 * Triggers welcome message in UI and based on the decision received directs how to proceed. 
	 * 
	 * If welcome message returns an array of length >1 then a custom puzzle is solved verbosely. 
	 * If an array of length 1 is returned:
	 * 		if the value is 2, a solvable random puzzle is generated and solved verbosely.
	 * 		if the value is 3, NUM_BULK_CASES random puzzles are generated and solved then the stats are displayed. 
	 */
	public static void main(String[] args) {
		PuzzleSolver.getPuzzleSolver();
		PuzzleGenerator.getPuzzleGenerator();
		ui = UI.getUI();
		
		int[] to_do = ui.welcomeMessage();
		if(to_do.length == 1) {
			if(to_do[0] == 2) {
				int[] puzzle = PuzzleGenerator.generateRandomPuzzle();
				while(!PuzzleSolver.isSolvable(puzzle))
					puzzle = PuzzleGenerator.generateRandomPuzzle();
				solveAndShow(puzzle);
			}else if(to_do[0] == 3) {
				bulkTest(NUM_BULK_CASES);
			}
		}else {
			solveAndShow(to_do);
		}
	}
	
	/**
	 * Feeds the ui method printSinglePuzzleStats an array of stats about the solution of the given puzzle.  
	 */
	private static void solveAndShow(int[] initial_state) {
		UI.printSinglePuzzleStats(PuzzleSolver.solvePuzzle(initial_state, true));
	}
	
	/**
	 * Option 3.
	 * Runs a given number of test cases and prints out the stats.
	 */
	private static void bulkTest(int num_tests) {
		HashMap<Integer , Double[]> stats_by_depth = getRandomCases(num_tests);
		UI.printStats(stats_by_depth);
	}
	
	/**
	 * Creates a hashmap of stats for solved random problems. 
	 * Key = depth ; Value = [number of cases , total h1 cost , total h2 cost , total h1 runtime , total h2 runtime]
	 */
	private static HashMap<Integer , Double[]> getRandomCases(int num_cases){
		HashMap<Integer , Double[]> stats_by_depth = new HashMap<>();
		
		for(int i = 0 ; i < num_cases ; i++) {
			//result format: [solution depth , h1 cost , h2 cost , h1 runtime , h2 runtime]
			double[] random_result = {-1,-1,-1,-1,-1};
			while(random_result[0] < 0) {
				int[] puzzle = PuzzleGenerator.generateRandomPuzzle();
				while(!PuzzleSolver.isSolvable(puzzle))
					puzzle = PuzzleGenerator.generateRandomPuzzle();
				random_result = PuzzleSolver.solvePuzzle(puzzle , false);
			}
			
			int depth = (int)random_result[0];
			//new depth stat
			if(!stats_by_depth.containsKey(depth)) {
				Double[] stats = {1.0 , random_result[1] , random_result[2] , random_result[3] , random_result[4]};
				stats_by_depth.put(depth, stats);
			}
			//recurring depth stat
			else {
				Double[] stats = stats_by_depth.get(Integer.valueOf((int)random_result[0]));
				stats[0] += 1.0;
				stats[1] += random_result[1];
				stats[2] += random_result[2];
				stats[3] += random_result[3];
				stats[4] += random_result[4];
				stats_by_depth.replace(Integer.valueOf((int)random_result[0]), stats);
			}
		}
		return stats_by_depth;
	}
}
