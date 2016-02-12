package pl.nikowis.SaperBot;

public class Board {

	EFieldState[][] fields = new EFieldState[SIZE][SIZE];
	public Board(){
		for(int i=0;i<SIZE;i++)
			for(int j=0;j<SIZE;j++)
				fields[i][j]=EFieldState.UNCHECKED;

	}
	
	public static int boardCorner[] = {-8684677,-8684677,-4144960,-4144960,-4144960,
            -16777216,-16777216,-65536,-65536,-65536,-65536,
            -65536,-65536,-65536,-65536,-65536,-16777216,
            -65536,-65536,-65536,-65536,-65536,-65536,-65536,
            -65536,-65536,-16777216,-16777216,-1,-1,-4342339,-4342339,-4342339};
	public static int coveredField= -1;
	public static int face[]= {-256,-16777216,-16777216,-256,-256,-256};
	public static int one = -16776961;
	public static int two = -16745728;
	public static int three = -65536;
	public static int bomb = -16777216;
	public static final int SIZE = 8;
}
