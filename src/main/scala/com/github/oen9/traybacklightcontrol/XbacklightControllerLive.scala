package com.github.oen9.traybacklightcontrol

import zio.ZIO
import scala.sys.process._

trait XbacklightControllerLive extends XbacklightController {
  class XbacklightCommunicationError(msg: String) extends Exception(msg)

  val xbacklightController: XbacklightController.Service[Any] = new XbacklightController.Service[Any] {
    def set(value: Float): ZIO[Any, Throwable, Unit] = {
      val command = s"xbacklight -set $value"
      for {
        cmdResult <- ZIO.effect(command.!)
        _ <- cmdResult match {
                  case 0 => ZIO.unit
                  case _ => ZIO.fail(new XbacklightCommunicationError("can't set brightness"))
                }
      } yield ()
    }

    def get(): ZIO[Any, Throwable, Float] = ZIO.fromOption {
      "xbacklight -getf".lazyLines.headOption.map(_.toFloat)
    }.mapError(_ => new XbacklightCommunicationError("can't get brightness value"))
  }
}
