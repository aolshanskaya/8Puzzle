
public class PuzzleGenerator {
	private static PuzzleGenerator pg = null;
	private PuzzleGenerator() {}
	public static PuzzleGenerator getPuzzleGenerator() {
		if(pg == null)
			pg = new PuzzleGenerator();
		return pg;
	}
	
	/**
	 * Generates a random 8-puzzle. 
	 */
	public static int[] generateRandomPuzzle() {
		int[] board = new int[9];
		for(int i = 1 ; i < 9 ; i++) {
			int index = (int)(Math.random()*9);
			while(board[index] != 0) {
				if(index == 8)
					index = 0;
				else
					index++;
			}
			board[index] = i;
		}
		return board;
	}
}
