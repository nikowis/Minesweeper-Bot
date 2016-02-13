package pl.nikowis.SaperBot;
import java.util.Random;

public class MyMain {
	
	private static void copyBoard(Board from, Board to){
		for(int i=0;i<Board.SIZE;i++)
			for(int j=0;j<Board.SIZE;j++)
				if(to.fields[i][j]!=EFieldState.FLAG)
					to.fields[i][j]=from.fields[i][j];
	}
	
	
	private static int[] getNextStep(int x, int y, Board gameBoard) {
		int [] a={1,1};
		
		Random rand = new Random();
		do{
		a[0]=rand.nextInt(8)+1;
		a[1]=rand.nextInt(8)+1;
		}while(gameBoard.fields[a[0]-1][a[1]-1]!=EFieldState.UNCHECKED || gameBoard.fields[a[0]-1][a[1]-1]==EFieldState.FLAG);
		System.out.println("x:" +a[0] +" y:" +a[1]);
		return a;
	}
	

	public static void main(String[] args) {
		Board scanBoard = new Board();
		Bot bot = new Bot(scanBoard);
		Board gameBoard = new Board();
		int x =1;
		int y =1;
		while(bot.clickField(x, y)){
			copyBoard(scanBoard,gameBoard);
			int [] a = getNextStep(x,y,scanBoard);
			x=a[0];
			y=a[1];
		}
		System.out.println();
		System.out.println("The end...");
	}
}
