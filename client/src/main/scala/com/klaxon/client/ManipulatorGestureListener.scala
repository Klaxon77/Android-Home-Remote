package com.klaxon.client

import com.klaxon.remote.common.io.Client
import android.view.{MotionEvent, GestureDetector}
import com.klaxon.remote.common.message._
import com.klaxon.remote.common.message.MovePoint
import com.klaxon.remote.common.message.Click
import com.klaxon.remote.common.message.RightClick
import com.klaxon.remote.common.message.Move

/**
 * <p>date 3/5/14 </p>
 * @author klaxon
 */
class ManipulatorGestureListener(client: Client, swipeThreshold: Int) extends GestureDetector.SimpleOnGestureListener {

  var xAccumulator = 0
  var yAccumulator = 0

  override def onSingleTapConfirmed(e: MotionEvent): Boolean = {
    client ! new Click()
    true
  }

  override def onLongPress(e: MotionEvent) = {
    client ! new RightClick()
  }

  override def onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean = {
    xAccumulator -= distanceX.toInt
    yAccumulator -= distanceY.toInt

    val moveDistance = math.sqrt(xAccumulator * xAccumulator + yAccumulator * yAccumulator).toInt

    if (moveDistance > swipeThreshold) {
      client.$bang(new Move(new MovePoint(xAccumulator, yAccumulator)))
      xAccumulator = 0
      yAccumulator = 0
    }

    true
  }

  override def onDown(e: MotionEvent) = {
    xAccumulator = 0
    yAccumulator = 0
    true
  }

  override def onDoubleTap(e: MotionEvent) = {
    client ! new DoubleClick()
    true
  }

}