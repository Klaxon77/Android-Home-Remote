package com.klaxon.remote.common.message

/**
 * Represents point on client side
 */
sealed trait Point

case class RelativePoint(x: Double, y: Double) extends Point {
  require(x >= 0 && x <= 1, "x should be in range [0, 1]")
  require(y >= 0 && y <= 1, "y should be in range [0, 1]")
}

case class MovePoint(x: Double, y: Double) extends Point