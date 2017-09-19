

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Player {
	public static int playerNum;
	public static void main(String[] args){
		
		
		/*
		if (args.length != 2) {
            System.err.println("Correto: java Player <wait> <port number>");
            System.exit(1);
        }
        */
		
		String action = args[0];
		//Age como servidor esperando por conexao
		if(action.toLowerCase().equals("wait")){
			
			//Jogador 1
			playerNum = 1;
			int port = Integer.parseInt(args[1]);
			
			try(
				ServerSocket serverSocket = new ServerSocket(port);
				Socket clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			){
				String inLine, outputline;
				inLine = "hello from player "+playerNum;
				out.println(inLine);
				
				BufferedReader stdIn =
		                new BufferedReader(new InputStreamReader(System.in));
		        String fromP2;
		        String jogada;
				
				while ((fromP2 = in.readLine()) != null) {
	                System.out.println("Server: " + fromP2);
	                if (fromP2.equals("Bye."))
	                    break;
	                
	                jogada = stdIn.readLine();
	                if (jogada != null) {
	                    System.out.println("Client: " + jogada);
	                    out.println(jogada);
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
				inLine = "hello from player "+playerNum;
				out.println(inLine);
				
				BufferedReader stdIn =
		                new BufferedReader(new InputStreamReader(System.in));
		        String fromP1;
		        String jogada;
		           
		        while ((fromP1 = in.readLine()) != null) {
	                System.out.println("Server: " + fromP1);
	                if (fromP1.equals("Bye."))
	                    break;
	                
	                jogada = stdIn.readLine();
	                if (jogada != null) {
	                    System.out.println("Client: " + jogada);
	                    out.println(jogada);
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
}
