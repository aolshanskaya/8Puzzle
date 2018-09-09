import java.util.*;

public class PuzzleSolver {
	private static PuzzleSolver ps = null;
	private static int step_cost;
	private PuzzleSolver() {
		UI.getUI();
		step_cost = 1;
	}
	public static PuzzleSolver getPuzzleSolver() {
		if(ps == null)
			ps = new PuzzleSolver();
		return ps;
	}
	
	/**
	 * Returns whether or not the puzzle is solvable. 
	 */
	public static boolean isSolvable(int[] puzzle) {
		State p = new State(puzzle);
		return p.isSolvable();
	}
	
	/**
	 * Uses A* search to solve scrambled 8-Puzzle.
	 * Returns array with [solution depth , cost of A* using h1 , cost of A* using h2]
	 * Cost is determined by number of nodes explored with each heuristic.
	 * If puzzle cannot be solved returns invalid array [-1,-1,-1].
	 */
	public static double[] solvePuzzle(int[] initial_state , boolean show) {
		double[] results = {-1 , -1 , -1 , -1 , -1};
		State initial = new State(initial_state);
		if(!initial.isSolvable())
			return results;
		
		double start = System.currentTimeMillis();
		List<State> expanded_with_h1 = solver(initial , 1 , show);
		double fin = System.currentTimeMillis();
		results[3] = fin - start;
		
		start = System.currentTimeMillis();
		List<State> expanded_with_h2 = solver(initial , 2 , show);
		fin = System.currentTimeMillis();
		results[4] = fin - start;
		
		results[2] = expanded_with_h2.size();
		results[1] = expanded_with_h1.size();
		results[0] = expanded_with_h2.get((int)(results[2]-1)).getCost();
		
		return results;
	}
	
	/**
	 * Solves using A* search using given heuristic to sort frontier. 
	 */
	private static List<State> solver(State initial , int h , boolean show){
		List<State> frontier = new ArrayList<>();
		List<State> explored = new Stack<>();
		frontier.add(initial);
		if(show) {
			UI.printHeuristicChoice(h);
		}
		
		while(!frontier.isEmpty()) {
			State checking = frontier.remove(0);
			while(explored.contains(checking))
				checking = frontier.remove(0);
			
			if(show) {
				int f = checking.getf2();
				if(h == 1)
					f = checking.getf1();
				
				UI.printPuzzle(checking.toBoardArray());
				UI.printStateStats(checking.getCost() , (f-checking.getCost()) , f);
			}
			
			if(checking.isGoal()) {
				explored.add(checking);
				return explored;
			}
			
			/**
			 * Adds new child states to front of frontier so if they have the same evaluation value as other states,
			 * those with greater depth and lower heuristic values will be processed first. 
			 */
			List<State> children = getChildStates(checking);
			
			if(checking != initial) {
				for(State child : children) {
					if(child.getZeroIndex() == checking.getParentState().getZeroIndex()) {
						children.remove(child);
						break;
					}
				}
			}
			
			frontier.addAll(0 , children);
			frontier.sort(new Comparator<>() {
				public int compare(State s1 , State s2) {
					if(h == 1) {
						return s1.getf1() - s2.getf1();
					}
					return s1.getf2() - s2.getf2();
				}
			});
			explored.add(checking);
		}
		return explored;
	}
	
	/**
	 * Locates 0 on board and accordingly finds possible children. 
	 */
	private static List<State> getChildStates(State parent){
		int[] parent_board = parent.toBoardArray();
		
		if(parent_board[4] == 0) {
			return moveFromCenter(parent);
		}
		else if(parent_board[0] == 0 || parent_board[2] == 0 || parent_board[6] == 0 || parent_board[8] == 0) {
			return moveFromCorner(parent);
		}
		return moveFromSide(parent);
	}
	
