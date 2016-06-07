package com.rhs.murphyTCG.network

import com.rhs.murphyTCG.PORT
import com.rhs.murphyTCG.isServer
import com.esotericsoftware.kryonet.Client

//To Future Me: Objects are created lazily, don't worry
object Client {
    val client = Client()

    init {
        isServer = false
    }
}