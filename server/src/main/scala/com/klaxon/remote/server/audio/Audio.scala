package com.klaxon.remote.server.audio

import javax.sound.sampled._
import scala.Some
import scala.collection.TraversableLike

/**
 * <p>date 3/10/14 </p>
 * @author klaxon
 */
object Audio {

  private val MasterLineName = "Master"

  def volume_=(volume: Float) = {
    require(volume >= 0 && volume <= 1)

    val line = getMasterOutputLine

    val opened = open(line)
    try {
      findVolumeControl(line).setValue(volume)
    } finally {
      if (opened) line.close()
    }
  }

  def volume: Float = {
    val line = getMasterOutputLine

    val opened = open(line)
    try {
      findVolumeControl(line).getValue
    } finally {
      if (opened) line.close()
    }
  }

  def mute(): Unit = setMute(true)

  def unMute() = setMute(false)

  def setMute(isMute: Boolean) = {
    val line = getMasterOutputLine

    val opened = open(line)
    try {
      findMuteControl(line).setValue(isMute)
    } finally {
      if (opened) line.close()
    }
  }

  def isMute: Boolean = {
    val line = getMasterOutputLine

    val isOpened = open(line)
    try {
      findMuteControl(line).getValue
    } finally {
      if (isOpened) line.close()
    }
  }

  private def getMasterOutputLine: Line = {
    for (mixer <- getMixers) {

      val flattenedAvailableLines = getAvailableLines(mixer, mixer.getTargetLineInfo).flatten
      val masterOutputLine = flattenedAvailableLines.find(_.getLineInfo.toString.contains(MasterLineName))

      if (masterOutputLine.isDefined) return masterOutputLine.get
    }

    throw new RuntimeException("Master Ouput line not found")
  }

  private def getMixers: Array[Mixer] = {
    val mixerInfoArray = AudioSystem.getMixerInfo
    mixerInfoArray.map(AudioSystem.getMixer)
  }

  private def getAvailableLines(mixer: Mixer, lineInfoArr: Array[Line.Info]) = lineInfoArr.map(info => getLineIfAvailable(mixer, info))

  private def getLineIfAvailable(mixer: Mixer, lineInfo: Line.Info): Option[Line] = {
    try {
      Some(mixer.getLine(lineInfo))
    } catch {
      case e: LineUnavailableException => None
    }
  }

  private def open(line: Line): Boolean = {
    if (line.isOpen) return false
    line.open()
    true
  }

  private def findVolumeControl(line: Line): FloatControl = {
    require(line.isOpen)
    findControl(FloatControl.Type.VOLUME, line.getControls).asInstanceOf[FloatControl]
  }

  private def findMuteControl(line: Line): BooleanControl = {
    require(line.isOpen)
    findControl(BooleanControl.Type.MUTE, line.getControls).asInstanceOf[BooleanControl]
  }

  private def findControl(typeOfControl: Control.Type, controls: Array[Control]): Control = {

    def findControlOption(typeOfControl: Control.Type, controls: Array[Control]): Option[Control] = {
      for (control <- controls) {
        if (control.getType == typeOfControl) return Some(control)

        control match {
          case compoundControl: CompoundControl =>
            val member = findControlOption(typeOfControl, compoundControl.getMemberControls)
            if (member.isDefined) return member
          case _ =>
        }
      }

      None
    }

    findControlOption(typeOfControl, controls) match {
      case Some(control) => control
      case None => throw new RuntimeException(f"Control $typeOfControl not found")
    }
  }


}
