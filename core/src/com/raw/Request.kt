package com.raw

enum class Request private constructor(val value: Int) {
    NOTHING(0),
    SERVER_RECEIVE_TARGET(1),
    SERVER_REPLACE_CHELS(2),
    SERVER_DISCONNECT_CLIENT(3),
    SERVER_RECEIVE_CHAT_MESSAGE(4),
    SERVER_HANDLE_NEW_CONNECTION(5),
    CLIENT_RECEIVE_OBJECT(6),
    CLIENT_UPDATE(7),
    CLIENT_RECEIVE_CHAT_MESSAGE(8);


    companion object {

        fun fromInt(id: Int): Request? {
            for (a in Request.values()) {
                if (a.value == id) {
                    return a
                }
            }
            return null
        }
    }
}
