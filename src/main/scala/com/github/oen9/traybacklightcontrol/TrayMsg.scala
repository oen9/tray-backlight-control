package com.github.oen9.traybacklightcontrol

sealed trait TrayMsg

object TrayMsg {
  case class SetBacklight(value: Float)extends TrayMsg
  case object Exit extends TrayMsg
}
