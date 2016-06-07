package com.rhs.murphyTCG.network

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import com.rhs.murphyTCG.PORT
import com.rhs.murphyTCG.isServer

//To Future Me: Objects are created lazily, don't worry
object Server {
    val server = Server()

    init {
        isServer = true

        server.kryo.register(Packet::class.java)

        server.bind(PORT)
        server.start()

        server.addListener(object : Listener() {
            override fun received(connection: Connection?, received: Any?) {
                val packet = received as Packet

            }
        })
    }
}