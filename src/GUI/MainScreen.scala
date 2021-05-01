package src.GUI

import scala.swing._
import scala.swing.event._
import java.awt.Color
import src.Logic.Game

import javax.swing.SwingConstants

object MainScreen extends SimpleSwingApplication {


  // Creating the menu bar
  val save = new Button("Save")
  val load = new Button("Load")
  val ins  = new Button("Instruction")
  val game = new Menu("Game") { contents ++= Vector(save, load) }
  val help = new Menu("Help") { contents += ins }
  val menu = new MenuBar { contents ++= Vector(game, help) }


  // Creating the computer player prompt and player selection
  val cmpPrompt      = new Label("How many computer players?")
  val cmpPlayers     = new ComboBox[Int](0 to 11)
  val playerPrompt   = new Label("How many human players?")
  val playerSelect   = new ComboBox[Int](1 to 12)
  val playerCf      = new Button("Ok")
  val cmpPanel = new FlowPanel {
    contents  += cmpPrompt
    contents  += cmpPlayers
    //background = Color.green
  }
  val playerPanel = new FlowPanel {
    contents  += playerPrompt
    contents  += playerSelect
    contents  += playerCf
    //background = Color.green
  }


  // Content of the screen
  val screenContent = new BoxPanel(Orientation.Vertical) {
    contents += new Label("Cassino") {
     preferredSize = new Dimension (150, 100)
     font = new Font("Arial", 30, 30)
     horizontalAlignment = Alignment.Center
     verticalAlignment = Alignment.Center
    }
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
  this.listenTo(save)
  this.listenTo(load)
  this.listenTo(playerCf)
  this.reactions += {
    case clickEvent: ButtonClicked =>
      val source = clickEvent.source
      source match {
        case this.ins => InstructionScreen.top.visible = true
      }
  }


}
