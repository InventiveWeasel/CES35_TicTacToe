
public class Player {
	
	public static void main(String[] args) {
		
		if (args.length < 2) {
			System.out.println("Quantidade insuficiente de argumentos.");
			System.exit(1);
		}
		
		String action = args[0];
		int port = Integer.parseInt(args[1]);
		
		Game game = null;
		
		// Age como servidor esperando por conexao
		if (action.toLowerCase().equals("wait")) {
			game = new Game(1, port, null);
		}
		
		else if (action.toLowerCase().equals("start")) {
			if (args.length < 3) {
				System.out.println("Quantidade insuficiente de argumentos.");
				System.exit(1);
			}
			
			String host = args[2];
			game = new Game(2, port, host);
		}
		
		else {
			System.out.println("Argumento invalido: " + action);
			System.exit(1);
		}
		
		game.run();
	}
	
}
