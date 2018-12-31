package assignment4Game;

public class Configuration {
	
	public int[][] board;
	public int[] available;
	boolean spaceLeft;
	
	public Configuration(){
		board = new int[7][6];
		available = new int[7];
		spaceLeft = true;
	}
	
	public void print(){
		System.out.println("| 0 | 1 | 2 | 3 | 4 | 5 | 6 |");
		System.out.println("+---+---+---+---+---+---+---+");
		for (int i = 0; i < 6; i++){
			System.out.print("|");
			for (int j = 0; j < 7; j++){
				if (board[j][5-i] == 0){
					System.out.print("   |");
				}
				else{
					System.out.print(" "+ board[j][5-i]+" |");
				}
			}
			System.out.println();
		}
	}
	
	public void addDisk (int index, int player){
		try {
		int i = available[index];
		available[index]++;
		board[index][i] = player;
		} catch(ArrayIndexOutOfBoundsException e)
		{
			
		}

		//do not have to worry about a case where trying to add to a full column (according to A4)
//		if (available[index]+1 >=6)
//		{
//			System.out.println("kill it");
//			spaceLeft = false;
//		}
		//print();
	}
	
	public boolean isWinning (int lastColumnPlayed, int player){
		//ignore trying to deal with last column played
		//the winning move can be dropped in the middle of a sequence of 4 for example
		//easier to just go through every single point and assume it is the end of a winning sequence
		//probably not very efficient, especially in larger boards
		
		for (int i = 0; i<=6; i++)
		{
			for (int j = 0; j<=5; j++)
			{
				if(this.totalWin(i,j,player)==true)
				{
					return true;
				}
			}
		}
		return false; // DON'T FORGET TO CHANGE THE RETURN
	}
	
	//4 WIN CONDITIONS
	//PROVIDED WITH MOST RECENT PLAY, SIMPLY CHECK NEXT 3 FOR THE 4 CASES
	public boolean totalWin(int column, int row, int player)
	{
		//since iterate through all spots on board, possible we try to check wrong player so immediately return false
		if (board[column][row] != player)
		{
			return false;
		}
		
		//pass in every check to all the possible win conditions
		
		else if (this.winHorizontal(column, row, player) == true)
		{
			return true;
		}
		else if (this.winVertical(column, row, player) == true)
		{
			return true;
		}
		else if (this.winUpLeftDiagonal(column, row, player) == true)
		{
			return true;
		}
		else if (this.winUpRightDiagonal(column, row, player) == true)
		{
			return true;
		}else
			return false;
	}
	public boolean winHorizontal (int column, int row, int player)
	{
		//test from left to right since iterating through board goes in same direction, slightly faster
		//starts from bottom so test going up
		try {
		if (board[column+1][row] == player &&
				board[column+2][row] == player &&
					board[column+3][row] == player )
		{
			return true;
		}
		else
		{
			return false;
		}
		}catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
	}
	public boolean winVertical(int column, int row, int player)
	{
		try {
			if (board[column][row+1] == player &&
					board[column][row+2] == player &&
						board[column][row+3] == player )
			{
				return true;
			}
			else
			{
				return false;
			}
			}catch(ArrayIndexOutOfBoundsException e)
			{
				return false;
			}
	}
	public boolean winUpLeftDiagonal(int column, int row, int player)
	{
		try {
			if (board[column-1][row+1] == player &&
					board[column-2][row+2] == player &&
						board[column-3][row+3] == player )
			{
				return true;
			}
			else
			{
				return false;
			}
			}catch(ArrayIndexOutOfBoundsException e)
			{
				return false;
			}
	}
	public boolean winUpRightDiagonal(int column, int row, int player)
	{
		try {
			if (board[column+1][row+1] == player &&
					board[column+2][row+2] == player &&
						board[column+3][row+3] == player )
			{
				return true;
			}
			else
			{
				return false;
			}
			}catch(ArrayIndexOutOfBoundsException e)
			{
				return false;
			}
	}
	public int canWinNextRound (int player){
		//iterate through all the columns, add relevant disk, check if it then wins
		if (!this.spaceLeft)
		{
			return -1;
		}
		for (int i = 0; i<=6; i++)
		{
			this.addDisk(i, player);
		//	print();
			if(this.isWinning(i, player)==true)
			{
				this.removeDisk(i);
				return i;
			}else //if it does not win the game, we added a losing disk and must remove to rest board
			{
				this.removeDisk(i);
			}
		}
		return -1; // DON'T FORGET TO CHANGE THE RETURN
	}
	
	public void removeDisk (int column)
	{
		try {
			int j = available[column]-1;
			board[column][j] = 0;
			available[column]--;
			} catch(ArrayIndexOutOfBoundsException e)
			{
				
			}
	}
	public int canWinTwoTurns (int player){
		//set up players
		//player 1 play EVERYTHING
		//player 2 not win next round
		//AND player 1 play win next
		//if nothing works, return -1
		
		//lots of checks for if there's actually enough space
		int result = -1;
		if (!this.spaceLeft)
		{
			return result;
		}
		//safe to assume we only receive players 1 and 2
		int hero = 0;
		int opponent = 0;
		if (player == 1)
		{
			opponent = 2;
			hero = 1;
		}
		else 
		{
			hero = 2;
			opponent = 1;
		}
		//check can win for every column
		for (int i = 0; i<=6; i++)
		{
			if (!this.spaceLeft)
			{
				return result;
			}
			//ensure you can actually play in column
			if (this.available[i]<=5)
			{
				//check the play there
				this.addDisk(i,hero);
				if (!this.spaceLeft)
				{
					this.removeDisk(i);
					return result;
				}
				//ensure that adding this doesnt give the win to other player 
				//create a guard ensuring that they don't allow the other player to win first
				if (this.canWinNextRound(opponent) != -1)
				{
					return result;
				}
				int win = this.canWinNextRound(hero);
				//if with that play, hero can actually win next turn
				//means win will be the next play hero has to make, so naturally opponent will try to block it and add disk
				//with that new disk added, as long as opponent doesnt win with it, we check now if hero can win next
				if(win!=-1)
				{
					this.addDisk(win, opponent);
					if (!this.spaceLeft)
					{
						this.removeDisk(i);
						this.removeDisk(win);
						return result;
					}
					//if hero can still win then thats it, remove the test disks and return current column
					if(this.canWinNextRound(hero) != -1)
					{
						//remove the disks used to test
						this.removeDisk(i);
						this.removeDisk(win);
						return i; //return winning column
					}this.removeDisk(win);
				}this.removeDisk(i);
			}
		}
		return result; // DON'T FORGET TO CHANGE THE RETURN
	}
}
