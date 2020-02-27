package com.github.oen9.traybacklightcontrol

import zio.ZIO
import zio.macros.annotation.accessible
import zio.macros.annotation.mockable

@accessible(">")
@mockable
trait XbacklightController {
  val xbacklightController: XbacklightController.Service[Any]
}

object XbacklightController {

  trait Service[R] {
    def set(value: Float): ZIO[R, Throwable, Unit]
    def get(): ZIO[R, Throwable, Float]
  }
}
