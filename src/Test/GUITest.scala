package src.Test

import src.Logic._
import src.GUI._
import scala.swing._
import scala.swing.SimpleSwingApplication

object GUITest extends SimpleSwingApplication {

  val game = new Game
  val player1 = new Player("plr1", game)
  val player2 = new Player("plr2", game)
  val player3 = new Player("plr3", game)
  val player4 = new Player("plr4", game)
  val cmpPlayer = new ComputerPlayer("cmp1", game)

  Vector(player1, player2, player3, player4, cmpPlayer).foreach( game.addPlayer(_) )

  val screen = new GameScreen(game)

  def top = new MainFrame {
    contents = screen
  }

}
