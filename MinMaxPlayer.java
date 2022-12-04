/*Ονοματεπώνυμο : Κουρκουτίδης Αθανάσιος & ΑΕΜ : 9673 & Tel : 6945346059 & Email AUTH : kourkoua@ece.auth.gr 
 
Ονοματεπώνυμο : Σταύρος Βασίλειος Μπουλιόπουλος & ΑΕΜ : 9671 & Tel : 6943081690 & E-mail AUTH : smpoulio@ece.auth.gr*/
import java.lang.Math;
import java.util.ArrayList;
public class MinMaxPlayer extends Player{
	private ArrayList<Integer[] > path;       
	private static int r = 3 ;
	private int h_round;
	public MinMaxPlayer()
	{
		super();
		h_round = 0;
		path = new ArrayList<Integer[] >(); //Size=10
	}
	
	public MinMaxPlayer(int id, String name, Board board, int score, int x, int y, Weapon bow, Weapon pistol, Weapon sword)
	{
		super(id, name, board, score, x, y, bow, pistol, sword);
		h_round = 0;
		path = new ArrayList<Integer[] >(40 ); //For 40rounds limit ,then increasing according to formula :newCapacity = oldCapacity + (oldCapacity >> 1) (JDK8) . 
											   //Integer[] usually includes 11 integer elements . 0:dice code , 1: previous X , 2:previous Y , 3: new X , 4: new Y , 5 :points gained
											   //6:weapons collected , 7:foods collected , 8 : traps collected , 9 : weapon type , 10: trap type 
	}
	public MinMaxPlayer(MinMaxPlayer p)
	{
		super(p);
		h_round = p.h_round;
		path = p.path;

	}
	public ArrayList<Integer[] > getPath()
	{
		return path;
	}
	
	public void setPath(ArrayList<Integer[] > path )
	{
		this.path = path;
	}
	
	public  int getR()
	{
		return r;
	}
	
	public int getH_round()
	{
		return h_round;
	}
	
	public void setH_round(int h_round)
	{
		this.h_round = h_round;
	}

