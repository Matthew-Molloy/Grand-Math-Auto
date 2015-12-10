package grandmathauto;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server implements Runnable {
	
	volatile public Float currentData = null;
	static int portNum = 4040;
	
	public Server() { }

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
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Client Connected");
			
			BufferedReader datain = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String input = "";
			  
			while( !(input.equals( "exit" )) ) {
				input = datain.readLine();
				currentData = Float.parseFloat(input);
				System.out.println(currentData);
				//inputNums = input.split(delims);
				input = "";
			}
			sock.close();
      } catch (IOException e) {
         System.out.println(e);
      }
	}

}
