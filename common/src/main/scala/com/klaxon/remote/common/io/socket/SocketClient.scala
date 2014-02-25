package com.klaxon.remote.common.io.socket

import com.klaxon.remote.common.io.Client
import java.net.SocketAddress
import org.apache.mina.transport.socket.nio.NioSocketConnector
import org.apache.mina.filter.codec.ProtocolCodecFilter
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory

/**
 * Socket client based on Apache Mina [[org.apache.mina.transport.socket.nio.NioSocketConnector NioSocketConnector]]
 * <p>date 2/19/14</p>
 * @author klaxon
 */
abstract class SocketClient(val socketAddress: SocketAddress) extends Client {
  private val client = new NioSocketConnector()

  client.getFilterChain.addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()))
  client.setHandler(new SocketIoHandler(this)) //TODO: this reference escapes during construction time. Fix this problem
  val connectFuture = client.connect(socketAddress)

  override def !(message: Any): Unit = {
    //Wait for connection to be established
    connectFuture.await()
    connectFuture.getSession.write(message)
  }

  override def close() = client.dispose()
}


