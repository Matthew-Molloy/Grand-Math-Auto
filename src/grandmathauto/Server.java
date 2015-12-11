package grandmathauto;

import grandmathauto.Game.STATE;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server implements Runnable {
	
	volatile public Float currentData = null;
	static int portNum = 4040;
	Game game = null;
	
	public Server(Game g) {
		game = g;
	}

	@Override
	public void run() {
		try {
			// Setup server thread for sensor input
			ServerSocket ssock = null;
			Socket sock = null;
			try {
				ssock = new ServerSocket(portNum);
				System.out.println("Listening for client...");
				sock = ssock.accept();
				game.setState(STATE.INSTRUCTIONS);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Client Connected");
			
			BufferedReader datain = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String input = "";
			  
			while( !(input.equals( "exit" )) ) {
				input = datain.readLine();
				currentData = Float.parseFloat(input);
				//inputNums = input.split(delims);
				input = "";
			}
			sock.close();
      } catch (IOException e) {
         System.out.println(e);
      }
	}

}
