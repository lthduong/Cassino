package src.Test

import src.Logic._
import src.GUI._
import scala.swing._
import scala.swing.SimpleSwingApplication

object GUITest extends SimpleSwingApplication {

  val game = new Game
  val player1 = new Player("Alex", game)
  val player2 = new Player("Barry", game)
  val player3 = new Player("Cathy", game)
  val player4 = new Player("Danny", game)
  val player5 = new Player("Edward", game)
  val player6 = new Player("Felix", game)
  val cmpPlayer1 = new ComputerPlayer("Cmp1", game)
  val cmpPlayer2 = new ComputerPlayer("Cmp2", game)
  val cmpPlayer3 = new ComputerPlayer("Cmp3", game)

  Vector(player1, player2, player3, player4, player5, player6, cmpPlayer1, cmpPlayer2, cmpPlayer3).foreach( game.addPlayer(_) )

  val screen = new GameScreen(game)

  def top = new MainFrame {
    contents = screen
    size = new Dimension(1200, 750)
    resizable = false
  }

}
