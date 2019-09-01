package com.netrew.requests

import com.raw.Request
import com.netrew.Client

import java.io.IOException

class RequestHandler(private val client: Client) {

    fun sendRequest(request: Request) {
        try {
            client.out.writeInt(request.value)
            client.out.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
