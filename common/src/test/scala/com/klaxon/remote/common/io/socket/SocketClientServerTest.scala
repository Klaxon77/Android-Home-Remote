package com.klaxon.remote.common.io.socket

import org.specs2.mutable.{After, Specification}
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import java.net.{InetAddress, InetSocketAddress}
import com.klaxon.remote.common.message._
import com.klaxon.remote.common.message.DoubleClick
import com.klaxon.remote.common.message.Click
import java.util.concurrent.{LinkedBlockingQueue, TimeUnit}
import com.klaxon.remote.common.io.{MessageHandler, OutputChannel}

@RunWith(classOf[JUnitRunner])
class SocketClientServerTest extends Specification {

  "Client/Server" should {
    "send/receive correct messages" in new clientServer {
      val messages = List(Click, DoubleClick, RightClick)
      messages.foreach(client ! _)

      val serverMessages = for (i <- 0 until messages.size) yield serverMessagesBlockingQueue.poll(100, TimeUnit.MILLISECONDS)
      serverMessages.toList must be equalTo (messages)
    }
  }

}

trait clientServer extends After {
  private val socketAddress = new InetSocketAddress(6565)

  val serverMessagesBlockingQueue = new LinkedBlockingQueue[Any]()
  val server = new SocketServer(socketAddress, new ServerHandlerMock(serverMessagesBlockingQueue))
  val client = new SocketClient(socketAddress, new ClientHandlerMock())

  override def after: Any = {
    client.close()
    server.close()
  }
}

class ClientHandlerMock extends MessageHandler {
  override def receive(message: Any, sender: OutputChannel) = {}
}

class ServerHandlerMock(serverMessages: LinkedBlockingQueue[Any]) extends MessageHandler {
  //Sending/receiving messages is asynchronous, so to wait for all messages to be received
  //we use BlockingQueue
  override def receive(message: Any, sender: OutputChannel) = serverMessages.add(message)
}