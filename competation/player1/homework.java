import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class homework {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		String fileToRead = "input.txt";
		String fileToWrite = "output.txt";
		if(args.length > 0) {
			fileToRead = args[0];
			if(args.length > 1) {
				fileToWrite = args[1];
			}
		}
		Scanner scan = new Scanner(new BufferedReader(new FileReader(fileToRead)));
		int n = scan.nextInt();
		String mode = scan.next();
		char youplay = scan.next().charAt(0);
		double timeRemain = scan.nextDouble();
		int depthLimit = (int)timeRemain;
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
		Game game = new Game(youplay, depthLimit, timeRemain, boardValue, boardState);
		PrintWriter writer = new PrintWriter(fileToWrite, "UTF-8");
		String[] out = game.selectMode(mode);
		for(int i=0; i<out.length; i++) {
			writer.print(out[i]);
			if(i != out.length - 1) {
				writer.println();
			}
		}
		writer.close();
	}
}
