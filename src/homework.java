import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;


public class homework {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		Scanner scan = new Scanner(new BufferedReader(new FileReader("input.txt")));
		int n = scan.nextInt();
		String mode = scan.next();
		char youplay = scan.next().charAt(0);
		int depth = scan.nextInt();
		int[][] boardValue = new int[n][n];
		for(int i=0; i < n; i++) {
			for(int j=0; j < n; j++) {
				boardValue[i][j] = scan.nextInt();
			}
		}
		String[] boardState = new String[n];
		for(int i=0; i < n; i++) {
			boardState[i] = scan.next();
		}
		scan.close();
//		System.out.printf("N = %d\nMode = %s\nyouplay = %c\ndepth = %d\n", n, mode, youplay, depth);
//		for(int[] board: boardValue) {
//			StringBuilder sb = new StringBuilder();
//			for(int boa: board) {
//				sb.append(boa + " ");
//			}
//			System.out.println(sb.toString());
//		}
//		for(String state: boardState) {
//			System.out.println(state);
//		}
		Modes modes = new Modes(mode, youplay, depth, boardValue, boardState);
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		for(String s: modes.selectMode()) {
			writer.println(s);
		}
		writer.close();
	}
}
