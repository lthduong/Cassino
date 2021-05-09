package src.GUI

import scala.swing._
import java.awt.Color
import src.Logic._
import javax.imageio.ImageIO
import java.io._


// This is the class acts as the game screen

class GameScreen(game: Game) extends Panel {

  var winner: String = ""

  def paintCom(g: Graphics2D) = {
    // Draw background
    g.setColor(new Color(0, 153, 0))
    g.fillRect(0, 0, 1200, 750)
    //Draw turn
    g.setColor(new Color(0, 0, 0))
    g.setFont(new Font("Arial", java.awt.Font.BOLD, 45))
    g.drawString(game.playerTurn.name + "'s turn", 18, 55)
    g.setFont(new Font("Arial", 0, 30))
    // Draw table for the turn
    for(i <- game.table.allCard.indices) {
      val cardImage = game.table.allCard(i).image.getScaledInstance(95, 145, 76)
      val yCoord = if(i < 7) 80 else 250
      g.drawImage(cardImage, 120 + 140 * (i % 7), yCoord, null)
    }
    // Draw player hand
    g.drawString("Hand", 18, 500)
    for(i <- game.playerTurn.hand.indices) {
      val cardImage = {
        if(game.playerTurn.isInstanceOf[ComputerPlayer]) ImageIO.read(new File("./Image/Cards/back.png")).getScaledInstance(75, 115, 76)
        else game.playerTurn.hand(i).image.getScaledInstance(75, 115, 76)
      }
      g.drawImage(cardImage, 150 + 140 * i, 430, null)
    }
    // Draw scoreboard
    g.drawString("Score:", 18, 625)
    g.setColor(new Color(0, 12, 175))
    for(i <- game.playersList.indices) {
      val yCoord = if(game.playersList(i).isInstanceOf[ComputerPlayer]) 680 else 625
      g.drawString(game.playersList(i).name + ": " + game.playersList(i).getScore, 150 + 165 * (i % 6), yCoord)
    }

  }

  override def paintComponent(g: Graphics2D) = {
    paintCom(g)
  }

  this.revalidate()
  this.repaint()

}
