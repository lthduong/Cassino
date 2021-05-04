package src.GUI


import scala.collection.mutable.Buffer
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
  val humPlayers     = new ComboBox[Int](1 to 12)
  val playerCf       = new Button("Ok")

  val plrPanel = new BoxPanel(Orientation.Vertical) {
    background = new Color(4, 154, 49)
    contents += new FlowPanel {
      contents ++= Vector(cmpPrompt, cmpPlayers)
      cmpPrompt.font = new Font("Arial", 0, 20)
      background = new Color(4, 154, 49)
      maximumSize = new Dimension(2000, 50)
    }
    contents += new FlowPanel {
      contents ++= Vector(playerPrompt, humPlayers)
      playerPrompt.font = new Font("Arial", 0, 20)
      maximumSize = new Dimension(2000, 50)
      background = new Color(4, 154, 49)
    }
    contents += playerCf
  }



  // Content of the screen
  val screenContent = new BoxPanel(Orientation.Vertical) {
    contents += new Label("Cassino") {
     horizontalTextPosition = Alignment.Center
     verticalTextPosition = Alignment.Center
     horizontalAlignment = Alignment.Center
     preferredSize = new Dimension (2000, 100)
     font = new Font("Arial", 0, 75)
    }
    contents += plrPanel
    background = new Color(4, 154, 49)
  }


  // Adding the components together
  def top = new MainFrame {
    title         = "Cassino"
    menuBar       = menu
    preferredSize = new Dimension(1500, 1000)
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
        case this.playerCf => {
          val nrPlayers = cmpPlayers.item + humPlayers.item
          if(nrPlayers > 12) Dialog.showMessage(plrPanel, "The maximum number of players allowed is 12.", "Notice!")
          else if(nrPlayers <= 1) Dialog.showMessage(plrPanel, "The game should have at least 2 players.", "Notice!")
        }
      }
  }


}
