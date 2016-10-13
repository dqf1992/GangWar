import java.util.ArrayList;
import java.util.List;

public class Modes {
	String mode;
	char[] plays;
	char youplay;
	char otherplay;
	int depthLimit;
	int[][] boardValue;
	char[][] initState;
	int boardSize;

	public Modes(String mode, char youplay, int depthLimit, int[][] boardValue, String[] boardState) {
		this.mode = mode;
		this.plays = new char[]{'X','O'};
		this.youplay = youplay;
		this.otherplay = youplay == plays[0] ? plays[1]: plays[0];
		this.depthLimit = depthLimit;
		this.boardValue = boardValue;
		this.boardSize = boardState.length;
		this.initState = new char[boardState.length][]; 
		for(int i=0; i<boardState.length; i++) {
			initState[i] = boardState[i].toCharArray();	
		}
	}
	
	public String minimaxDecision() {
		char[][] state = this.initState;
		int depth = 0;
		int v = Integer.MIN_VALUE;
		int ov;
		String ma = "";
		for(String a:actions(state, youplay)) {
			ov = v;
			v = Math.max(v, minValue(result(state, a), depth));
			if(v > ov) {
				ma = a;
			}
		}
		return ma;
	}
	
	private int maxValue(char[][] state, int depth) {
		if(++depth == depthLimit) return utility(state);
		int v = Integer.MIN_VALUE;
		for(String a:actions(state, youplay)) {
			v = Math.max(v, minValue(result(state, a), depth));
		}
		return v;
	}
	
	private int minValue(char[][] state, int depth) {
		System.out.println("depth = " + (depth+1));
		for(int i=0; i<boardSize; i++) {
			for(int j=0; j<boardSize; j++) {
				System.out.print(state[i][j]);
			}
			System.out.println();
		}
		if(++depth == depthLimit) return utility(state);
		int v = Integer.MAX_VALUE;
		for(String a:actions(state, otherplay)) {
			System.out.println(a);
			v = Math.min(v, maxValue(result(state, a), depth));
		}
		return v;
	}
	
	private List<String> actions(char[][] state, char play) {
		// brute force all stake moves
		List<String> moves = new ArrayList<>();
		for(int row=0; row < boardSize; row++) {
			for(int col=0; col < boardSize; col++) {
				if(state[row][col] == '.') {
					char colIdx = (char) (col + 'A');
					String s = new String("Stake " + colIdx + (row+1) + " " + play);
					moves.add(s);
				}
			}
		}
		// calculate all raid move
		int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
		for(int row=0; row < boardSize; row++) {
			for(int col=0; col< boardSize; col++) {
				if(state[row][col] == play) {
					int uRow, uCol, oRow, oCol;
					for(int[] dir:dirs) {
						uRow = row + dir[0];
						uCol = col + dir[1];
						if(checkBounds(uRow, uCol) && state[uRow][uCol] == '.') {
							StringBuilder sb = new StringBuilder();
							char oplay = (play == youplay ? otherplay: youplay);
							for(int[] oDir: dirs) {
								oRow = uRow + oDir[0];
								oCol = uCol + oDir[1];
								if(checkBounds(oRow, oCol) && state[oRow][oCol] == oplay) {
									if(sb.length() == 0) {
										char uColIdx = (char) (uCol + 'A');
										sb.append("Raid " + uColIdx + (uRow+1));
									}
									char oColIdx = (char) (oCol + 'A');
									sb.append(" " + oColIdx + (oRow+1));
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
	
	private boolean checkBounds(int uRow, int uCol) {
		if(uRow >= boardSize || uRow < 0 || uCol >= boardSize || uCol < 0) return false;
		return true;
	}

	public char[][] result(char[][] state, String action) {
		String[] a = action.split(" ");
		char[][] stateCopy = new char[boardSize][];
		for(int i=0; i<boardSize; i++) {
			stateCopy[i] = state[i].clone();
		}
		int col, row;
		if(a[0].equals("Stake")) {
			col = (int) (a[1].charAt(0) - 'A');
			row = (int) (a[1].charAt(1) - '1');
			stateCopy[row][col] = a[2].charAt(0);
		} else if(a[0].equals("Raid")) {
			int i = 1;
			while(i<a.length-1) {
				col = (int) (a[i].charAt(0) - 'A');
				row = (int) (a[i].charAt(1) - '1');
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
	
	public String alphaBetaSearch() {
		char[][] state = this.initState;
		int depth = 0;
		int v = Integer.MIN_VALUE;
		int ov;
		String ma = "";
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		for(String a:actions(state, youplay)) {
			ov = v;
			v = Math.max(v, minValue(result(state, a), depth, alpha, beta));
			if(v > ov) ma = a;
			if(v >= beta) return a;
			alpha = Math.max(alpha, v);
		}
		return ma;
	}
	
	private int maxValue(char[][] state, int depth, int alpha, int beta) {
		if(++depth == depthLimit) return utility(state);
		int v = Integer.MIN_VALUE;
		for(String a:actions(state, youplay)) {
			v = Math.max(v, minValue(result(state,a), depth, alpha, beta));
			if(v >= beta) return v;
			alpha = Math.max(alpha, v);
		}
		return v;
	}

	private int minValue(char[][] state, int depth, int alpha, int beta) {
		if(++depth == depthLimit) return utility(state);
		int v = Integer.MAX_VALUE;
		for(String a: actions(state, otherplay)) {
			v = Math.min(v, maxValue(result(state,a), depth, alpha, beta));
			if(v <= alpha) return v;
			beta = Math.min(beta, v);
		}
		return v;
	}

	public String[] selectMode() {
		String[] out = new String[boardValue.length + 1];
		String action = " ";
		if(mode.equals("MINIMAX")) {
			action = minimaxDecision();
		} else if(mode.equals("ALPHABETA")) {
			action = alphaBetaSearch();
		} else if(mode.equals("COMPETITION")) {
			return null;
		}
		String[] actionArr = action.split(" ");
		out[0] = actionArr[1] + " " + actionArr[0];
		char[][] res = result(this.initState, action);
		for(int i=1; i<out.length; i++) {
			out[i] = new String(res[i-1]);
		}
		return out;
	}
	
	public void printBoard(char[][] state) {
		for(int i=0; i<boardSize; i++) {
			for(int j=0; j<boardSize; j++) {
				System.out.print(state[i][j]);
			}
			System.out.println();
		}
	}
}
