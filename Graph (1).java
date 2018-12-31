package assignment4Graph;

public class Graph {
	
	boolean[][] adjacency;
	int nbNodes;
	
	public Graph (int nb){
		this.nbNodes = nb;
		this.adjacency = new boolean [nb][nb];
		for (int i = 0; i < nb; i++){
			for (int j = 0; j < nb; j++){
				this.adjacency[i][j] = false;
			}
		}
	}
	
	public void addEdge (int i, int j){
		//ensure actually valid input 
		if(i>=0 || i<this.nbNodes || j>=0 || j<this.nbNodes)
		{
		adjacency[i][j] = true;
		adjacency[j][i] = true;
		}
	}
	
	public void removeEdge (int i, int j){
		if(i>=0 || i<this.nbNodes || j>=0 || j<this.nbNodes)
		{
		adjacency[i][j] = false;
		adjacency[j][i] = false;
		}
	}
	
	public int nbEdges(){
		int count = 0;
		for (int i = 0; i < nbNodes; i++)
		{
			for (int j = 0; j < nbNodes; j++)
			{
				if (this.adjacency[i][j] == true)
				{
					count++;
				}
			}
		}
		return (count/2);
		//divide by 2 since each edge is true at 2 locations given lack of orientation
		
	}
	
	public boolean cycle(int start){
		//returns true if part of cycle and false if not 
		
		//error handling for out of bounds start
		if (start >= this.nbNodes) // = since i starts at 0
		{
			return false;
		}
		if (start < 0)
		{
			return false;
		}
	
		//boolean matrix of same size to see what was visited
		//simply for iteration purposes
		boolean visitedNode[] = new boolean[this.nbNodes];
		//boolean matrix of adjacencies with node in question
		//will only check nodes it is linked to
		boolean linked[] = new boolean[this.nbNodes];
		//provided node s linked to itself and already visisted
		visitedNode[start]=true;
		linked[start]=true;
		//populate linked
//		boolean result = this.adjacency[start][start];
//		System.out.println("here"+result);
//		Boolean result2 = linked[start];
//		System.out.println("222before "+result2);
		for (int i = 0; i < this.nbNodes; i++)
		{
			if (this.adjacency[start][i])
			{
				linked[i]=true;
			}else
			{
				linked[i]=false;
			}
		}
//		System.out.println("next"+result);
//		//Boolean result2 = linked[start];
//		System.out.println("222 "+result2);
		//iterate through all nodes to check if they are in cycle with linked nodes
		boolean cycle = false;
		for (int i = 0; i < this.nbNodes; i++)
		{
			//look for cycles on all linked points (not start point)
			if (i != start && this.adjacency[i][start]) //HEREEEEE
			{
				//recursive check helper method if in cycle
				cycle = cycleCheck(i,visitedNode,linked);
				if (cycle)
				{
					//if cycle check is true, then its part of cycle
					return true;
				}
			}
		}
		return false;
	}
	
	//basically above is going to iterate through all the attached points to start
	//below then takes all the linked points, and iterates through all of their own links
	//if the linked node is also then found to be linked to ANOTHER linked node of the start
	//then automatically it must mean that it goes back to our original start
	//if not, you've marked that linked one as visited, repass that new visited into the cycle check again 
	//and keep going through each of them
	//shortest will do similar, just count as well and error handle
	
	//pseudo dumb talk
	//you take a linked guy, check all its links and if one of its links is also linked to the start
	//then they're all interconnected
	
	public boolean cycleCheck (int checkIndex, boolean[] visitedNode, boolean[] linked)
	{
		//set the node being checked as visited henceforth
		visitedNode[checkIndex] = true;
		//recursively check all the connections 
		//only not visited ones, until eventually all will be visited
		for (int i = 0; i<this.nbNodes; i++)
		{
			//checks adjacency between current i and i in cycle 
			//
			if (!visitedNode[i] && this.adjacency[checkIndex][i])
			{
				//not a visited node and is adjacent to our defined start from above
				
				if(linked[i])
				{
					return true;
				}else
				{
				cycleCheck(i, visitedNode,linked);
				}
			}
		}
		return false;
	}
	public int shortestPath(int start, int end) {
		
		//att2
		//immediately check if it isn't one of the special cases
		if(this.nbNodes <= start || this.nbNodes <= end)
		{
			return this.nbNodes+1;
		}
		if (end<0 || start<0)
		{
			return this.nbNodes+1;
		}
		//a path must have an edge
		//if start == end 
		//2 options
		//its in a loop so we calculate the loop back to itself
		//if it doesnt have that cycle, treat as error, otherwise the recursion will handle it
		if (start == end && cycle(start) == false)
		{
			return this.nbNodes+1;
		}
		//if it has a single edge back into itself 
		if (start==end && this.adjacency[start][end])
		{
			//only needs to traverse 1 single edge
			return 1;
		}
		boolean[] linked = new boolean[this.nbNodes];
		int[] visitedNode = new int[this.nbNodes];
		int tracker =0;
		//int matrix to keep track of counts
		//only want to check links so fill boolean matrix
		for (int i = 0; i<this.nbNodes; i++)
		{
			if (this.adjacency[start][i] && i != start)
			{
				linked[i]=true;
			}
			else 
			{
				linked[i]=false;
			}
		}
		
		for (int i = 0; i<this.nbNodes; i++)
		{
			//fills the visited node with the setup values to count their connections
			//will fill the faulty ones with the error handling return
			//rest with values ready to be counted with
			if (i!=0)
			{
				visitedNode[i]=this.nbNodes+1;
			}
			else 
			{
				visitedNode[i]=0;
			}
		}
		for (int i = 0; i<this.nbNodes; i++)
		{
			if (linked[i] == true)
			{
				traverse(visitedNode, i, tracker);
			}
		}
		//its values will keep getting updated until end, return what it becomes
		return visitedNode[end];
	}
	
	public void traverse(int visitedNode[], int checker, int tracker)
	{
		//tracker keeps count of the recursion
		tracker+=1;
		visitedNode[checker] = tracker;
		//update the value of the int matrix with the count every time
		//we want to now check all the further links
		//create another linked boolean matrix to explore further
		//this time with the new reference node
		boolean[] linked = new boolean[this.nbNodes];
		//populate
		for (int i =0; i<this.nbNodes;i++)
		{
			//same concept as previous links
			if (this.adjacency[i][checker] && i!=checker)
			{
				linked[i]=true;
			}
			else
			{
				linked[i]=false;
			}
		}
		//now we search through all of the checkers nodes with the recursion
		//which will ensure to keep count and add it to the int matrix
		for (int i = 0; i<this.nbNodes; i++)
		{
			//checks NOT the start (like before), all the links and avoid all the already determined "deadends"
			if (i!=checker && linked[i] == true && tracker+1 < visitedNode[i])
			{
				//recursively count through all of them, repeating the process
				traverse(visitedNode, i, tracker);
			}
		}
	}	
}