	public void boardSetup()
	{
		int newx = getX();
		int newy = getY();
		Weapon w = null;
		Food f = null;
		Trap t = null;
		// Check if there is a weapon on the new tile
		for(int i=0; i<getBoard().getWeapons().length; i++) {
			if(getBoard().getWeapons()[i].getX() == newx && 
				getBoard().getWeapons()[i].getY() == newy &&
				getBoard().getWeapons()[i].getPlayerId() == getId() ) {
				w = getBoard().getWeapons()[i];
				if(w.getType() == "sword") {
					setSword(w);
				} else if(w.getType() == "bow") {
					setBow(w);
				} else if(w.getType() == "pistol") {
					setPistol(w);
				}
				w.setX(0);
				w.setY(0);
			}
		}
		// Check if there is food on the new tile
		for(int i=0; i<getBoard().getFood().length; i++) {
			if(getBoard().getFood()[i].getX() == newx && getBoard().getFood()[i].getY() == newy) {
				f = getBoard().getFood()[i];
				setScore(getScore() + f.getPoints());
				f.setX(0);
				f.setY(0);
			}
		}
		// Check if there is a trap on the new tile
		for(int i=0; i<getBoard().getTraps().length; i++) {
			if(getBoard().getTraps()[i].getX() == newx && getBoard().getTraps()[i].getY() == newy) {
				t = getBoard().getTraps()[i];
				boolean avoided = false;
				if(t.getType() == "ropes") {
					if(getSword() != null) {
						avoided = true;
					}
				} else if(t.getType() == "animals") {
					if(getBow() != null) {
						avoided = true;
					}
				}
				if(!avoided) {
					setScore(getScore() + t.getPoints());
				} 
			}
		}

	}
	public int absolute(int k)
	{
		int v=k;
		if(v>0)
			return v;
		return -v;
	}
	public float playersDistance(Player p)
	{
		float di = 0; //Distance
		if(getX()*p.getX()>0 && getY()*p.getY()>0) 		//ίδιο τεταρτημόριο
		{
			di = (float) Math.sqrt((Math.pow(getX()-p.getX(),2)+Math.pow(getY()-p.getY(),2)));
		}
		else if(getX()*p.getX()<0 && getY()*p.getY()>0) //ετεροσημο Χ , αγνοω 0 καθετο αξονα Χ
			di = (float) Math.sqrt((Math.pow(absolute(getX()-p.getX())-1,2)+Math.pow(getY()-p.getY(),2)));
		else if(getX()*p.getX()>0 && getY()*p.getY()<0) //ετεροσημο Υ , αγνοω 0 οριζοντιο αξονα Υ
			di = (float) Math.sqrt((Math.pow(getX()-p.getX(),2)+Math.pow(absolute(getY()-p.getY())-1,2)));
		else											//ετεροσημα Χ,Υ , αγνοω μηδενικους αξονες Χ,Υ
			di = (float) Math.sqrt((Math.pow(absolute(getX()-p.getX())-1,2)+Math.pow(absolute(getY()-p.getY())-1,2)));
		if (r > (int) di)
			return di; //Returns distance if enemy in vision
		return -1;	   //Returns -1 if enemy not in vision
	}
	/*
	 * under maintenance strategic solutions : rushing center,turboscan , camping on table mid 
	public int stepDistanceBoxes(int x1, int y1 , int x2,int y2)
	{
		int d = 0;
		int bx1 = x1;
		int by1 = y1;
		int bx2 = x2;
		int by2 = y2;
		
		
		while(true)
		{
			if(bx2 > bx1)
			{
				bx2--;
				if(bx2==0)
				{
					bx2--;
				}
				
			}
			else if(bx2 < bx1)
			{
				bx2++;
				if(bx2==0)
				{
					bx2++;
				}
				
			}
			
			if(by2 > by1)
			{
				by2--;
				if(by2==0)
				{
					by2--;
				}
				
			}
			
			else if(by2 < by1)
			{
				by2++;
				if(by2==0)
				{
					by2++;
				}
				
			}
			d++;
			if(bx1==bx2 && bx1==bx2)
			{
				return d;
			}
			
		}
		
	}
	public int evaluateTurboScan()
	{
		int dice =0;
		return dice;
	}
	*/
	///////////////////////////////////
	public double evaluate(int dice, Player p)
	{
		double gainWeapons=0;
		double gainPoints=0;
		double avoidTraps=0;
		double forceKill=0;
		double avoidDeath=0;
		double aim=0;
		
		MinMaxPlayer tempPlayer=new MinMaxPlayer();
		
		tempPlayer.setX(getX());
		tempPlayer.setY(getY());
		
		int moves[][] = {
				{0,-1},
				{1,-1},
				{1,0},
				{1,1},
				{0,1},
				{-1,1},
				{-1,0},
				{-1,-1},
		};
		
		tempPlayer.setX(getX() + moves[dice][0]);
	
		//avoid x==0
		if(tempPlayer.getX()==0)
		{
			tempPlayer.setX(tempPlayer.getX()+ moves[dice][0]);
		}
		
		tempPlayer.setY(getY() + moves[dice][1]);
		
		//avoid y==0
		if(tempPlayer.getY()==0)
		{
			tempPlayer.setY(tempPlayer.getY()+ moves[dice][1]);
		}
		
		
		//check if there is a weapon on these coordinates
		for(int i=0; i<getBoard().getW();i++)
		{
			if(tempPlayer.getX()==getBoard().getWeapons()[i].getX() && tempPlayer.getY()==getBoard().getWeapons()[i].getY() && getId()== getBoard().getWeapons()[i].getPlayerId())
			{
				if(getBoard().getWeapons()[i].getType().equals("pistol"))
				{
					gainWeapons=100.0;	
					tempPlayer.setPistol(getBoard().getWeapons()[i]);
				}
				else
				{
					gainWeapons=20.0;
				}
				
				break;

			}
			
		}
		
		//check if there is food on these coordinates
		for(int i=0; i<getBoard().getF();i++)
		{
			if(tempPlayer.getX()==getBoard().getFood()[i].getX() && tempPlayer.getY()==getBoard().getFood()[i].getY())
			{
				gainPoints=getBoard().getFood()[i].getPoints();
				break;
			}
					
		}
		
		//check if there is a trap on these coordinates
		for(int i=0; i<getBoard().getT();i++)
		{
			if(tempPlayer.getX()==getBoard().getTraps()[i].getX() && tempPlayer.getY()==getBoard().getTraps()[i].getY())
			{
				if((getBow()!=null && getBoard().getTraps()[i].getType().equals("animals")) || (getSword()!=null && getBoard().getTraps()[i].getType().equals("ropes")))
				{
					avoidTraps=0;
					break;
				}
				else
				{
					avoidTraps=getBoard().getTraps()[i].getPoints();
					break;
				}
				
				
			}
							
		}
		
		//check if the other player is nearby part1
		if((tempPlayer.playersDistance(p) < r-1 && tempPlayer.playersDistance(p)!=-1)  && p.getPistol()!=null)
		{
				avoidDeath=-1000.0;
		}
		
		
		//check if the other player is nearby part2
		if((tempPlayer.playersDistance(p) < r-1 && tempPlayer.playersDistance(p)!=-1) &&( (this.getPistol()!=null) ||tempPlayer.getPistol()!=null ))
		{
			forceKill=10000.0;
		}
		 
		aim = gainWeapons + gainPoints + avoidTraps + forceKill + avoidDeath;
		return aim;
	}
	
