package servers;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.List;
import java.util.Vector;

public class ThreadedServer {

	private static List<Socket> sockets = new Vector<Socket>();

	public ThreadedServer() {

	}

	public static void main(String[] args) throws Exception {

		ServerSocket server = new ServerSocket(3000);
		System.out.println("server is listening on port 3000...");

		while (true) {
			Socket socket = server.accept();
			String username =new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
			PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
			pw.println("HI from the server");
			sockets.add(socket);

			new ClientHandler(socket , username).start();
		}

	}
	// ----------------------------BroadCastHandler-------------------

	static class Broadcast extends Thread {

		private String message;
		private Socket socket;

		public Broadcast(Socket socket,String message) {
			this.message = message;
			this.socket=socket;
		}

		@Override
		public void run() {
			try {
				for (Socket socket : sockets) {
					if (socket !=this.socket) {
						
						PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
						pw.println(this.message);
					}
				}
			} catch (IOException e) {
				System.out.println("error in the broadCastHandler");
			}
		}
	}

	// ----------------------------ClientHandler-------------------
	static class ClientHandler extends Thread {
		private Socket socket;
		private String username ;
		public ClientHandler(Socket socket, String username) {
			this.socket = socket;
			this.username = username;

		}
		
		@Override
		public void run() {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				while (true) {
					String msg = br.readLine();
					new Broadcast(this.socket,"->"+username+" : "+msg).start();
				}
			} catch (IOException e) {
				System.out.println(username+" disconnected ... ");

			}
		}
	}

}
