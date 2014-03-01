package com.klaxon.remote.server

import com.klaxon.remote.common.io.{MessageHandler, OutputChannel}
import com.klaxon.remote.common.message._
import java.awt.{GraphicsEnvironment, MouseInfo, Robot}
import java.awt.event.InputEvent
import com.klaxon.remote.common.message.Click
import com.klaxon.remote.common.message.RightClick
import com.klaxon.remote.common.message.DoubleClick

/**
 * Manipulator of device which controls the mouse.
 * <p>date 2/25/14 </p>
 * @author klaxon
 */
class DeviceManipulator(robot: Robot) extends MessageHandler {

  def this() = this(new Robot())

  def receive(message: Any, sender: OutputChannel): Unit = {
    println("received " + message)

    message match {
      case Click() => leftClick()
      case DoubleClick() => doubleClick()
      case RightClick() => rightClick()
      case Move (coordinate) => move (mouseLocationFrom (coordinate))
      case _ => println(message)
    }
  }

  private def leftClick() = {
    robot.mousePress(InputEvent.BUTTON1_MASK)
    robot.mouseRelease(InputEvent.BUTTON1_MASK)
  }

  private def rightClick() = {
    robot.mousePress(InputEvent.BUTTON3_MASK)
    robot.mouseRelease(InputEvent.BUTTON3_MASK)
  }

  private def doubleClick() = {
    leftClick()
    leftClick()
  }

  private def mouseLocationFrom(p: Point): (Int, Int) = p match {
    case MovePoint(x, y) =>
      val mouseLocation = MouseInfo.getPointerInfo.getLocation
      (mouseLocation.x + x, mouseLocation.y + y)

    case ExactPoint(x, y) =>
      val displayMode = GraphicsEnvironment.getLocalGraphicsEnvironment.getDefaultScreenDevice.getDisplayMode
      val xPos = displayMode.getWidth * x
      val yPos = displayMode.getHeight * y
      (xPos.toInt, yPos.toInt)
  }

  private def move(point: (Int, Int)) = {
    println("moving to " + point)
    robot.mouseMove(point._1, point._2)
  }

}
