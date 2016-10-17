import java.util.ArrayList;
import java.util.List;

public class Game {
	char[] plays;
	char youplay;
	char otherplay;
	int depthLimit;
	double timeRemain;
	int[][] boardValue;
	char[][] initState;
	int boardSize;
	int moveRemain;
	double avgMoveTime;
	double processRate;
	
	public Game(char youplay, int depthLimit, double timeRemain, int[][] boardValue, String[] boardState) {
		this.plays = new char[]{'X','O'};
		this.youplay = youplay;
		this.otherplay = youplay == plays[0] ? plays[1]: plays[0];
		this.depthLimit = depthLimit;
		this.timeRemain = timeRemain;
		this.boardValue = boardValue;
		this.boardSize = boardState.length;
		this.initState = new char[boardSize][]; 
		for(int i=0; i<boardSize; i++) {
			initState[i] = boardState[i].toCharArray();	
		}
		this.moveRemain = 0;
		for(int i=0; i<boardSize; i++) {
			for(int j=0; j<boardSize; j++) {
				if(initState[i][j] == '.') {
					moveRemain++;
				}
			}
		}
		processRate = moveRemain / (boardSize * boardSize);
		processRate = 1 - processRate;
		moveRemain = moveRemain/2;
		avgMoveTime = timeRemain/moveRemain;
	}
	
	private String minimaxDecision() {
		char[][] state = this.initState;
		int depth = 0;
		int v = Integer.MIN_VALUE;
		int ov;
		String ma = null;
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
		depth++;
		if(cutOffTest(state, depth)) return utility(state);
		int v = Integer.MIN_VALUE;
		for(String a:actions(state, youplay)) {
			v = Math.max(v, minValue(result(state, a), depth));
		}
		return v;
	}
	
	private int minValue(char[][] state, int depth) {
		depth++;
		if(cutOffTest(state, depth)) return utility(state);
		int v = Integer.MAX_VALUE;
		for(String a:actions(state, otherplay)) {
			v = Math.min(v, maxValue(result(state, a), depth));
		}
		return v;
	}
	
	private List<String> actions(char[][] state, char uplay) {
		// brute force all stake moves
		List<String> moves = new ArrayList<>();
		for(int row=0; row < boardSize; row++) {
			for(int col=0; col < boardSize; col++) {
				if(state[row][col] == '.') {
					char colIdx = (char) (col + 'A');
					String s = new String("Stake " + colIdx + (row+1) + " " + uplay);
					moves.add(s);
				}
			}
		}
		// calculate all raid moves
		int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
		for(int row=0; row < boardSize; row++) {
			for(int col=0; col < boardSize; col++) {
				if(state[row][col] == '.') {
					int nRow, nCol;
					boolean existU = false;
					boolean existO = false;
					char oplay = (uplay == youplay? otherplay: youplay); 
					StringBuilder sb = new StringBuilder();
					for(int[] dir:dirs) {
						nRow = row + dir[0];
						nCol = col + dir[1];
						if(!checkBounds(nRow, nCol)) continue;
						if(state[nRow][nCol] == uplay) {
							existU = true;
						} else if(state[nRow][nCol] == oplay) {
							existO = true;
						}
					}
					if(existU && existO) {
						char colIdx = (char) (col + 'A');
						sb.append("Raid " + colIdx + (row+1));
						for(int[] dir: dirs) {
							nRow = row + dir[0];
							nCol = col + dir[1];
							if(checkBounds(nRow, nCol) && state[nRow][nCol] == oplay) {
								char nColIdx = (char) (nCol + 'A');
								sb.append(" " + nColIdx + (nRow+1));
							}
						}
						sb.append(" " + uplay);
						moves.add(sb.toString());
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

	private char[][] result(char[][] state, String action) {
		String[] a = action.split(" ");
		char[][] stateCopy = new char[boardSize][];
		for(int i=0; i<boardSize; i++) {
			stateCopy[i] = state[i].clone();
		}
		int col, row;
		if(a[0].equals("Stake")) {
			col = (int) (a[1].charAt(0) - 'A');
			row = (int) (a[1].charAt(1) - '0');
			if(a[1].length() == 3) {
				row = row * 10 + (int) (a[1].charAt(2) - '0');
			} 
			row --;
			stateCopy[row][col] = a[2].charAt(0);
		} else if(a[0].equals("Raid")) {
			int i = 1;
			while(i<a.length-1) {
				col = (int) (a[i].charAt(0) - 'A');
				row = (int) (a[i].charAt(1) - '0');
				if(a[i].length() == 3) {
					row = row * 10 + (int) (a[i].charAt(2) - '0');
				}
				row --;
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
	
	private String competitionAlphaBeta() {
		depthLimit = 3;
		if(processRate < 0.5){
			depthLimit = 2;
		}
		char[][] state = this.initState;
		int depth = 0;
		int v = Integer.MIN_VALUE;
		int ov;
		String ma = null;
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
	
	private String alphaBetaSearch() {
		char[][] state = this.initState;
		int depth = 0;
		int v = Integer.MIN_VALUE;
		int ov;
		String ma = null;
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
		depth++;
		if(cutOffTest(state, depth)) return utility(state);
		int v = Integer.MIN_VALUE;
		for(String a:actions(state, youplay)) {
			v = Math.max(v, minValue(result(state,a), depth, alpha, beta));
			if(v >= beta) return v;
			alpha = Math.max(alpha, v);
		}
		return v;
	}

	private int minValue(char[][] state, int depth, int alpha, int beta) {
		depth++;
		if(cutOffTest(state, depth)) return utility(state);
		int v = Integer.MAX_VALUE;
		for(String a: actions(state, otherplay)) {
			v = Math.min(v, maxValue(result(state,a), depth, alpha, beta));
			if(v <= alpha) return v;
			beta = Math.min(beta, v);
		}
		return v;
	}
	
	private boolean cutOffTest(char[][] state, int depth) {
		boolean hasSpace = false;
		for(int i=0; i<boardSize; i++) {
			for(int j=0; j<boardSize; j++) {
				if(state[i][j] == '.') {
					hasSpace = true;
				}
			}
		}
		return !hasSpace || (depth == depthLimit);
	}

	public String[] selectMode(String mode) {
		String[] out = new String[boardValue.length + 1];
		String action = null;
		if(mode.equals("MINIMAX")) {
			action = minimaxDecision();
		} else if(mode.equals("ALPHABETA")) {
			action = alphaBetaSearch();
		} else if(mode.equals("COMPETITION")) {
			action = competitionAlphaBeta();
		}
		if(action == null) return null;
		String[] actionArr = action.split(" ");
		out[0] = actionArr[1] + " " + actionArr[0];
		char[][] res = result(this.initState, action);
		for(int i=1; i<out.length; i++) {
			out[i] = new String(res[i-1]);
		}
		return out;
	}
	
}
