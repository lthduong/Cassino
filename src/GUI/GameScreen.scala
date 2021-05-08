package src.GUI

import scala.swing._
import java.awt.Color
import src.Logic.Game


// This is the class acts as the game screen

class GameScreen(game: Game) extends Panel {

  def paintCom(g: Graphics2D) = {
    g.setColor(new Color(0, 153, 0))
  }

  background = new Color(0, 153, 0)

}
