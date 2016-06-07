package com.rhs.murphyTCG.network

import com.rhs.murphyTCG.LOCAL_HOST
import com.rhs.murphyTCG.PORT
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

//To Future Me: Objects are created lazily, don't worry
object Client {
    val socket = Socket(LOCAL_HOST, PORT)
    val input = BufferedReader(InputStreamReader(socket.inputStream))
    val output = PrintWriter(socket.outputStream, true)
}