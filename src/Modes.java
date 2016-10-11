import java.util.ArrayList;
import java.util.List;

public class Modes {
	String mode;
	char[] plays;
	char youplay;
	char otherplay;
	int depthLimit;
	int depth;
	int[][] boardValue;
	char[][] initState;

	public Modes(String mode, char youplay, int depthLimit, int[][] boardValue, String[] boardState) {
		this.mode = mode;
		this.plays = new char[]{'X','O'};
		this.youplay = youplay;
		this.otherplay = youplay == plays[0] ? plays[1]: plays[0];
		this.depth = 0;
		this.depthLimit = depthLimit;
		this.boardValue = boardValue;
		for(int i=0; i<boardState.length; i++) {
			initState[i] = boardState[i].toCharArray();
		}
	}
	
	public String minimaxDecision() {
		++depth;
		int v = Integer.MIN_VALUE;
		char[][] state = this.initState;
		for(String a:actions(state, youplay)) {
			v = Math.max(v, minValue(result(state, a)));
		}
		return null;
	}
	
	private int maxValue(char[][] state) {
		if(++depth > depthLimit) return utility(state);
		int v = Integer.MIN_VALUE;
		for(String a:actions(state, youplay)) {
			v = Math.max(v, minValue(result(state, a)));
		}
		return v;
	}
	
	private int minValue(char[][] state) {
		if(++depth > depthLimit) return utility(state);
		int v = Integer.MAX_VALUE;
		for(String a:actions(state, otherplay)) {
			v = Math.min(v, maxValue(result(state, a)));
		}
		return v;
	}
	
	private List<String> actions(char[][] state, char play) {
		// brute force all stake moves
		List<String> moves = new ArrayList<>();
		for(int row=0; row < state.length; row++) {
			for(int col=0; col< state.length; col++) {
				if(state[row][col] == '.') {
					char colIdx = (char) (col + 'A');
					moves.add(new String("Stake " + colIdx + row));
				}
			}
		}
		// calculate all raid move
		int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
		for(int row=0; row < state.length; row++) {
			for(int col=0; col< state[0].length; col++) {
				if(state[row][col] == play) {
					int uRow, uCol, oRow, oCol;
					for(int[] dir:dirs) {
						uRow = row + dir[0];
						uCol = col + dir[1];
						if(state[uRow][uCol] == '.') {
							StringBuilder sb = new StringBuilder();
							char oplay = (play == youplay ? otherplay: youplay);
							for(int[] oDir: dirs) {
								oRow = uRow + oDir[0];
								oCol = uCol + oDir[1];
								if(state[oRow][oCol] == oplay) {
									if(sb.length() == 0) {
										char uColIdx = (char) (uCol + 'A');
										sb.append("Raid " + uColIdx + uRow);
									}
									char oColIdx = (char) (oCol + 'A');
									sb.append(" " + oColIdx + oRow);
								}
							}
							if(sb.length() != 0) {
								sb.append(" " + play);
								moves.add(sb.toString());
							}
						}
					}
				}
			}
		}
		return moves;
	}
	
	private char[][] result(char[][] state, String action) {
		String[] a = action.split(" ");
		char[][] stateCopy = state.clone();
		int col, row;
		if(a[0].equals("Stake")) {
			col = (int) (a[1].charAt(0) - 'A');
			row = (int) (a[1].charAt(1) - '0');
			stateCopy[row][col] = a[2].charAt(0);
		} else if(a[0].equals("Raid")) {
			int i = 1;
			while(i<a.length-1) {
				col = (int) (a[1].charAt(0) - 'A');
				row = (int) (a[1].charAt(1) - '0');
				stateCopy[row][col] = a[a.length-1].charAt(0);
				i++;
			}
		} else {
			return null;
		}
		return stateCopy;
	}
	
	private int utility(char[][] state) {
		int util = 0;
		for(int row=0; row < state.length; row++) {
			for(int col=0; col< state.length; col++) {
				if(state[row][col] == youplay) {
					util += boardValue[row][col];
				} else if(state[row][col] == otherplay) {
					util -= boardValue[row][col];
				}
			}
		}
		return util;
	}
	
	public String alphaBeta() {
		return null;
	}
	
	public String selectMode() {
		if(mode.equals("MINIMAX")) {
			return minimaxDecision();
		} else if(mode.equals("ALPHABETA")) {
			return this.alphaBeta();
		} else if(mode.equals("COMPETITION")) {
			return null;
		}
		return null;
	}
}