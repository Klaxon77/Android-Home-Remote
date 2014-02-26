package com.klaxon.remote.common.io.socket

import org.apache.mina.core.service.IoHandlerAdapter
import com.klaxon.remote.common.io.{MessageHandler, OutputChannel}
import org.apache.mina.core.session.IoSession

private[socket] class SocketIoHandler(handler: MessageHandler) extends IoHandlerAdapter {
  override def messageReceived(session: IoSession, message: Any) = {
    println("Received " + handler)
    handler.receive(message, new OutputChannelAdapter(session))
  }
  override def exceptionCaught(session: IoSession, cause: Throwable) = throw cause
}

private final class OutputChannelAdapter(ioSession: IoSession) extends OutputChannel {
  override def !(msg: Any): Unit = ioSession.write(msg)
}