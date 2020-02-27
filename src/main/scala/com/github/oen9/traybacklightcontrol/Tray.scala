package com.github.oen9.traybacklightcontrol

import zio.ZIO
import com.formdev.flatlaf.FlatDarculaLaf
import java.awt.SystemTray
import java.awt.Image
import java.awt.TrayIcon
import java.awt.Toolkit
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelListener
import java.awt.event.MouseWheelEvent
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import javax.swing.JPopupMenu
import javax.swing.JMenuItem
import javax.swing.JSlider
import javax.swing.SwingConstants
import javax.swing.event.ChangeListener
import javax.swing.event.ChangeEvent
import javax.swing.JMenu

object Tray {
  class SystemTrayNotSupported extends Exception("System tray not supported")

  def init(queue: zio.Queue[TrayMsg],
           sendToZio: zio.ZIO[zio.ZEnv, Throwable, Any] => Unit,
           sliderInitValue: Float): ZIO[Any, Throwable, Unit] = {
    if (SystemTray.isSupported()) {
      FlatDarculaLaf.install()

      val (popup, exitItem) = createPopup(queue, sendToZio, sliderInitValue)
      val trayIcon = createTrayIcon(popup)
      val sysTray = initSysTray(trayIcon)
      initExitItem(queue, sendToZio, exitItem, trayIcon, sysTray)
      ZIO.unit

    } else ZIO.fail(new SystemTrayNotSupported)
  }

  private def createPopup(queue: zio.Queue[TrayMsg],
                          sendToZio: zio.ZIO[zio.ZEnv, Throwable, Any] => Unit,
                          sliderInitValue: Float): (JPopupMenu, JMenuItem) = {

    val valueMultiplier = 10f
    val initValue = (sliderInitValue * valueMultiplier).toInt
    val slider: JSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 1000, initValue)
    slider.addChangeListener(new ChangeListener {
      override def stateChanged(e: ChangeEvent) = {
        val value = slider.getValue() / valueMultiplier
        sendToZio(queue.offer(TrayMsg.SetBacklight(value)))
      }
    })
    slider.addMouseWheelListener(new MouseWheelListener {
      override def mouseWheelMoved(e: MouseWheelEvent) = {
        val potentialNewValue = slider.getValue + (if (e.getWheelRotation() < 0) 1 else -1)
        val newValue =
          if (potentialNewValue < slider.getMinimum()) slider.getMinimum()
          else if (potentialNewValue > slider.getMaximum()) slider.getMaximum()
          else potentialNewValue
        slider.setValue(newValue)
      }
    })

    val closePopupItem = new JMenuItem("close popup")
    val optionsItem = new JMenu("options")
    val exitItem = new JMenuItem("exit")

    optionsItem.add(exitItem)

    val popup = new JPopupMenu()
    popup.add(closePopupItem)
    popup.addSeparator()
    popup.add(slider)
    popup.addSeparator()
    popup.add(optionsItem)

    (popup, exitItem)
  }

  private def createTrayIcon(popup: JPopupMenu) = {
    val image: Image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/bulb14.png"));
    val trayIcon: TrayIcon = new TrayIcon(image);
    //trayIcon.setImageAutoSize(true)

    trayIcon.addMouseListener(new MouseAdapter() {
        override def mouseReleased(e: MouseEvent) = maybeShowPopup(e)
        override def mousePressed(e: MouseEvent) = maybeShowPopup(e)
        def maybeShowPopup(e: MouseEvent) = {
          if (e.isPopupTrigger() || (e.getButton() == MouseEvent.BUTTON1 && !popup.isVisible())) {
            popup.setLocation(e.getX() + 1, e.getY() + 1);
            popup.setInvoker(popup);
            popup.setVisible(true);
          }
        }
    })

    trayIcon
  }

  private def initSysTray(trayIcon: TrayIcon): SystemTray = {
    val tray: SystemTray = SystemTray.getSystemTray();
    tray.add(trayIcon)
    tray
  }

  private def initExitItem(queue: zio.Queue[TrayMsg],
                           sendToZio: zio.ZIO[zio.ZEnv, Throwable, Any] => Unit,
                           exitItem: JMenuItem,
                           trayIcon: TrayIcon,
                           sysTray: SystemTray) = {
    exitItem.addActionListener(new ActionListener {
      def actionPerformed(ae: ActionEvent) = {
        sysTray.remove(trayIcon)
        sendToZio(queue.offer(TrayMsg.Exit))
      }
    })
  }
}