	public void createMySubtree(Node root, int depth, MinMaxPlayer me, Player opponent) 
	{
		int possibleMoves = 8;
		boolean isXEdge = me.getX() == -root.getNodeBoard().getN()/2 || me.getX() == root.getNodeBoard().getN()/2;
		boolean isYEdge = me.getY() == -root.getNodeBoard().getM()/2 || me.getY() == root.getNodeBoard().getM()/2;
		if(isXEdge && isYEdge) {
			possibleMoves = 3;
			} 
		else if(isXEdge || isYEdge) {
			possibleMoves = 5;
			}
		MinMaxPlayer tempPlayer = new MinMaxPlayer(me);
		Player tempOpPlayer = new Player(opponent);
		ArrayList<Node> tempRoot = new ArrayList<Node>(possibleMoves); //children of main root
		int moves[][] = {
				{0,-1},
				{1,-1},
				{1,0},
				{1,1},
				{0,1},
				{-1,1},
				{-1,0},
				{-1,-1},
		};
		int tiles[][] = new int[possibleMoves][2];
		int counter = 0;
		int valid_dices[] = new int[possibleMoves];
		for(int k=0; k<8; k++) {          //k : 0-7 , moves 
			int newx = tempPlayer.getX() + moves[k][0];
			int newy = tempPlayer.getY() + moves[k][1];
			// Skip row 0 and column 0
			if(newx == 0) {
				newx += moves[k][0];
			}
			if(newy == 0) {
				newy += moves[k][1];
			}
			if(tempPlayer.getBoard().isPositionValid(newx, newy)) {
				tiles[counter][0] = newx;
				tiles[counter][1] = newy;
				valid_dices[counter] = k; //Dices code
				counter++;
				
			}
		}
		int move[] = new int[3]; //0 : x , 1 : y , 2 : dice code
		for(int i=0 ; i < possibleMoves ; i++)
		{
			tempPlayer = new MinMaxPlayer(me);
			tempOpPlayer = new Player(opponent);
		tempRoot.add(i ,new Node(root));
		tempRoot.get(i).setParent(root);
		tempRoot.get(i).setNodeEvaluation(tempPlayer.evaluate(valid_dices[i], tempOpPlayer));
		
		tempPlayer.setX(tiles[i][0]);
		move[0] = tempPlayer.getX();
		tempPlayer.setY(tiles[i][1]);
		move[1] = tempPlayer.getY();
	
		tempPlayer.boardSetup();
		tempOpPlayer.boardSetup();
		tempRoot.get(i).setNodeBoard(new Board(tempPlayer.getBoard()));
		move[2] = valid_dices[i];
		tempRoot.get(i).setNodeDepth(depth);
		tempRoot.get(i).setNodeMove(move);
		createOpponentSubtree( tempRoot.get(i) , depth+1 , tempPlayer , tempOpPlayer);
		}
		root.setChildren(tempRoot);
		
	}
		
	
	public void createOpponentSubtree(Node parent, int depth, MinMaxPlayer me, Player opponent) 
	{
		int possibleMoves2 = 8;
		boolean isXEdge = opponent.getX() == -parent.getNodeBoard().getN()/2 || opponent.getX() == parent.getNodeBoard().getN()/2;
		boolean isYEdge = opponent.getY() == -parent.getNodeBoard().getM()/2 || opponent.getY() == parent.getNodeBoard().getM()/2;
		if(isXEdge && isYEdge) {
			possibleMoves2 = 3;
			} 
		else if(isXEdge || isYEdge) {
			possibleMoves2 = 5;
			}
		MinMaxPlayer tempPlayer = new MinMaxPlayer(me);
		Player tempOpPlayer = new Player(opponent);
		ArrayList<Node> tempParent = new ArrayList<Node>(possibleMoves2); //children of main parent
		int moves[][] = {
				{0,-1},
				{1,-1},
				{1,0},
				{1,1},
				{0,1},
				{-1,1},
				{-1,0},
				{-1,-1},
		};
		int tiles[][] = new int[possibleMoves2][2];
		int counter = 0;
		int valid_dices[] = new int[possibleMoves2];
		for(int k=0; k<8; k++) {          //k : 0-7 , moves 
			int newx = tempOpPlayer.getX() + moves[k][0];
			int newy = tempOpPlayer.getY() + moves[k][1];
			// Skip row 0 and column 0
			if(newx == 0) {
				newx += moves[k][0];
			}
			if(newy == 0) {
				newy += moves[k][1];
			}
			if(tempOpPlayer.getBoard().isPositionValid(newx, newy)) {
				tiles[counter][0] = newx;
				tiles[counter][1] = newy;
				valid_dices[counter] = k; //Dices code
				counter++;
				
			}
		}
		int move[] = new int[3]; //0 : x , 1 : y , 2 : dice code
		for(int i=0 ; i < possibleMoves2 ; i++)
		{
			tempPlayer = new MinMaxPlayer(me);
			tempOpPlayer = new Player(opponent);
		tempParent.add(i ,new Node(parent));
		tempParent.get(i).setParent(parent);
		tempParent.get(i).setNodeEvaluation(tempParent.get(i).getNodeEvaluation() - tempOpPlayer.evaluate(valid_dices[i], tempPlayer));

		tempOpPlayer.setX(tiles[i][0]);
		move[0] = tempOpPlayer.getX();
		tempOpPlayer.setY(tiles[i][1]);
		move[1] = tempOpPlayer.getY();
		
		tempPlayer.boardSetup(); //Last tree level ,so doesn't matter
		tempOpPlayer.boardSetup();//Last tree level ,so doesn't matter
		tempParent.get(i).setNodeBoard(new Board(tempOpPlayer.getBoard())); //Same^^
		move[2] = valid_dices[i];
		tempParent.get(i).setNodeDepth(depth);
		tempParent.get(i).setNodeMove(move);
		}
		parent.setChildren(tempParent);
		
	}
	
