package src.Test

import src.Logic._
import src.GUI._
import scala.swing._
import scala.swing.SimpleSwingApplication

object GUITest extends SimpleSwingApplication {


  val player1 = new Player("Alex")
  val player2 = new Player("Barry")
  val player3 = new Player("Cathy")
  val player4 = new Player("Danny")
  val player5 = new Player("Edward")
  val player6 = new Player("Felix")

  val players = Vector(player1, player2, player3, player4, player5, player6)
  Game.newGame(3, players)
  GameScreen.visible = true
  def top = new MainFrame {
    contents = GameScreen
    size = new Dimension(1200, 750)
    resizable = false
  }

}
