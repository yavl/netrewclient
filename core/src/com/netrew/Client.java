package com.netrew;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.raw.RawChel;
import com.raw.Request;
import com.netrew.requests.RequestHandler;
import com.netrew.ui.GameHud;
import com.netrew.ui.MainMenu;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Client {
	private Socket socket;
	public RequestHandler requests;
	public DataOutputStream out;
	public DataInputStream in;
	private String name;
	private String ip;
	private int port;
	private Label chatLabel;
	private MainMenu menu;
	private GameHud hud;
	private Main main;
	//
	public ArrayList<RawChel> chels;
	Resender resend;

	public Client(String name, String ip, int port, Main main) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.chatLabel = main.hud.chatLabel;
		this.menu = main.menu;
		this.hud = main.hud;
		this.main = main;
		requests = new RequestHandler(this);
	}

	public void stop() {
		resend.setStop();
	}

	public void start() {
		try {
			socket = new Socket(ip, port);
			socket.setTcpNoDelay(true);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());

			resend = new Resender();
			resend.start();
			while (menu.connected) { // Output loop
				if (main.inputManager.send_message && menu.connected) {
					requests.sendRequest(Request.SERVER_RECEIVE_CHAT_MESSAGE);
					sendLine(hud.chatTextField.getText());
					main.inputManager.send_message = false;
					main.hud.chatTextField.setText("");
					main.hud.chatTextField.setVisible(false);
				}
				Thread.sleep(50);
			}
			hud.chatLabel.setText(chatLabel.getText() + "\nYou are now disconnected");
			requests.sendRequest(Request.SERVER_DISCONNECT_CLIENT);
			resend.setStop();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	private void close() {
		try {
			out.close();
			in.close();
			socket.close();
		} catch (Exception e) {
			System.err.println("Failed to close streams");
		}
	}

	public String getLine() {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		String line = "";
		try {
			line = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}

	public void sendLine(String text) {
		try {
			String line = text + '\n';
			out.write(line.getBytes(StandardCharsets.UTF_8));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendVector2(Vector2 coords) {
		try {
			out.writeFloat(coords.x);
			out.writeFloat(coords.y);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class Resender extends Thread {
		private boolean stopped;

		public void setStop() {
			stopped = true;
		}

		@Override
		public void run() {
			try {
				requests.sendRequest(Request.SERVER_HANDLE_NEW_CONNECTION);
				sendLine(name);
				while (!stopped && menu.connected) { // Input loop
					int requestId = in.readInt();
					System.out.println("incoming request: " + requestId);
					Request request = Request.fromInt(requestId);
					switch (request) {
						case CLIENT_RECEIVE_CHAT_MESSAGE:
							String str = getLine();
							chatLabel.setText(chatLabel.getText() + "\n" + str);
							System.exit(0);
							break;
						case CLIENT_RECEIVE_OBJECT:
							/*try {
								//main.rawchels = (ArrayList<RawChel>) in.readObject();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							break;*/
						case CLIENT_UPDATE:
//							for (RawChel chel : main.rawchels) {
//								chel.position.x = in.readFloat();
//								chel.position.y = in.readFloat();
//							}
//							break;
					}
				}
				requests.sendRequest(Request.SERVER_DISCONNECT_CLIENT);
			} catch (IOException e) {
				System.err.println("Failed to receive message.");
				e.printStackTrace();
			}
		}
	}
}
