import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Game {
	
	public final int N = 3;
	public final static String p1Token = "X";
	public final static String p2Token = "O";
	
	private String[][] board = new String[N][N];
	private int[] rows = new int[N*N];
	private int[] cols = new int[N*N];
	
	private int playerNum;
	private int port;
	private String host;
	
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	
	private BufferedReader stdIn = null;
	private BufferedReader otherPlayerIn = null;
	private PrintWriter otherPlayerOut = null;
	
	public Game(int playerNum, int port, String host){
		this.playerNum = playerNum;
		this.port = port;
		this.host = host;
	}
	
	private void setStreams() throws IOException {
		if (host == null) {
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
		}
		
		else {
			socket = new Socket(host, port);
		}
		
		otherPlayerIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		otherPlayerOut = new PrintWriter(socket.getOutputStream(), true);
		stdIn = new BufferedReader(new InputStreamReader(System.in));
	}
	
	private void clearBoard() {
		for(int i=0; i < N; i++)
			for(int j = 0; j < N; j++) {
				int position = i*N + j;
				board[i][j] = Integer.toString(position);
				rows[position] = i;
				cols[position] = j;
			}
	}
	
	private void printBoard() {
		System.out.println("      ###################");
		System.out.println("      #     #     #     #");
		System.out.println("      #  "+board[0][0]+"  #  "+board[0][1]+"  #  "+board[0][2]+"  #");
		System.out.println("      #     #     #     #");
		System.out.println("      ###################");
		System.out.println("      #     #     #     #");
		System.out.println("      #  "+board[1][0]+"  #  "+board[1][1]+"  #  "+board[1][2]+"  #");
		System.out.println("      #     #     #     #");
		System.out.println("      ###################");
		System.out.println("      #     #     #     #");
		System.out.println("      #  "+board[2][0]+"  #  "+board[2][1]+"  #  "+board[2][2]+"  #");
		System.out.println("      #     #     #     #");
		System.out.println("      ###################");
	}
	
	public void run() {
		clearBoard();
		
		try {
			setStreams();
			String fromOtherPlayer;
			
			// Primeira jogada
			if (playerNum == 1) {
				printBoard();
				playTurn();
			}
			
			while ((fromOtherPlayer = otherPlayerIn.readLine()) != null) {
				updateGameState(fromOtherPlayer);
				
				System.out.println("Sua vez");
	        	printBoard();
	            
	        	if (fromOtherPlayer.equals("Bye."))
	                break;
	            
	            playTurn();
			}
			
			close();
		}
		
		catch (IOException e) {
		    System.out.println("Exception caught when trying to listen on port "
		            + port + " or listening for a connection");
		    System.out.println(e.getMessage());
		}
	}
	
	private void playTurn() throws IOException {
		boolean finish_turn = false;
		
		while (!finish_turn) {
			String jogada = stdIn.readLine();
			
			if (jogada != null) {
				int posicao = Integer.parseInt(jogada);
				
				if (posicao < 0 || posicao > 8) {
					System.out.println("Entrada invalida");
				}
				
				else if (board[rows[posicao]][cols[posicao]].equals(p1Token) ||
					board[rows[posicao]][cols[posicao]].equals(p2Token)) {
					System.out.println("Posicao ja ocupada");
				}
				
				else {
					String token = (playerNum == 1) ? p1Token : p2Token;
					board[rows[posicao]][cols[posicao]] = token;
					finish_turn = true;
				}
			}
		}
		
		printBoard();
		
		System.out.println("Aguarde o seu turno");
		sendGameState();
	}
	
	private void sendGameState() {
		String state = "";
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				state = state + board[i][j];
		
		otherPlayerOut.println(state);
	}
	
	private void updateGameState(String state) {
		for(int i = 0; i < N*N; i++)
			board[rows[i]][cols[i]] = String.valueOf(state.charAt(i));
	}
	
	private void close() throws IOException {
		if (stdIn != null) stdIn.close();
		if (otherPlayerIn != null) otherPlayerIn.close();
		if (otherPlayerOut != null) otherPlayerOut.close();
		if (socket != null) socket.close();
		if (serverSocket != null) serverSocket.close();
	}

}
