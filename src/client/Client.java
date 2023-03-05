package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public Client() {

	}

	public static void main(String[] args) throws IOException {

		Socket socket = new Socket("100.97.112.182", 3000);
		System.out.println("connected ...");
		PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		Scanner scanner = new Scanner(System.in);
		// send the username in the first message :
		System.out.println("enter ur username :");
		String username = scanner.nextLine();
		pw.println(username);
		new MessageHandler(br).start();
		System.out.println("start chating -----------------------------------------------------------------");
		while (true) {
			String msgToSend = scanner.nextLine();
			if (!msgToSend.equals("") || msgToSend != null)
				pw.println(msgToSend);
		}

	}

	static class MessageHandler extends Thread {
		private BufferedReader br;

		public MessageHandler(BufferedReader br) {
			this.br = br;
		}

		@Override
		public void run() {
			try {
				while (true) {
					String message = br.readLine();
					if (!message.equals("") || message != null)

						System.out.println(message);

				}

			} catch (IOException e) {
				System.out.println("error in the MessageHandler");

			}
		}
	}

}
