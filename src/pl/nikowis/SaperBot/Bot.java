package pl.nikowis.SaperBot;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

public class Bot {
	private Robot robot;
    private BufferedImage screenshot; 
    private Board board;
    private int[] boardCoordinates;
    private boolean boardDetected;
    private int interspaceX;
    private int interspaceY;
    
	public Bot(Board board){
		interspaceX=interspaceY=16;
		this.board=board;
		boardDetected = false;
		
		try {
            robot = new Robot();
        } catch (AWTException ex) {
            System.out.println(ex.getMessage());
        }
		getScreenshot();
		
	}
	
	private void getScreenshot(){
		screenshot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	}
	
	private int[] findWindow(){
        int matchingPixels = 0;
        int [] point = {1,1};
        for(int x = 1; x<Toolkit.getDefaultToolkit().getScreenSize().getWidth();x++)
        {
            for(int y= 1; y<Toolkit.getDefaultToolkit().getScreenSize().getHeight();y++)
            {
                if(screenshot.getRGB(x,y)==Board.boardCorner[matchingPixels])
                {
                	matchingPixels++;
                    if(matchingPixels >15)
                    {
                    	point[0]=x;
                    	point[1]=y+30;
                    	boardDetected=true;
                        return point;
                    }
                }
                else
                	matchingPixels = 0;
            }
        }
        return null;
    }
	
	public void writeBoardToConsole(){
	    for(int row = 0; row < Board.SIZE; row++){
	            for(int column = 0; column <Board.SIZE; column++){
	                switch(board.fields[row][column]){
	                    case UNCHECKED :
	                        System.out.print(" X");
	                        break;
	                    case BLANK :
	                        System.out.print(" O");
	                        break;
	                    case ONE :
	                        System.out.print(" 1");
	                        break;
	                    case TWO :
	                        System.out.print(" 2");
	                        break;
	                    case THREE :
	                        System.out.print(" 3");
	                        break;
	                    case FOUR :
	                        System.out.print(" 4");
	                        break;
	                    case BOMB :
	                        System.out.print(" B");
	                        break;
	                        
	                }
	            }
	            System.out.println();
	        }
	    System.out.println("\n------------------\n");
	    }
	
	public void getBoardState(){
        int pixelColor;
        robot.mouseMove(1, 1);
        getScreenshot();
        boardCoordinates = findWindow();
        if(!(boardCoordinates==null)){
            for(int row = 0; row < Board.SIZE;row++){
                for(int column = 0; column <Board.SIZE; column++){
                    //tutaj sprawdzamy jaka jest dana komorka i zapisujemy w pola
                    for(int i = 1; i<10;i++){
                       // robot.mouseMove(boardCoordinates[0]+i+column*odstepX,boardCoordinates[1]+i+row*odstepY);
                       // robot.delay(50);
                    	pixelColor = screenshot.getRGB(boardCoordinates[0]+i+column*interspaceX,boardCoordinates[1]+i+row*interspaceY);
                        
                    	if(pixelColor == Board.coveredField){
                        	board.fields[row][column] = EFieldState.UNCHECKED;
                            break;
                        }
                        else if(pixelColor == Board.one){
                            board.fields[row][column] = EFieldState.ONE;
                            break;
                        }
                        else if(pixelColor == Board.two){
                        	board.fields[row][column] = EFieldState.TWO;
                            break;
                        }
                        else if(pixelColor == Board.three){
                        	board.fields[row][column] = EFieldState.THREE;
                            break;
                        }
                        else if(pixelColor == Board.bomb){
                        	board.fields[row][column] = EFieldState.BOMB;
                            break;
                        }
                        board.fields[row][column] = EFieldState.BLANK;
                    }
                }
            }
            writeBoardToConsole();
        
        }
    }
	
	//Checking
	public boolean hasLost(){
		int x=boardCoordinates[0]+63;
		int y=boardCoordinates[1]-25;
        if(boardDetected){
            for(int yi= y; yi<y+8;yi++){
                   if(screenshot.getRGB(x,yi)==Board.face)
                           return true;
            }
        }
        return false;
    }
	
	public void clickField(int i, int j){
		if(hasLost()){
        	System.out.println("You lost...");
        	return;
        }
		if(!boardDetected){
			System.out.println("Board not detected");
			return;
		}
        if(i>Board.SIZE+1 || j > Board.SIZE+1)
            return;
        robot.mouseMove(boardCoordinates[0]+5+(i-1)*interspaceX,boardCoordinates[1]+5+(j-1)*interspaceY);
        robot.delay(300);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseMove(0,0);
        robot.delay(200);
        
        getBoardState();
        
    }
}
