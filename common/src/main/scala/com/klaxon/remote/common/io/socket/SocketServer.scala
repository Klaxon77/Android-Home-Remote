package com.klaxon.remote.common.io.socket

import com.klaxon.remote.common.io.{MessageHandler, Server}
import org.apache.mina.transport.socket.nio.NioSocketAcceptor
import org.apache.mina.filter.codec.ProtocolCodecFilter
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory
import java.net.SocketAddress

/**
 * Socket server based on Apache Mina [[org.apache.mina.transport.socket.nio.NioSocketAcceptor NioSocketAcceptor]]
 * <p>date 2/19/14 </p>
 * @author klaxon
 */
class SocketServer(socketAddress: SocketAddress, override val messageHandler: MessageHandler) extends Server {
  private val server = new NioSocketAcceptor()

  server.getFilterChain.addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()))
  server.setHandler(new SocketIoHandler(messageHandler))
  server.bind(socketAddress)

  override def close(): Unit = {
    server.unbind()
    server.dispose()
  }
}