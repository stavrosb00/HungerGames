/*Ονοματεπώνυμο : Κουρκουτίδης Αθανάσιος & ΑΕΜ : 9673 & Tel : 6945346059 & Email AUTH : kourkoua@ece.auth.gr 
 
Ονοματεπώνυμο : Σταύρος Βασίλειος Μπουλιόπουλος & ΑΕΜ : 9671 & Tel : 6943081690 & E-mail AUTH : smpoulio@ece.auth.gr*/
public class Game {
	
	private int round;

	public Game(int round) {
		this.round = round;
	}

	public Game() {
		round = 0;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public static void main(String[] args) {
		Game game = new Game();
		// Initialise board
		int N = 20;
		Board board = new Board(N, N, 6, 10, 8);
		// Initialise board limits
		int[][] wal = {
				{-2,-2},
				{2,-2},
				{2,2},
				{-2,2},
			};
		int[][] fal = {
				{-3,-3},
				{3,-3},
				{3,3},
				{-3,3},
		};
		int[][] tal = {
				{-4,-4},
				{4,-4},
				{4,4},
				{-4,4},
			};
		board.setWeaponAreaLimits(wal);
		board.setFoodAreaLimits(fal);
		board.setTrapAreaLimits(tal);
		// Generate random board
		board.createBoard();
		int jk=0;
		int sc=0;
		// Initialise players
			Player player0 =	new Player(0, "Player 0", board, 15, N/2, N/2, null, null, null);
			MinMaxPlayer player1 =new MinMaxPlayer(1, "Player 1", board, 15, N/2, N/2, null, null, null);

		// Game loop
		int maxRounds = 10000; // Just for safety
		while(maxRounds-- >= 0) {
			System.out.println("======= Round " + game.round + "===========");
			
			// Make each player move
			player0.move();
			if(player0.getScore() < 0) //Losing if he gets negative score
			{
				sc=2;
				break;
			}
			if(player0.getPistol()!=null)
			{
				if(MinMaxPlayer.kill(player1, player0, 2))
				{
					jk=2;
					break;
				}
			}
			
			player1.getNextMove(player0);
			player1.statistics();
			
			if(player1.getScore() < 0) //Losing if he gets negative score
			{
				sc=1;
				break;
			}
			if(player1.getPistol()!=null)
			{
				if(MinMaxPlayer.kill(player1, player0, 2))
				{
					jk=1;
					break;
				}
			}
			
			// Resize board
			if(game.round != 0 && game.round%3 == 0) {
				board.resizeBoard(player0, player1);
			}
			
			// Increment round counter
			game.round += 1;
			
			// Get board as string array to print
			String ss[][] = board.getStringRepresentation();
			
			// Add player positions on the board
			ss[board.x2i(player0.getX())][board.y2j(player0.getY())] = "  P0";
			ss[board.x2i(player1.getX())][board.y2j(player1.getY())] = "  P1";			
			
			// Print board
			for(int i=0; i<ss.length; i++) {
				for(int j=0; j<ss[0].length; j++) {
					System.out.print(ss[i][j]);
					System.out.print("|");
				}
				System.out.print("\n");
			}
			
			// Check end game conditions
			if(board.getM() == 4 && board.getN() == 4) {
				// End game
				break;
			}
		}
		
		// Find winner
		Player winner = null;
		if(player0.getScore() > player1.getScore()) {
			winner = player0;
		} else if(player0.getScore() < player1.getScore()) {
			winner = player1;
		}
		if(jk==1)
		{
			winner=player1;
			System.out.printf("%s killed enemy %s ! " ,player1.getName(),player0.getName());
		}
		else if(jk==2)
		{
			winner=player0;
			System.out.printf("%s killed enemy player %s ! " ,player0.getName(),player1.getName());
		}
		if(sc==1)
		{
			winner=player0;
			System.out.printf("%s lost all his points ! " ,player1.getName());	
		}
		else if(sc==2)
		{
			winner=player1;
			System.out.printf("%s lost all his points ! " ,player0.getName());
		}
		// Print game stats
		System.out.printf("Game finished: total rounds %d, scores %d %d, winner is player %s", (game.round-1), player0.getScore(), player1.getScore(), (winner!=null ? winner.getName() : "...actually, it was a tie"));
	}
	/*public static void main(String[] args) {
		Game game = new Game();
		// Initialise board
		int N = 20;
		Board board = new Board(N, N, 6, 10, 8);
		// Initialise board limits
		int[][] wal = {
				{-2,-2},
				{2,-2},
				{2,2},
				{-2,2},
			};
		int[][] fal = {
				{-3,-3},
				{3,-3},
				{3,3},
				{-3,3},
		};
		int[][] tal = {
				{-4,-4},
				{4,-4},
				{4,4},
				{-4,4},
			};
		board.setWeaponAreaLimits(wal);
		board.setFoodAreaLimits(fal);
		board.setTrapAreaLimits(tal);
		// Generate random board
		board.createBoard();
		int jk=0;
		// Initialise players
			Player player0 =	new Player(0, "Player 0", board, 0, -N/2, -N/2, null, null, null);
			HeuristicPlayer player1 =new HeuristicPlayer(1, "Player 1", board, 0, N/2, N/2, null, null, null);

		// Game loop
		int maxRounds = 10000; // Just for safety
		while(maxRounds-- >= 0) {
			System.out.println("======= Round " + game.round + "===========");
			
			// Make each player move
			player0.move();
			if(player0.getPistol()!=null)
			{
				if(HeuristicPlayer.kill(player1, player0, 2))
				{
					jk=2;
					break;
				}
			}
			
			player1.move(player0);
			player1.statistics();
			
			
			if(player1.getPistol()!=null)
			{
				if(HeuristicPlayer.kill(player1, player0, 2))
				{
					jk=1;
					break;
				}
			}
			
			// Resize board
			if(game.round != 0 && game.round%3 == 0) {
				board.resizeBoard(player0, player1);
			}
			
			// Increment round counter
			game.round += 1;
			
			// Get board as string array to print
			String ss[][] = board.getStringRepresentation();
			
			// Add player positions on the board
			ss[board.x2i(player0.getX())][board.y2j(player0.getY())] = "  P0";
			ss[board.x2i(player1.getX())][board.y2j(player1.getY())] = "  P1";			
			
			// Print board
			for(int i=0; i<ss.length; i++) {
				for(int j=0; j<ss[0].length; j++) {
					System.out.print(ss[i][j]);
					System.out.print("|");
				}
				System.out.print("\n");
			}
			
			// Check end game conditions
			if(board.getM() == 4 && board.getN() == 4) {
				// End game
				break;
			}
		}
		
		// Find winner
		Player winner = null;
		if(player0.getScore() > player1.getScore()) {
			winner = player0;
		} else if(player0.getScore() < player1.getScore()) {
			winner = player1;
		}
		if(jk==1)
		{
			winner=player1;
			System.out.printf("%s killed enemy %s ! " ,player1.getName(),player0.getName());
		}
		else if(jk==2)
		{
			winner=player0;
			System.out.printf("%s killed enemy player %s ! " ,player0.getName(),player1.getName());
		}
		// Print game stats
		System.out.printf("Game finished: total rounds %d, scores %d %d, winner is player %s", (game.round-1), player0.getScore(), player1.getScore(), (winner!=null ? winner.getName() : "...actually, it was a tie"));
	}*/

}