	/**
	 * Returns 2 possible children when moving 0 from a corner.
	 */
	private static List<State> moveFromCorner(State parent) {
		List<State> children = new Stack<>();
		
		if(parent.toBoardArray()[0] == 0) {
			children.add(new State( getChildBoardArray(parent.toBoardArray(),0,1) , parent.getCost()+step_cost , parent));
			children.add(new State( getChildBoardArray(parent.toBoardArray(),0,3) , parent.getCost()+step_cost , parent));
		}
		else if(parent.toBoardArray()[2] == 0) {
			children.add(new State( getChildBoardArray(parent.toBoardArray(),2,1) , parent.getCost()+step_cost , parent));
			children.add(new State( getChildBoardArray(parent.toBoardArray(),2,5) , parent.getCost()+step_cost , parent));
		}
		else if(parent.toBoardArray()[6] == 0) {
			children.add(new State( getChildBoardArray(parent.toBoardArray(),6,3) , parent.getCost()+step_cost , parent));
			children.add(new State( getChildBoardArray(parent.toBoardArray(),6,7) , parent.getCost()+step_cost , parent));
		}
		else {
			children.add(new State( getChildBoardArray(parent.toBoardArray(),8,5) , parent.getCost()+step_cost , parent));
			children.add(new State( getChildBoardArray(parent.toBoardArray(),8,7) , parent.getCost()+step_cost , parent));
		}
		
		return children;
	}
	
	/**
	 * Returns 3 possible children when moving 0 from a side.
	 */
	private static List<State> moveFromSide(State parent) {
		List<State> children = new Stack<>();
		
		if(parent.toBoardArray()[1] == 0) {
			children.add(new State( getChildBoardArray(parent.toBoardArray(),1,0) , parent.getCost()+step_cost , parent));
			children.add(new State( getChildBoardArray(parent.toBoardArray(),1,2) , parent.getCost()+step_cost , parent));
			children.add(new State( getChildBoardArray(parent.toBoardArray(),1,4) , parent.getCost()+step_cost , parent));
		}
		else if(parent.toBoardArray()[3] == 0) {
			children.add(new State( getChildBoardArray(parent.toBoardArray(),3,0) , parent.getCost()+step_cost , parent));
			children.add(new State( getChildBoardArray(parent.toBoardArray(),3,6) , parent.getCost()+step_cost , parent));
			children.add(new State( getChildBoardArray(parent.toBoardArray(),3,4) , parent.getCost()+step_cost , parent));
		}
		else if(parent.toBoardArray()[5] == 0) {
			children.add(new State( getChildBoardArray(parent.toBoardArray(),5,2) , parent.getCost()+step_cost , parent));
			children.add(new State( getChildBoardArray(parent.toBoardArray(),5,8) , parent.getCost()+step_cost , parent));
			children.add(new State( getChildBoardArray(parent.toBoardArray(),5,4) , parent.getCost()+step_cost , parent));
		}
		else {
			children.add(new State( getChildBoardArray(parent.toBoardArray(),7,6) , parent.getCost()+step_cost , parent));
			children.add(new State( getChildBoardArray(parent.toBoardArray(),7,8) , parent.getCost()+step_cost , parent));
			children.add(new State( getChildBoardArray(parent.toBoardArray(),7,4) , parent.getCost()+step_cost , parent));
		}
		
		return children;
	}
	
	/**
	 * Returns 4 possible children when moving 0 from center.
	 */
	private static List<State> moveFromCenter(State parent) {
		List<State> children = new Stack<>();
		
		for(int i = 1 ; i < 8 ; i+=2) {
			children.add(new State( getChildBoardArray(parent.toBoardArray(),4,i) , parent.getCost()+step_cost , parent));
		}
		
		return children;
	}
	
	/**
	 * Swaps 0 with tile to create child board and returns the array. 
	 */
	private static int[] getChildBoardArray(int[] parent_board , int zero_index , int tile_index) {
		int[] child_board = new int[9];
		for(int j = 0 ; j < 9 ; j++) 
			child_board[j] = parent_board[j];
		
		child_board[zero_index] = child_board[tile_index];
		child_board[tile_index] = 0;
		
		return child_board;
	}
}
