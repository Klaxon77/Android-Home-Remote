package com.klaxon.remote.common.message

sealed trait AudioAction

case class MuteToggle() extends AudioAction

/**
 * Exact volume to be set on server.
 * Takes values in range [0, 1]
 */
case class Volume(value: Float) extends AudioAction {
  require(value >= 0 && value <= 1)
}

/**
 * The value by which the volume should be changed.
 * Takes value in range [-1, 1]
 */
case class VolumeChange(value: Float) extends AudioAction {
  require(value >= -1 && value <= 1)
}