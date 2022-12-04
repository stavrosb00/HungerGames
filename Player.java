/*Ονοματεπώνυμο : Κουρκουτίδης Αθανάσιος & ΑΕΜ : 9673 & Tel : 6945346059 & Email AUTH : kourkoua@ece.auth.gr 
 
Ονοματεπώνυμο : Σταύρος Βασίλειος Μπουλιόπουλος & ΑΕΜ : 9671 & Tel : 6943081690 & E-mail AUTH : smpoulio@ece.auth.gr*/
import java.util.Random;
public class Player {

	private int id;
	private String name;
	private Board board;
	private int score;
	private int x;
	private int y;
	private Weapon bow;
	private Weapon pistol;
	private Weapon sword;
	
	public Player() {
		id = -1;
		name = "";
		board = null;
		score = 0;
		x = 0;
		y = 0;
		bow = null;
		pistol = null;
		sword = null;
	}

	public Player(int id, String name, Board board, int score, int x, int y, Weapon bow, Weapon pistol, Weapon sword) {
		this.id = id;
		this.name = name;
		this.board = board;
		this.score = score;
		this.x = x;
		this.y = y;
		this.bow = bow;
		this.pistol = pistol;
		this.sword = sword;
	}
	
	public Player(Player p)
	{
		id =p.id;
		name =p.name; 
		board =new Board(p.board); 
		score =p.score; 
		x =p.x; 
		y =p.y; 
		bow =p.bow; 
		pistol =p.pistol; 
		sword =p.sword; 
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) { 
		this.score = score;		
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Weapon getBow() {
		return bow;
	}

	public void setBow(Weapon bow) {
		this.bow = bow;
	}

	public Weapon getPistol() {
		return pistol;
	}

	public void setPistol(Weapon pistol) {
		this.pistol = pistol;
	}

	public Weapon getSword() {
		return sword;
	}

	public void setSword(Weapon sword) {
		this.sword = sword;
	}
	

	public int[] getRandomMove(int x, int y) {
		// Find number of possible moves
		int possibleMoves = 8;
		boolean isXEdge = x == -board.getN()/2 || x == board.getN()/2;
		boolean isYEdge = y == -board.getM()/2 || y == board.getM()/2;
		if(isXEdge && isYEdge) {
			possibleMoves = 3;
		} else if(isXEdge || isYEdge) {
			possibleMoves = 5;
		}
		
		// Get random die value
		Random r = new Random();
		int die = r.nextInt(possibleMoves);

		// Find next tile based on the die
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
		for(int i=0; i<8; i++) {
			int newx = x + moves[i][0];
			int newy = y + moves[i][1];
			// Skip row 0 and column 0
			if(newx == 0) {
				newx += moves[i][0];
			}
			if(newy == 0) {
				newy += moves[i][1];
			}
			if(board.isPositionValid(newx, newy)) {
				tiles[counter][0] = newx;
				tiles[counter][1] = newy;
				valid_dices[counter] = i;
				counter++;
			}
		}
		int randMove[] = new int[3]; //New added features
		randMove[0] = tiles[die][0];
		randMove[1] = tiles[die][1];
		randMove[2] = valid_dices[die]; //Dice move code
		return randMove;
	}

	public int[] move() {
		int newPos[] = getRandomMove(x, y);
		int newx = newPos[0];
		int newy = newPos[1];
		Weapon w = null;
		Food f = null;
		Trap t = null;
		// Check if there is a weapon on the new tile
		for(int i=0; i<board.getWeapons().length; i++) {
			if(board.getWeapons()[i].getX() == newx && 
			   board.getWeapons()[i].getY() == newy &&
			   board.getWeapons()[i].getPlayerId() == id ) {
				w = board.getWeapons()[i];
				System.out.println("Found a weapon");
				if(w.getType() == "sword") {
					this.sword = w;
				} else if(w.getType() == "bow") {
					this.bow = w;
				} else if(w.getType() == "pistol") {
					this.pistol = w;
				}
				w.setX(0);
				w.setY(0);
			}
		}
		// Check if there is food on the new tile
		for(int i=0; i<board.getFood().length; i++) {
			if(board.getFood()[i].getX() == newx && board.getFood()[i].getY() == newy) {
				f = board.getFood()[i];
				System.out.println("Found food");
				this.score += f.getPoints();
				f.setX(0);
				f.setY(0);
			}
		}
		// Check if there is a trap on the new tile
		for(int i=0; i<board.getTraps().length; i++) {
			if(board.getTraps()[i].getX() == newx && board.getTraps()[i].getY() == newy) {
				t = board.getTraps()[i];
				boolean avoided = false;
				if(t.getType() == "ropes") {
					if(sword != null) {
						avoided = true;
					}
				} else if(t.getType() == "animals") {
					if(bow != null) {
						avoided = true;
					}
				}
				if(!avoided) {
					System.out.printf("Player %d (Trap) | prevscore %d, newscore %d\n", id, this.score, this.score+t.getPoints());
					this.score += t.getPoints();
				} else {
					System.out.printf("Player %d (Trap) | successfully avoided", id);
				}
			}
		}
		
		System.out.printf("Player %d (Move) | prevpos %d %d, newpos %d %d\n", id, this.x, this.y, newx, newy);
		this.x = newx;
		this.y = newy;
		return new int[] {newx, newy, w!=null?1:0, f!=null?1:0, t!=null?1:0};
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
	public double evaluate(int dice, MinMaxPlayer p)
	{
		double gainWeapons=0;
		double gainPoints=0;
		double avoidTraps=0;
		double forceKill=0;
		double avoidDeath=0;
		double aim=0;
		
		Player tempPlayer=new Player();
		
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
		if((tempPlayer.playersDistance(p) < p.getR()-1 && tempPlayer.playersDistance(p)!=-1)  && p.getPistol()!=null)
		{
				avoidDeath=-1000.0;
		}
		
		
		//check if the other player is nearby part2
		if((tempPlayer.playersDistance(p) < p.getR()-1 && tempPlayer.playersDistance(p)!=-1) &&( (this.getPistol()!=null) ||tempPlayer.getPistol()!=null ))
		{
			forceKill=10000.0;
		}
		 
		aim = gainWeapons + gainPoints + avoidTraps + forceKill + avoidDeath;
		return aim;
	}
	public int absolute(int k)
	{
		int v=k;
		if(v>0)
			return v;
		return -v;
	}
	public float playersDistance(MinMaxPlayer p)
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
		if (p.getR() > (int) di)
			return di; //Returns distance if enemy in vision
		return -1;	   //Returns -1 if enemy not in vision
	}
}
