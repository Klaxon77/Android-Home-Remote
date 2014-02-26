package com.klaxon.remote.common.message

/**
 * Represents point on client side
 */
sealed trait Point

/**
 * One shouldn't know about used screen resolution on Server. Therefore you should
 * provide coordinates that are in range [0, 1].
 *
 * ===Example:===
 * {{{(0, 0) is top-left point of screen
 * (0.5, 0.5) is center
 * (1, 1) is bottom-right point of screen}}}
 */
case class MovePoint(x: Int, y: Int) extends Point {
  require(x >= 0 && x <= 1, "x should be in range [0, 1]")
  require(y >= 0 && y <= 1, "y should be in range [0, 1]")
}


/**
 * Coordinate that is relative to current cursor position
 *
 * ===Example:===
 * {{{Current mouse position is (100, 2)
 * RelativeMove is (17, 3)
 * Then current cursor position should be moved to (117, 5)}}}
 */
case class RelativePoint(x: Double, y: Double) extends Point