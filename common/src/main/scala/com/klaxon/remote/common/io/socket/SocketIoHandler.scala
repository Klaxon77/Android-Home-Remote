package com.klaxon.remote.common.io.socket

import org.apache.mina.core.service.IoHandlerAdapter
import com.klaxon.remote.common.io.{OutputChannel, Endpoint}
import org.apache.mina.core.session.IoSession

private[socket] class SocketIoHandler(endpoint: Endpoint) extends IoHandlerAdapter{
  override def messageReceived(session: IoSession, message: Any) = endpoint.receive(message, new OutputChannelAdapter(session))
  override def exceptionCaught(session: IoSession, cause: Throwable) = throw cause
}

private final class OutputChannelAdapter(ioSession: IoSession) extends OutputChannel {
  override def !(msg: Any): Unit = ioSession.write(msg)
}