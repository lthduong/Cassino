package src.GUI

import scala.swing._
import scala.swing.event.ButtonClicked

object CassinoApp extends SimpleSwingApplication {

  val save = new MenuItem("Save")
  val load = new MenuItem("Load")
  val ins  = new MenuItem("Instruction")
  val game = new Menu("Game") { contents ++= Vector(save, load) }
  val help = new Menu("Help") { contents += ins }
  val menu = new MenuBar { contents ++= Vector(game, help) }

  def top = new MainFrame {
    contents = FirstScreen
    size = new Dimension(1200, 750)
    resizable = false
    menuBar = menu
  }

  this.listenTo(ins)
  this.reactions += {
    case b: ButtonClicked => {
      val source = b.source
      source match {
        case this.ins =>InstructionScreen.top.visible = true
      }
    }
  }
}
