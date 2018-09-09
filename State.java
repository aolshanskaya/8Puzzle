
public class State {
	private int[] state;
	private int f1;
	private int f2;
	private int g;
	private boolean solvable;
	private boolean goal;
	private State parent_state;
	private int zero_index;
	
	
	/**
	 * Constructor takes an int[] representing this state and the accumulated path cost
	 * to get to this state from the initial state. 
	 */
	public State(int[] state , int cost , State parent) {
		this.state = state;
		parent_state = parent;
		solvable = checkIfSolvable();
		goal = checkIfGoal();
		f1 = cost + calcH1();
		f2 = cost + calcH2();
		g = cost;
	}
	
	public State(int[] state) {
		this(state , 0 , null);
	}
	
	public int getZeroIndex() {
		return zero_index;
	}
	
	public State getParentState() {
		return parent_state;
	}
	
	public int[] toBoardArray() {
		return state;
	}
	
	/**
	 * Step cost
	 */
	public int getCost() {
		return g;
	}
	
	/**
	 * Step cost + respective heuristics
	 */
	public int getf1() {
		return f1;
	}
	
	public int getf2() {
		return f2;
	}
	
	public boolean isSolvable() {
		return solvable;
	}
	
	public boolean isGoal() {
		return goal;
	}
	
	/**
	 * Checks if this state is goal state;
	 */
	private boolean checkIfGoal() {
		for(int i = 1 ; i < 9 ; i++) {
			if( state[i] != i )
				return false;
		}
		return true;
	}
	
	/**
	 * Checks if solvable by counting inversions. If odd num of inversions then not solvable.
	 */
	private boolean checkIfSolvable() {
		int inversions = 0;
		
		for(int i = 0 ; i < 9 ; i++) {
			if(state[i] == 0) {
				zero_index = i;
				continue;
			}
			for(int j = i+1 ; j < 9 ; j++) {
				if(state[j] != 0 && state[i] > state[j])
					inversions++;
			}
		}
		if(inversions % 2 == 0)
			return true;
		return false;
	}
	
	/**
	 * Number of misplaced tiles in state.
	 */
	private int calcH1() {
		int h = 0;
		for(int i = 1 ; i < 9 ; i++) {
			if(state[i] != i)
				h++;
		}
		return h;
	}
	
	/**
	 * Sum of distances of all tiles from their individual goal positions.
	 */
	private int calcH2() {
		int h = 0;
		for(int i = 0 ; i < 9 ; i++) {
			 if(state[i] == 0)
				 continue;
			 
			 int vertical_distance = (int)Math.abs(state[i]/3 - i/3);
		
			 int j = i + vertical_distance*3;
			 if(i > state[i])
				 j = i - vertical_distance*3;
			 
			 int distance = vertical_distance + (int)Math.abs(state[i] - j);
			 h+=distance;
		}
		return h;
	}
	
}
