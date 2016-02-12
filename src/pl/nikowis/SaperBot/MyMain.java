package pl.nikowis.SaperBot;

public class MyMain {

	public static void main(String[] args) {
		Board board = new Board();
		Bot bot = new Bot(board);
		bot.getBoardState();
		bot.clickField(1, 1);
		
		System.out.println("The end...");
	}
}
