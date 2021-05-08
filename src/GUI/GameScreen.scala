package src.GUI

import scala.swing._
import java.awt.Color
import src.Logic.Game


// This is the class acts as the game screen

class GameScreen(game: Game) extends Panel {

  def paintCom(g: Graphics2D) = {
    // Draw background
    g.setColor(new Color(0, 153, 0))
    g.fillRect(0, 0, 1200, 750)
    //Draw turn
    g.setColor(new Color(0, 0, 0))
    g.setFont(new Font("Arial", java.awt.Font.BOLD, 45))
    g.drawString(game.playerTurn.name + "'s turn", 18, 55)
    g.setFont(new Font("Arial", 0, 30))
    //Draw player hand
    //TODO: Adding the card drawing
    g.drawString("Hand", 18, 500)
    // Draw scoreboard
    g.drawString("Score:", 18, 625)
    g.setColor(new Color(0, 12, 175))
    for(i <- game.playersList.indices) {
      val yCoord = if(i <= 5) 625 else 680
      g.drawString(game.playersList(i).name + ": " + game.playersList(i).getScore, 150 + 165 * (i % 6), yCoord)
    }

  }

  override def paintComponent(g: Graphics2D) = {
    paintCom(g)
  }

  this.revalidate()
  this.repaint()

}
