package src.GUI

import scala.swing._
import java.awt.Color
import src.Logic.Game

object MainScreen extends SimpleSwingApplication {

  // Creating the menu bar
  val saveButton = new Button("Save")
  val loadButton = new Button("Load")
  val instructionButton = new Button("Instruction")

  val menu = new MenuBar
  menu.contents += saveButton
  menu.contents += loadButton
  menu.contents += instructionButton

  // Creating the computer player prompt and player selection
  val cmpPrompt = new Label("Do you want computer players?")
  val computerPlayer = new Button("Yes")
  val playerPrompt = new Label("How many players?")
  val playerSelect = new ComboBox[Int](1 to 12)
  val confirm = new Button("Ok")

  val cmpPanel = new FlowPanel
  cmpPanel.contents += cmpPrompt
  cmpPanel.contents += computerPlayer

  val playerPanel = new FlowPanel
  playerPanel.contents += playerPrompt
  playerPanel.contents += playerSelect
  playerPanel.contents += confirm

  val screenContent = new BoxPanel(Orientation.Vertical)
  screenContent.background = Color.green
  screenContent.contents += cmpPanel
  screenContent.contents += playerPanel

  // Adding the components together
  def top = new MainFrame {
    title = "Cassino"
    menuBar = menu
    preferredSize = new Dimension(750, 500)
    contents = screenContent
  }



}
