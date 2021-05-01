package src.GUI

import scala.swing._
import scala.swing.event._
import java.awt.Color
import src.Logic.Game

object MainScreen extends SimpleSwingApplication {


  // Creating the menu bar
  val save = new Button("Save")
  val load = new Button("Load")
  val ins = new Button("Instruction")
  val game = new Menu("Game") { contents ++= Vector(save, load) }
  val help = new Menu("Help") { contents += ins }
  val menu = new MenuBar { contents ++= Vector(game, help) }


  // Creating the computer player prompt and player selection
  val cmpPrompt      = new Label("Do you want computer players?")
  val computerPlayer = new Button("Yes")
  val playerPrompt   = new Label("How many players?")
  val playerSelect   = new ComboBox[Int](1 to 12)
  val confirm        = new Button("Ok")
  val cmpPanel = new FlowPanel {
    contents  += cmpPrompt
    contents  += computerPlayer
    //background = Color.green
  }


  // The panel contains the player prompt
  val playerPanel = new FlowPanel {
    contents  += playerPrompt
    contents  += playerSelect
    contents  += confirm
    //background = Color.green
  }


  // Content of the screen
  val screenContent = new BoxPanel(Orientation.Vertical) {
    contents += cmpPanel
    contents += playerPanel
  }


  // Adding the components together
  def top = new MainFrame {
    title         = "Cassino"
    menuBar       = menu
    preferredSize = new Dimension(750, 500)
    contents      = screenContent
  }

  // Event handling
  this.listenTo(ins)
  this.reactions += {
    case clickEvent: ButtonClicked =>
      val source = clickEvent.source
      source match {
        case this.ins => InstructionScreen.top.visible = true
      }
  }


}
