package com.klaxon.remote.common.io

import java.io.Closeable

/**
 * Channel by which you can send your messages
 */
trait OutputChannel {
  def !(msg: Any)
}

/**
 * Endpoint should handle incoming messages in receive method.
 */
trait Endpoint extends Closeable {
  def receive(message: Any, sender: OutputChannel)
}

trait Client extends Endpoint with OutputChannel
trait Server extends Endpoint