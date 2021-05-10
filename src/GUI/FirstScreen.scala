package src.GUI

import scala.swing._
import scala.swing.event._
import java.awt.Color
import javax.imageio.ImageIO
import java.io.File

object FirstScreen extends BoxPanel(Orientation.Vertical) {

  val title = new Label("Cassino")
  val plrCf = new Button("Ok")
  val plrPrompt = new Label("Number of human players")
  val humPlayers = new ComboBox(1 to 6)
  val cmpPrompt = new Label("Number of computer players")
  val cmpPlayers = new ComboBox(0 to 3)
  val nameCf = new Button("Confirm")
  val back = new Button("Back")
  var nrCmp: Int = 0
  var namePanels = Vector[TextField]()


  val titlePanel = new FlowPanel {
    title.font = new Font("Arial", 0, 60)
    maximumSize = new Dimension(1200, 200)
    contents += title
    background = new Color(0, 153, 0)
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


  val mainPanel = new FlowPanel {
    contents += plrPanel
    background = new Color(0, 153, 0)
  }


  // This method will create a panel to input names
  def getName(number: Int): BoxPanel = {
    val namePrompt = new BoxPanel(Orientation.Vertical) {
      for( i <- 1 to number ) {
        val namePanel = new FlowPanel {
          namePanels = namePanels :+ new TextField(10)
          contents += new Label("Input player's name:")
          contents += namePanels(i - 1)
          background = new Color(0, 153, 0)
        }
        contents += namePanel
      }
      maximumSize = new Dimension(1400, 900)
      background = new Color(0, 153, 0)
      contents += nameCf
      contents += back
    }
    namePrompt
  }


  contents += titlePanel
  contents += mainPanel


  this.listenTo(plrCf)
  this.listenTo(back)
  this.reactions += {
    case b: ButtonClicked =>
      val source = b.source
      source match {
        case this.plrCf => {
          val nameInput = getName(humPlayers.item)
          nrCmp = cmpPlayers.item
          mainPanel.contents.clear()
          mainPanel.contents += getName(humPlayers.item)
          this.repaint()
          this.revalidate()
        }
        case this.back => {
          mainPanel.contents.clear()
          mainPanel.contents += plrPanel
          this.repaint()
          this.revalidate()
        }
      }
  }

}
