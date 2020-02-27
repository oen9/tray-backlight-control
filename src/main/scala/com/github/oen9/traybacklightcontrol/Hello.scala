package com.github.oen9.traybacklightcontrol

import zio.ZIO
import zio.console._
import java.io.StringWriter
import java.io.PrintWriter

object Hello extends zio.App {
  override def run(args: List[String]): ZIO[zio.ZEnv,Nothing,Int] = {
    app
    .flatMapError {
      case e: Throwable =>
        val sw = new StringWriter
        e.printStackTrace(new PrintWriter(sw))
        zio.console.putStrLn(sw.toString())
    }
    .provideSome[Console](env => new XbacklightControllerLive with Console {
      val console = env.console
    })
    .fold(_ => 1, _ => 0)
  }

  def app(): ZIO[XbacklightController, Throwable, Unit] = for {
    initValue <- XbacklightController.>.get()
    queue <- zio.Queue.unbounded[TrayMsg]
    msgHandler <- handleTrayMsg(queue).repeat(zio.Schedule.doUntilEquals(TrayMsg.Exit)).fork
    _ <- ZIO.effect(Tray.init(queue, unsafeRunSync[Throwable, Any](_), initValue))
    _ <- msgHandler.await
  } yield ()

  def handleTrayMsg(q: zio.Queue[TrayMsg]) = for {
    newMsg <- q.take
    remainingMsg <- q.takeAll
    newest = remainingMsg.lastOption.getOrElse(newMsg)
    _ <- dispatchTrayMsg(newest)
  } yield (newMsg)

  def dispatchTrayMsg(msg: TrayMsg) = msg match {
    case TrayMsg.SetBacklight(value) => XbacklightController.>.set(value)
    case _ => ZIO.unit
  }
}

