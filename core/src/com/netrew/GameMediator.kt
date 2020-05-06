package com.netrew

import com.netrew.net.GameClient

class GameMediator {
    fun connect(ip: String, tcpPort: Int, udpPort: Int) {
         Globals.client = GameClient(ip, tcpPort, udpPort)
    }

    fun disconnect() {
        Globals.client.disconnect()
    }

    fun client() : GameClient {
        return Globals.client
    }
}