	public int chooseMinMaxMove( Node root)
	{
		int dice =0;
		int max1 = 0;
		double m1 =0;
		double m2 ;
		int cr = 0; //Counter of rejected moves
		int rejected_dices[] = new int[root.getChildren().size()];
		for(int i =0 ; i<root.getChildren().size() ; i++)
		{	
			
			m2 = root.getChildren().get(i).getChildren().get(0).getNodeEvaluation();
			for(int j=0 ; j <root.getChildren().get(i).getChildren().size(); j++)
			{
				
				if(m2 > root.getChildren().get(i).getChildren().get(j).getNodeEvaluation()) //Minimum of Level2
				{
					m2 = root.getChildren().get(i).getChildren().get(j).getNodeEvaluation();
				}
			}
			if(0 > root.getChildren().get(i).getNodeEvaluation())
			{
				rejected_dices[cr] = root.getChildren().get(i).getNodeMove()[2];
				cr++;
			}
			root.getChildren().get(i).setNodeEvaluation(m2); //Minimum values go up to level1
			m1 = root.getChildren().get(0).getNodeEvaluation();
			max1 = root.getChildren().get(0).getNodeMove()[2];
			
			if(m1 < root.getChildren().get(i).getNodeEvaluation()) //Maximum of Level1
			{
				m1 = root.getChildren().get(i).getNodeEvaluation();
				max1 = root.getChildren().get(i).getNodeMove()[2];
			}
		

		}
		if(m1 == 0 && cr!=root.getChildren().size())
		{
			//We can put move to central zone here or rush to find a weapon, but whatever
			int i = 0;
			
			while(true)
			{
				int newPos[] = getRandomMove(getX(), getY());
				dice = newPos[2];
				while(i < cr)
				{
					if(dice == rejected_dices[i])
					{
						break;
					}
					i++;
				}
				if(i == cr)
				{
					break;
				}
			}
		}
		else
		{
			
			dice = max1;
		}
		return dice;
	}
	
