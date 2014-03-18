package com.klaxon.remote.server

import com.klaxon.remote.common.io.{OutputChannel, MessageHandler}

/**
 * <p>date 3/11/14 </p>
 * @author klaxon
 */
case class CompositeMessageHandler(private val handlers: MessageHandler*) extends MessageHandler {

  override def receive(message: Any, sender: OutputChannel): Unit = handlers.foreach(_.receive(message, sender))

}
