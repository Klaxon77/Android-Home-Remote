package com.klaxon.remote.server

import com.klaxon.remote.common.io.socket.SocketServer
import java.net.{UnknownHostException, InetAddress, InetSocketAddress}
import com.klaxon.remote.common.config.Configuration


/**
 * <p>date 2/25/14 </p>
 * @author klaxon
 */
object Main {

  private val HORIZONTAL_LINE = "-" * 40

  def main(args: Array[String]): Unit = {
    if (args.isEmpty) {
      println("Please provide host name in command line arguments")
      return
    }

    val host = hostFrom(args(0))
    if (host == None) {
      println("Unknown host provided")
      return
    }

    printlnWithLine("Server start")
    startServer(host.get)
  }

  private def hostFrom(arg: String): Option[InetAddress] = {
    try {
      Some(InetAddress.getByName(arg))
    } catch {
      case e: UnknownHostException => None
    }
  }

  private def startServer(host: InetAddress): Unit = {
    val address = new InetSocketAddress(host, Configuration.PORT)
    val server = new SocketServer(address, serverMessageHandler)

    while (true) {
      println("Press [q] to stop server")
      val char = readChar()
      if (char.toLower == 'q') {
        server.close()
        printlnWithLine("Server closed")
        return
      }
    }
  }

  private def serverMessageHandler = CompositeMessageHandler(new MouseManipulator, new AudioManipulator)

  private def printlnWithLine(x: Any) = {
    println(HORIZONTAL_LINE)
    println(x)
    println(HORIZONTAL_LINE)
  }

}