	public static boolean kill(Player player1, Player player2, float d)
	{
		if(( ((MinMaxPlayer)player1).playersDistance(player2) < d )&& (((MinMaxPlayer)player1).playersDistance(player2)!=-1) )   //Typecast 
		{
			return true;
		}
		return false;
	}
	/////////
	public int[] getNextMove (Player p)
	{
		int newx;
		int newy;
		int dice;
		// Find number of possible moves
		int possibleMoves = 8;
		boolean isXEdge = getX() == -getBoard().getN()/2 || getX() == getBoard().getN()/2;
		boolean isYEdge = getY() == -getBoard().getM()/2 || getY() == getBoard().getM()/2;
		if(isXEdge && isYEdge) {
			possibleMoves = 3;
			} 
		else if(isXEdge || isYEdge) {
			possibleMoves = 5;
			}
		int moves[][] = {
				{0,-1},
				{1,-1},
				{1,0},
				{1,1},
				{0,1},
				{-1,1},
				{-1,0},
				{-1,-1},
		};
		Node Root = new Node(null , possibleMoves , 0 , null ,new Board(getBoard()) , 0 );
		//(Node parent,int n,int nodeDepth,int[] nodeMove,Board nodeBoard,double nodeEvaluation) System.out.println(" hi ");
		createMySubtree(Root, 1, this , p);
		dice = chooseMinMaxMove(Root);
		newx = getX() + moves[dice][0];
		newy = getY() + moves[dice][1];
		// Skip row 0 and column 0
		if(newx == 0) {
			newx += moves[dice][0];
		}
		if(newy == 0) {
			newy += moves[dice][1];
		}
		
		////////
		Integer details[] = new Integer[11];
		//Initialized to zero value  by java : Default 
		//Includes 11 integer elements . 0:dice code( 0<->7) , 1: previous X , 2:previous Y , 3: new X , 4: new Y , 5 :points gained
		//6:weapons collected(0 or 1) , 7:foods collected(0 or 1) , 8 : traps avoided ( 1= avoided 2= lost points on that) , 9 : weapon type( 0 or 1 or 2) , 10: trap type( 0 or 1)
		details[0] = dice ; 
		details[1] = getX();
		details[2] = getY();
		details[3] = newx;
		details[4] = newy;
		details[5] = 0;
		details[6] = 0;
		details[7] = 0;
		details[8] = 0;
		details[9] = 0;
		details[10] = 0;
		Weapon w = null;
		Food f = null;
		Trap t = null;
		// Check if there is a weapon on the new tile
		for(int i=0; i<getBoard().getWeapons().length; i++) {
			if(getBoard().getWeapons()[i].getX() == newx && 
				getBoard().getWeapons()[i].getY() == newy &&
				getBoard().getWeapons()[i].getPlayerId() == getId() ) {
				w = getBoard().getWeapons()[i];
				details[6] = 1;
				if(w.getType() == "sword") {
					setSword(w);
					details[9] = 2;
				} else if(w.getType() == "bow") {
					setBow(w);
					details[9] = 1;
				} else if(w.getType() == "pistol") {
					setPistol(w);
					details[9] = 0;
				}
				w.setX(0);
				w.setY(0);
			}
		}
		// Check if there is food on the new tile
		for(int i=0; i<getBoard().getFood().length; i++) {
			if(getBoard().getFood()[i].getX() == newx && getBoard().getFood()[i].getY() == newy) {
				f = getBoard().getFood()[i];
				details[5] = f.getPoints();
				details[7] = 1;
				setScore( getScore() + f.getPoints());
				f.setX(0);
				f.setY(0);
			}
		}
		// Check if there is a trap on the new tile
		for(int i=0; i<getBoard().getTraps().length; i++) {
			if(getBoard().getTraps()[i].getX() == newx && getBoard().getTraps()[i].getY() == newy) {
				t = getBoard().getTraps()[i];
				boolean avoided = false;
				if(t.getType() == "ropes") {
					details[10] = 0;
					if(getSword() != null) {
						avoided = true;
					}
				} else if(t.getType() == "animals") {
					details[10] = 1;
					if(getBow() != null) {
						avoided = true;
					}
				}
				if(!avoided) {
					details[5] = t.getPoints();
					details[8] = 1;
					setScore(getScore() + t.getPoints());
				} else {
					details[8] = 2;
					
				}
			}
		}
		
		setX(newx);
		setY(newy);
		path.add(h_round , details);
		
		int newMove[] = new int[2];
		newMove[0] = newx;
		newMove[1] = newy;
		return newMove;
	}
	/////////
	public void statistics() //Prints: dice code,prevpos,newpos , points gainining 
	{
		Integer data[] = new Integer[11]; 
		data = path.get(h_round);
		System.out.printf("\nPlayer %d on round %d got %d die number . Previous position : (%d,%d) New position : (%d,%d) \n" ,getId() , h_round , data[0]+1 ,data[1] , data[2] ,data[3],data[4]);
		h_round++; //Called in every round makes it like suitable game.round counter 
		String wtypes[] = {
				"pistol",
				"bow",
				"sword"
		};
		String wt = " ";
		String ttypes[] = {
				"ropes",
				"animals"
		};
		String tt = " ";
		if(data[6] == 1)
		{
			switch(data[9])
			{
			case 0:
				wt = wtypes[0];
				break;
			case 1:
				wt = wtypes[1];
				break;
			case 2:
				wt = wtypes[2];
				break;
			}
			System.out.printf(" Found a weapon type of : %s  \n", wt );
		}
		if(data[7] == 1)
		{
			System.out.printf(" Found food and won %d points . \n" , data[5]);
		}
		
		if(data[8] == 1)
		{
			switch(data[10])
			{
			case 0:
				tt = ttypes[0];
				break;
			case 1:
				tt = ttypes[1];
				break;
			default:
				break;
			}
			System.out.printf(" Fell into the trap of %s . Lost %d points .\n", tt , -data[5] );
		}
		else if(data[8] ==2)
		{
			switch(data[10])
			{
			case 0:
				tt = ttypes[0];
				break;
			case 1:
				tt = ttypes[1];
				break;
			default:
				break;
			}
			System.out.printf(" Succesfully avoided trap of %s \n", tt);
		}
			
	}
	
	
}





