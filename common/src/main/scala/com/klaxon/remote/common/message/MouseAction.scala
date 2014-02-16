package com.klaxon.remote.common.message

/**
 * Message class for communication from client to server.
 * Subclasses include such common UI messages as Move, Click, RightClick, etc.
 */
sealed trait MouseAction

case class Move(point: Point) extends MouseAction
case class Click() extends MouseAction
case class RightClick() extends MouseAction
case class DoubleClick() extends MouseAction