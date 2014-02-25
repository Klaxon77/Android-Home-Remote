package com.klaxon.remote.common.io.socket

import org.specs2.mutable.{After, Specification}
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import java.net.{InetAddress, InetSocketAddress, SocketAddress}
import com.klaxon.remote.common.message._
import com.klaxon.remote.common.message.DoubleClick
import com.klaxon.remote.common.message.Click
import java.util.concurrent.{LinkedBlockingQueue, TimeUnit}
import com.klaxon.remote.common.io.OutputChannel

@RunWith(classOf[JUnitRunner])
class SocketClientServerTest extends Specification {

  "Client/Server" should {
    "send/receive correct messages" in new clientServer {
      println("start test")

      val messages = List(Click, DoubleClick, RightClick)
      messages.foreach(client ! _)

      val messagesOnServer = for (i <- 0 until messages.size) yield server.messages.poll(100, TimeUnit.MILLISECONDS)
      messagesOnServer.toList must be equalTo(messages)
    }
  }

}

trait clientServer extends After {
  val port = 6565

  val server = new ServerMock(new InetSocketAddress(InetAddress.getLocalHost, port))
  val client = new ClientMock(new InetSocketAddress(InetAddress.getLocalHost, port))

  override def after: Any = {
    client.close()
    server.close()
  }
}

class ClientMock(address: SocketAddress) extends SocketClient(address) {
  override def receive(message: Any, sender: OutputChannel): Unit = {}
}

class ServerMock(address: SocketAddress) extends SocketServer(address) {
  //Sending/receiving messages is asynchronous, so to wait for all messages to be received
  //we use BlockingQueue
  val messages = new LinkedBlockingQueue[Any]()

  override def receive(message: Any, sender: OutputChannel): Unit = messages.add(message)
}