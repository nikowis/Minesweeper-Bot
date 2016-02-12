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
	public Bot(Board board){
		try {
            robot = new Robot();
        } catch (AWTException ex) {
            System.out.println(ex.getMessage());
        }
		getScreenshot();
		this.board=board;
		boardDetected = false;
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
                    if(matchingPixels >20)
                    {
                    	point[0]=x;
                    	point[1]=y+8;
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
	    for(int row = 0; row < Board.SIZE; row++)
	        {
	            for(int column = 0; column <Board.SIZE; column++)
	            {
	                switch(board.fields[row][column])
	                {
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
        int odstepX=16,odstepY=16;
        int kolorPixela;
        getScreenshot();
        boardCoordinates = findWindow();
        if(!(boardCoordinates==null))
        {
            for(int row = 0; row < Board.SIZE;row++)
            {
                for(int column = 0; column <Board.SIZE; column++)
                {
                    //tutaj sprawdzamy jaka jest dana komorka i zapisujemy w pola
                    for(int i = 1; i<10;i++)
                    {
                        //bocik.mouseMove(poczatekPlanszy[0]+i+kolumna*odstepX,poczatekPlanszy[1]+i+rzad*odstepY);
                        //bocik.delay(100);
                        kolorPixela = screenshot.getRGB(boardCoordinates[0]+i+column*odstepX,boardCoordinates[1]+i+row*odstepY);
                        if(kolorPixela == Board.coveredField)
                        {
                        	board.fields[row][column] = EFieldState.UNCHECKED;
                            break;
                        }
                        else if(kolorPixela == Board.one)
                        {
                            board.fields[row][column] = EFieldState.ONE;
                            break;
                        }
                        else if(kolorPixela == Board.two)
                        {
                        	board.fields[row][column] = EFieldState.TWO;
                            break;
                        }
                        else if(kolorPixela == Board.three)
                        {
                        	board.fields[row][column] = EFieldState.THREE;
                            break;
                        }
                        else if(kolorPixela == Board.bomb)
                        {
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
	
	public void clickField(int i, int j){
		if(!boardDetected){
			System.out.println("Board not detected");
			return;
		}
        int odstepX=16,odstepY=16;
        if(i>Board.SIZE+1 || j > Board.SIZE+1)
            return;
        robot.mouseMove(boardCoordinates[0]+5+(i-1)*odstepX,boardCoordinates[1]+5+(j-1)*odstepY);
        robot.delay(300);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseMove(0,0);
        robot.delay(200);
        getBoardState();
//        if(czyPrzegrana())
//        {
//            System.out.println("przegralem");
//            //System.exit(1);
//        }
    }
}
