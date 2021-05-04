package src.GUI


import scala.collection.mutable.Buffer
import scala.swing._
import scala.swing.event._
import java.awt.Color
import src.Logic.Game
import javax.swing.SwingConstants


object FirstScreen extends BoxPanel(Orientation.Vertical) {

  val title = new Label("Cassino")
  val plrCf = new Button("Ok")
  val plrPrompt = new Label("Number of human players")
  val humPlayers = new ComboBox(1 to 6)
  val cmpPrompt = new Label("Number of computer players")
  val cmpPlayers = new ComboBox(0 to 3)
  val nameCf = new Button("Confirm")


  val titlePanel = new FlowPanel {
    title.font = new Font("Arial", 0, 60)
    maximumSize = new Dimension(1200, 100)
    contents += title
  }


  // The place where the user can type the number of player they want
  val plrPanel = new BoxPanel(Orientation.Vertical) {
    contents += new FlowPanel {
      contents ++= Vector(cmpPrompt, cmpPlayers)
      maximumSize = new Dimension(1400, 100)
      background = new Color(0, 153, 0)
    }
    contents += new FlowPanel {
      contents ++= Vector(plrPrompt, humPlayers)
      maximumSize = new Dimension(1400, 100)
      background = new Color(0, 153, 0)
    }
    contents += plrCf
    background = new Color(0, 153, 0)
  }


  // This method will create a panel to input names
  def getName(number: Int): BoxPanel = {
    val namePrompt = new BoxPanel(Orientation.Vertical) {
      for( i <- 1 to number ) {
        val namePanel = new FlowPanel {
          contents += new Label("Input player's name:")
          contents += new TextField(10)
          background = new Color(0, 153, 0)
        }
        contents += namePanel
      }
      background = new Color(0, 153, 0)
      contents += nameCf
    }
    namePrompt
  }

  contents += title
  contents += plrPanel
  background = new Color(0, 153, 0)

  this.listenTo(plrCf)
  this.listenTo(nameCf)
  this.reactions += {
    case b: ButtonClicked =>
      val button = b.source
      button match {
        case this.plrCf => {
          val nrPlayers = cmpPlayers.item + humPlayers.item
          val nameInput = getName(humPlayers.item)
          this.plrPanel.contents.clear()
          contents += getName(humPlayers.item)
          this.revalidate()
        }
      }
  }



}
