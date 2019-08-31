package com.netrew.requests;

import com.raw.Request;
import com.netrew.Client;

import java.io.IOException;

public class RequestHandler {
	private Client client;

	public RequestHandler(Client client) {
		this.client = client;
	}

	public void sendRequest(Request request) {
		try {
			client.out.writeInt(request.getValue());
			client.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
