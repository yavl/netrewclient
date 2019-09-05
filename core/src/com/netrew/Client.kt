package com.netrew

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.raw.RawChel
import com.raw.Request
import com.netrew.requests.RequestHandler
import com.netrew.ui.GameHud
import com.netrew.ui.MainMenu

import java.io.*
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.ArrayList

class Client(private val name: String, private val ip: String, private val port: Int, private val main: Main) {
    private var socket: Socket? = null
    var requests: RequestHandler
    lateinit var outputStream: DataOutputStream
    lateinit var inputStream: DataInputStream
    private val chatLabel: Label
    private val menu: MainMenu
    private val hud: GameHud
    //
    lateinit private var resend: Resender
    var clientName = name

    val line: String
        get() {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var line = ""
            try {
                line = bufferedReader.readLine()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return line
        }

    init {
        this.chatLabel = main.hud.chatLabel
        this.menu = main.menu
        this.hud = main.hud
        requests = RequestHandler(this)
    }

    fun stop() {
        resend.setStop()
    }

    fun start() {
        try {
            socket = Socket(ip, port)
            socket!!.tcpNoDelay = true
            outputStream = DataOutputStream(socket!!.getOutputStream())
            inputStream = DataInputStream(socket!!.getInputStream())

            resend = Resender()
            resend.start()
            while (menu.connected) { // Output loop
                if (main.inputManager.sendMessage && menu.connected) {
                    requests.sendRequest(Request.SERVER_RECEIVE_CHAT_MESSAGE)
                    sendLine(hud.chatTextField.text)
                    main.inputManager.sendMessage = false
                    main.hud.chatTextField.text = ""
                    main.hud.chatTextField.isVisible = false
                }
                Thread.sleep(50)
            }
            hud.chatLabel.setText(chatLabel.text.toString() + "\nYou are now disconnected")
            requests.sendRequest(Request.SERVER_DISCONNECT_CLIENT)
            resend.setStop()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    private fun close() {
        try {
            outputStream.close()
            inputStream.close()
            socket!!.close()
        } catch (e: Exception) {
            System.err.println("Failed to close streams")
        }

    }

    fun sendLine(text: String) {
        try {
            val line = text + '\n'
            outputStream.write(line.toByteArray(StandardCharsets.UTF_8))
            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun sendVector2(coords: Vector2) {
        try {
            outputStream.writeFloat(coords.x)
            outputStream.writeFloat(coords.y)
            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private inner class Resender : Thread() {
        private var stopped: Boolean = false

        fun setStop() {
            stopped = true
        }

        override fun run() {
            try {
                requests.sendRequest(Request.SERVER_HANDLE_NEW_CONNECTION)
                sendLine(clientName)
                while (!stopped && menu.connected) { // Input loop
                    val requestId = inputStream.readInt()
                    println("incoming request: $requestId")
                    val request = Request.fromInt(requestId)
                    when (request) {
                        Request.CLIENT_RECEIVE_CHAT_MESSAGE -> {
                            val str = line
                            chatLabel.setText(chatLabel.text.toString() + "\n" + str)
                            System.exit(0)
                        }
                    }/*try {
								//main.rawchels = (ArrayList<RawChel>) in.readObject();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							break;*///							for (RawChel chel : main.rawchels) {
                    //								chel.position.x = in.readFloat();
                    //								chel.position.y = in.readFloat();
                    //							}
                    //							break;
                }
                requests.sendRequest(Request.SERVER_DISCONNECT_CLIENT)
            } catch (e: IOException) {
                System.err.println("Failed to receive message.")
                e.printStackTrace()
            }

        }
    }
}
