

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Player {
	public static int playerNum;
	public final static int N = 3;
	static String[][] board = new String[N][N];
	static int[] rows = new int[N*N];
	static int[] cols = new int[N*N];
	public final static String p1Token = "X";
	public final static String p2Token = "O";
	public static int turn = 1;
	
	public static void main(String[] args){
		
		startGame();
		String action = args[0];
		//Age como servidor esperando por conexao
		if(action.toLowerCase().equals("wait")){
			
			//Jogador 1
			playerNum = 1;
			int port = Integer.parseInt(args[1]);
			
			try(
				ServerSocket serverSocket = new ServerSocket(port);
				Socket player1Socket = serverSocket.accept();
				PrintWriter out = new PrintWriter(player1Socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
			){
				
				System.out.println("Sua vez");
				String inLine, outputline;
				//inLine = "hello from player "+playerNum;
				//out.println(inLine);
				
				BufferedReader stdIn =
		                new BufferedReader(new InputStreamReader(System.in));
		        String fromP2;
		        String jogada;
		        
		        printGame();
		        
		        //Primeira jogada
		        jogada = stdIn.readLine();
                if (jogada != null) {
                    //System.out.println("Player 1: " + jogada);
                	makeMove(jogada);
                	System.out.println("Aguarde o seu turno");
                	printGame();
                    sendGameState(out);
                }
				
				while ((fromP2 = in.readLine()) != null) {
	                //System.out.println("Player 2: " + fromP2);
					updateGameState(fromP2);
                	System.out.println("Sua vez");
                	printGame();
	                if (fromP2.equals("Bye."))
	                    break;
	                
	                jogada = stdIn.readLine();
	                if (jogada != null) {
	                	makeMove(jogada);
	                	System.out.println("Aguarde o seu turno");
	                	printGame();
	                	sendGameState(out);
	                }
	            }
				
			} catch (IOException e) {
	            System.out.println("Exception caught when trying to listen on port "
	                    + port + " or listening for a connection");
	                System.out.println(e.getMessage());
	        }
		} else if(action.toLowerCase().equals("start")){
			
			//Jogador 2
			playerNum = 2;
			
			String host = args[1];
			int port = Integer.parseInt(args[2]);
			
			try(
				Socket player2Socket = new Socket(host, port);
	            PrintWriter out = new PrintWriter(player2Socket.getOutputStream(), true);
	            BufferedReader in = new BufferedReader(
	                new InputStreamReader(player2Socket.getInputStream()));	
			){
				String inLine, outputline;
				//inLine = "hello from player "+playerNum;
				//out.println(inLine);
				//read(in);
				
				BufferedReader stdIn =
		                new BufferedReader(new InputStreamReader(System.in));
		        String fromP1;
		        String jogada;
		           
		        while ((fromP1 = in.readLine()) != null) {
	                //System.out.println("Player 1: " + fromP1);
		        	updateGameState(fromP1);
                	System.out.println("Sua vez");
                	printGame();
	                if (fromP1.equals("Bye."))
	                    break;
	                
	                jogada = stdIn.readLine();
	                if (jogada != null) {
	                	makeMove(jogada);
	                	System.out.println("Aguarde o seu turno");
	                	printGame();
	                	sendGameState(out);
	                }
	            }
	                
			} catch (UnknownHostException e) {
	            System.err.println("Don't know about host " + host);
	            System.exit(1);
	        } catch (IOException e) {
	            System.err.println("Couldn't get I/O for the connection to " +
	                host);
	            System.exit(1);
	        }
		}
	}
	
	public static String read(BufferedReader in){
		String input = null;
		try {
			input = in.readLine();
			int opponent = playerNum%2 + 1;
			System.out.println("Player "+opponent+": "+input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}
	
	public static void startGame(){
		for(int i=0; i < N; i++)
			for(int j = 0; j < N; j++){
				int position = i*N + j;
				board[i][j] = Integer.toString(position);
				rows[position] = i;
				cols[position] = j;
			}
				
			
	}
	
	public static void printGame(){
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
	
	public static void printTurnMessage(){
		//if()
	}
	
	public static void makeMove(String jogada){
		int posicao = Integer.parseInt(jogada);
		String token;
		if(playerNum == 1)
			token = p1Token;
		else
			token = p2Token;
		board[rows[posicao]][cols[posicao]] = token;
	}
	
	public static void sendGameState(PrintWriter out){
		String state = "";
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				state = state + board[i][j];
		
		out.println(state);
	}
	
	public static void updateGameState(String state){
		for(int i = 0; i < N*N; i++)
			board[rows[i]][cols[i]] = String.valueOf(state.charAt(i));
	}
}
