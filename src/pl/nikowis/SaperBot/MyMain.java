package pl.nikowis.SaperBot;
import java.util.Random;

public class MyMain {
	
	private static void copyBoard(Board from, Board to){
		for(int i=0;i<Board.SIZE;i++)
			for(int j=0;j<Board.SIZE;j++)
				if(to.fields[i][j]!=EFieldState.FLAG)
					to.fields[i][j]=from.fields[i][j];
	}
	
	private static int[] getNextRand(Board gameBoard) {
		System.out.println("Drawing a random number...");
		int [] a={1,1};			
		Random rand = new Random();
		do{
		a[0]=rand.nextInt(8)+1;
		a[1]=rand.nextInt(8)+1;
		}while(gameBoard.fields[a[0]-1][a[1]-1]!=EFieldState.UNCHECKED || gameBoard.fields[a[0]-1][a[1]-1]==EFieldState.FLAG);
		return a;
	}
	
	private static int countNeighbour(int i, int j, Board gameBoard, EFieldState state) {
		int count =0;
		if(i-1>=0 &&j-1>=0 && gameBoard.fields[i-1][j-1]==state)
			count++;
		if(j-1>=0 && gameBoard.fields[i][j-1]==state)
			count++;
		if(i+1<8 && j-1>=0 && gameBoard.fields[i+1][j-1]==state)
			count++;
		if(i-1>=0 && gameBoard.fields[i-1][j]==state)
			count++;
		if(i+1<8 && gameBoard.fields[i+1][j]==state)
			count++;
		if(i-1>=0 && j+1<8 && gameBoard.fields[i-1][j+1]==state)
			count++;
		if(j+1<8 && gameBoard.fields[i][j+1]==state)
			count++;
		if(i+1<8 && j+1<8 && gameBoard.fields[i+1][j+1]==state)
			count++;
		
		return count;
	}
	
	private static boolean clickSafeUncheckedNeighbours(int i, int j, Board gameBoard, Bot bot) {
		boolean changed=false;
		System.out.println("Clicking safe unchecked...");
		if(i-1>=0 &&j-1>=0 && gameBoard.fields[i-1][j-1]==EFieldState.UNCHECKED){
			bot.clickField(i, j);
			changed=true;
		}
		if(j-1>=0 && gameBoard.fields[i][j-1]==EFieldState.UNCHECKED){
			bot.clickField(i+1, j);
			changed=true;
		}
		if(i+1<8 && j-1>=0 && gameBoard.fields[i+1][j-1]==EFieldState.UNCHECKED){
			bot.clickField(i+2, j);
			changed=true;
		}
		if(i-1>=0 && gameBoard.fields[i-1][j]==EFieldState.UNCHECKED){
			bot.clickField(i, j+1);
			changed=true;
		}
		if(i+1<8 && gameBoard.fields[i+1][j]==EFieldState.UNCHECKED){
			bot.clickField(i+2, j+1);
			changed=true;
		}
		if(i-1>=0 && j+1<8 && gameBoard.fields[i-1][j+1]==EFieldState.UNCHECKED){
			bot.clickField(i, j+2);
			changed=true;
		}
		if(j+1<8 && gameBoard.fields[i][j+1]==EFieldState.UNCHECKED){
			bot.clickField(i+1, j+2);
			changed=true;
		}
		if(i+1<8 && j+1<8 && gameBoard.fields[i+1][j+1]==EFieldState.UNCHECKED){
			bot.clickField(i+2, j+2);
			changed=true;
		}
		
		return changed;
	}
	
	private static boolean clickSafeUnchecked(Board gameBoard, Bot bot) {
		int flagCount;
		boolean clickedSomething=false;
		for(int j=0;j<Board.SIZE;j++){
			for(int i=0;i<Board.SIZE;i++){
				flagCount=countNeighbour(i,j,gameBoard,EFieldState.FLAG);
				if(flagCount==gameBoard.fields[i][j].ordinal())
					clickedSomething=clickSafeUncheckedNeighbours(i,j,gameBoard,bot);
			}
		}
		return clickedSomething;
	}

	private static void setFlagsOnNeighbours(int i, int j, Board gameBoard, Bot bot) {
		System.out.println("Setting flags...");
		if(i-1>=0 &&j-1>=0 && gameBoard.fields[i-1][j-1]==EFieldState.UNCHECKED){
			bot.setFlag(i, j);
			gameBoard.fields[i-1][j-1]=EFieldState.FLAG;
		}
		if(j-1>=0 && gameBoard.fields[i][j-1]==EFieldState.UNCHECKED){
			bot.setFlag(i+1, j);
			gameBoard.fields[i][j-1]=EFieldState.FLAG;
		}
		if(i+1<8 && j-1>=0 && gameBoard.fields[i+1][j-1]==EFieldState.UNCHECKED){
			bot.setFlag(i+2, j);
			gameBoard.fields[i+1][j-1]=EFieldState.FLAG;
		}
		if(i-1>=0 && gameBoard.fields[i-1][j]==EFieldState.UNCHECKED){
			bot.setFlag(i, j+1);
			gameBoard.fields[i-1][j]=EFieldState.FLAG;
		}
		if(i+1<8 && gameBoard.fields[i+1][j]==EFieldState.UNCHECKED){
			bot.setFlag(i+2, j+1);
			gameBoard.fields[i+1][j]=EFieldState.FLAG;
		}
		if(i-1>=0 && j+1<8 && gameBoard.fields[i-1][j+1]==EFieldState.UNCHECKED){
			bot.setFlag(i, j+2);
			gameBoard.fields[i-1][j+1]=EFieldState.FLAG;
		}
		if(j+1<8 && gameBoard.fields[i][j+1]==EFieldState.UNCHECKED){
			bot.setFlag(i+1, j+2);
			gameBoard.fields[i][j+1]=EFieldState.FLAG;
		}
		if(i+1<8 && j+1<8 && gameBoard.fields[i+1][j+1]==EFieldState.UNCHECKED){
			bot.setFlag(i+2, j+2);
			gameBoard.fields[i+1][j+1]=EFieldState.FLAG;
		}
	}
	
	private static void setFlags(Board gameBoard, Bot bot) {
		int uncheckedCount;
		for(int j=0;j<Board.SIZE;j++){
			for(int i=0;i<Board.SIZE;i++){
				if(gameBoard.fields[i][j].ordinal()<1 || gameBoard.fields[i][j].ordinal()>5)
					continue;
				uncheckedCount=countNeighbour(i,j,gameBoard,EFieldState.UNCHECKED);
				if(uncheckedCount==gameBoard.fields[i][j].ordinal())
					setFlagsOnNeighbours(i,j,gameBoard,bot);
			}
		}
		
	}
	
	public static void main(String[] args) {
		Board scanBoard = new Board();
		Bot bot = new Bot(scanBoard);
		Board gameBoard = new Board();
		int []a;
		bot.clickField(1, 1);
		while(!bot.hasLost()){
			copyBoard(scanBoard,gameBoard);
			setFlags(gameBoard, bot);
			if(clickSafeUnchecked(gameBoard,bot))
				continue;
			a=getNextRand(gameBoard);
			bot.clickField(a[0], a[1]);
		}
		System.out.println();
		System.out.println("The end...");
	}

}
