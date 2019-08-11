package com.netrew;

public enum Request {
	NOTHING(0),
	SERVER_RECEIVE_TARGET(1),
	SERVER_REPLACE_CHELS(2),
	SERVER_DISCONNECT_CLIENT(3),
	SERVER_RECEIVE_CHAT_MESSAGE(4),
	SERVER_HANDLE_NEW_CONNECTION(5),
	CLIENT_RECEIVE_OBJECT(6),
	CLIENT_UPDATE(7),
	CLIENT_RECEIVE_CHAT_MESSAGE(8);

	private int id;

	Request(int id) {
		this.id = id;
	}

	public static Request fromInt(int id) {
		for (Request a: Request.values()) {
			if (a.getValue() == id) {
				return a;
			}
		}
		return null;
	}

	public int getValue() {
		return id;
	}
}
