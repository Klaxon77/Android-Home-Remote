package com.klaxon.remote.server

import com.klaxon.remote.common.io.{OutputChannel, MessageHandler}
import com.klaxon.remote.common.message.{VolumeChange, Volume, MuteToggle}
import com.klaxon.remote.server.audio.Audio

/**
 * Manipulates master audio of device, like volume and mute
 * <p>date 3/11/14 </p>
 * @author klaxon
 */
class AudioManipulator extends MessageHandler {

  override def receive(message: Any, sender: OutputChannel): Unit = message match {
    case MuteToggle() => Audio.setMute(!Audio.isMute)
    case Volume(value) => Audio.volume = value
    case VolumeChange(volumeChange) =>
      var newVolume = Audio.volume + volumeChange
      newVolume = if (newVolume > 1) 1 else if (newVolume < 0) 0 else newVolume
      Audio.volume = newVolume
    case _ =>
  }

}
