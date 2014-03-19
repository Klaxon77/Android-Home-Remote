package com.klaxon.remote.common.io


/**
 * Channel by which you can send your messages
 */
trait OutputChannel {
  def !(msg: Any)
}

/**
 * Handles incoming messages in receive method.
 */
trait MessageHandler {
  def receive(message: Any, sender: OutputChannel)
}

trait MessageHandlerComponent {
  val messageHandler: MessageHandler
}

trait Closeable {
  def close(): Unit
}

trait Client extends MessageHandlerComponent with OutputChannel with Closeable

trait Server extends MessageHandlerComponent with Closeable