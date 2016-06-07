package com.rhs.murphyTCG.network

import com.rhs.murphyTCG.PORT
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetAddress
import java.net.MulticastSocket
import java.net.ServerSocket

//To Future Me: Objects are created lazily, don't worry
object Server {
    val socket = ServerSocket(PORT).use { listener -> listener.accept() }
    val input = BufferedReader(InputStreamReader(socket.inputStream))
    val output = PrintWriter(socket.outputStream, true)
}