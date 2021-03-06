package com.klaxon.remote.server

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import java.awt.Robot
import com.klaxon.remote.common.message.Click

/**
 * <p>date 2/25/14 </p>
 * @author klaxon
 */
@RunWith(classOf[JUnitRunner])
class MouseManipulatorTest extends Specification with Mockito{

  "Pc operator" should {
    "perform click" in {
      val robotMock = mock[Robot]
      val pcManipulator = new MouseManipulator(robotMock)
      pcManipulator.receive(Click(), null)

      there was one (robotMock).mousePress(any[Int])
      there was one (robotMock).mouseRelease(any[Int])
    }
  }

}




