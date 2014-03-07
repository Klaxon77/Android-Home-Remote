package com.klaxon.remote.common.io.socket

import com.klaxon.remote.common.io.{OutputChannel, MessageHandler, Client}
import java.net.SocketAddress
import org.apache.mina.transport.socket.nio.NioSocketConnector
import org.apache.mina.filter.codec.ProtocolCodecFilter
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory
import java.io.IOException
import scala.util.Try

/**
 * Socket client based on Apache Mina [[org.apache.mina.transport.socket.nio.NioSocketConnector NioSocketConnector]]
 *
 * Opens connection to remote server. If connection can not be established throws RuntimeIoException
 * <p>date 2/19/14</p>
 * @author klaxon
 */
class SocketClient(socketAddress: SocketAddress, override val messageHandler: MessageHandler) extends Client {
  private val client = new NioSocketConnector()

  client.getFilterChain.addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()))
  client.setHandler(new SocketIoHandler(messageHandler))

  val connectFuture = client.connect(socketAddress)
  connectFuture.awaitUninterruptibly()
  if (!connectFuture.isConnected) throw new IOException("Could not connect to server host " + socketAddress)

  def this(socketAddress: SocketAddress) = this(socketAddress, new NullMessageHandler)

  override def !(message: Any): Unit = {
    connectFuture.getSession.write(message)
  }

  override def close() = client.dispose()
}

object SocketClient {

  def apply(): Try[SocketClient] = Try(new SocketClient())

}

private class NullMessageHandler extends MessageHandler {
  override def receive(message: Any, sender: OutputChannel) = {}
}


