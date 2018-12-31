package assignment4Game;

import java.io.*;

public class Game {
	
	public static int play(InputStreamReader input){
		BufferedReader keyboard = new BufferedReader(input);
		Configuration c = new Configuration();
		int columnPlayed = 3; int player;
		
		// first move for player 1 (played by computer) : in the middle of the grid
		c.addDisk(firstMovePlayer1(), 1);
		int nbTurn = 1;
		
		while (nbTurn < 42){ // maximum of turns allowed by the size of the grid
			player = nbTurn %2 + 1;
			if (player == 2){
				columnPlayed = getNextMove(keyboard, c, 2);
			}
			if (player == 1){
				columnPlayed = movePlayer1(columnPlayed, c);
			}
			System.out.println(columnPlayed);
			c.addDisk(columnPlayed, player);
			if (c.isWinning(columnPlayed, player)){
				c.print();
				System.out.println("Congrats to player " + player + " !");
				return(player);
			}
			nbTurn++;
		}
		return -1;
	}
	
	public static int getNextMove(BufferedReader keyboard, Configuration c, int player){
		//get info from keyboard
		//make sure valid
		//check if available no -> ask again yes -> put in
		//c.print();
		//System.out.println("^^start^^");
		while (true) //forever keeps asks until return
		{
		int input;
		String err = "";
		try {
			input = Integer.parseInt(keyboard.readLine());
			if (input > 6 || input < 0)
			{
				err = "invalid input, try again";
				System.out.println(err);
			}
			else if (c.available[input] > 5)
			{
				err = "column is full, try again";
				System.out.println(err);
			}else return input;
		
		}catch (IOException e)
		{
			e.printStackTrace();
		}
		}
	}
	
	public static int firstMovePlayer1 (){
		return 3;
	}
	
	public static int movePlayer1 (int columnPlayed2, Configuration c){
		//try {
		c.print();
		int i = 1;
		//int columnAttempt = columnPlayed2;
		if (c.canWinNextRound(1) != -1)
		{
			//int winningColumn = c.canWinNextRound(1);
			//c.addDisk(winningColumn, 1);
			return c.canWinNextRound(1);
		}
		if (c.canWinTwoTurns(1) != -1)
		{
			//int winningColumn = c.canWinNextRound(1);
			//c.addDisk(winningColumn, 1);
			return c.canWinTwoTurns(1);
		}
		if (c.available[columnPlayed2] <= 5)
		{
			return columnPlayed2;
		}
		else
		{
			while (true) 
			{
			if ((columnPlayed2-i)>-1 && c.available[columnPlayed2-i] <= 5)
			{
				return (columnPlayed2-i);
			}
			if ((columnPlayed2+i)<7 && c.available[columnPlayed2+i] <= 5)
			{
				return (columnPlayed2+i);
			}
			i++;
			}
		}
//		}catch (Exception e)
//		{
//			System.out.println("somethings wrong");
//		}
//		return -1;
		
	}


	
}
