package pl.nikowis.SaperBot;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

public class Bot {
	private static final int DELAY=100;
	private Robot robot;
    private BufferedImage screenshot; 
    private Board board;
    private int[] boardCoordinates;
    private boolean boardDetected;
    private int interspaceX;
    private int interspaceY;
    public int movesLeft =40;
    
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
		getBoardState();
	}
	
	private void getScreenshot(){
		screenshot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	}
	
	private int[] findWindow(){
        int matchingPixels = 0;
        boardDetected = false;
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
	
	private void writeBoardToConsole(){
	    for(int column = 0; column < Board.SIZE; column++){
	            for(int row = 0; row <Board.SIZE; row++){
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
	                    case FIVE :
	                        System.out.print(" 5");
	                        break;
	                    case BOMB :
	                        System.out.print(" B");
	                        break;
	                    default :
	                    	System.out.println("ERROR WRITING BOARD TO CONSOLE");
	                        
	                }
	            }
	            System.out.println();
	        }
	    System.out.println("\n------------------\n");
	    }
	
	private void getBoardState(){
        int pixelColor;
        robot.mouseMove(1, 1);
        getScreenshot();
        boardCoordinates = findWindow();
        if(boardDetected){
            for(int row = 0; row < Board.SIZE;row++){
                for(int column = 0; column <Board.SIZE; column++){
                    for(int i = 1; i<10;i++){
                    	pixelColor = screenshot.getRGB(boardCoordinates[0]+i+row*interspaceX,boardCoordinates[1]+i+3+column*interspaceY);
                        //robot.mouseMove(boardCoordinates[0]+i+row*interspaceX,boardCoordinates[1]+i+4+column*interspaceY);
                    	//robot.delay(50);
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
                        else if(pixelColor == Board.four){
                        	board.fields[row][column] = EFieldState.FOUR;
                            break;
                        }
                        else if(pixelColor == Board.five){
                        	board.fields[row][column] = EFieldState.FIVE;
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
	
	public boolean hasLost(){
		if(movesLeft <1)
			return true;
        if(boardDetected){
        	int x=boardCoordinates[0]+63;
    		int y=boardCoordinates[1]-21;
            for(int yi= y; yi<y+5;yi++){
                   if(screenshot.getRGB(x,yi)==Board.face)
                           return true;
            }
        }
        return false;
    }
	
	public boolean clickField(int i, int j){
		movesLeft--;
		if(!boardDetected){
			System.out.println("Board not detected");
			return false;
		}
		if(hasLost()){
        	return false;
        }
        if(i>Board.SIZE+1 || j > Board.SIZE+1)
        {
        	System.out.println("ERROR OUT OF BOUNDS IN CLICKFIELD");
            return false;
        }
        robot.mouseMove(boardCoordinates[0]+6+(i-1)*interspaceX,boardCoordinates[1]+10+(j-1)*interspaceY);
        robot.delay(DELAY);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseMove(1,1);
        robot.delay(DELAY);
        
        getBoardState();
        return true;
    }
	
	public void setFlag(int i, int j){
		robot.mouseMove(boardCoordinates[0]+6+(i-1)*interspaceX,boardCoordinates[1]+10+(j-1)*interspaceY);
		robot.delay(DELAY);
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseMove(1,1);
        robot.delay(DELAY);
	}

	public boolean hasPossiblyWon(){
        if(boardDetected){
    		int x=boardCoordinates[0]+63;
    		int y=boardCoordinates[1]-25;
            for(int yi= y; yi<y+4;yi++){
                   if(screenshot.getRGB(x,yi)==Board.face)
                           return true;
            }
            return false;
        }
        return true;
	}

	public boolean reset() {
		if(boardDetected){
			movesLeft=20;
    		int x=boardCoordinates[0]+63;
    		int y=boardCoordinates[1]-25;
    		robot.mouseMove(x, y);
    		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseMove(1,1);
            getBoardState();
            return true;
       }
		return false;
	}
}
