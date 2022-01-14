package com.github.art241111.tcpClient.writer

interface Sender {
    fun send(text: String)
    fun send(bytes: ByteArray)
    fun send(chars: CharArray)
}
