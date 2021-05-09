package src.GUI

import scala.swing._
import java.awt.Color
import src.Logic._
import javax.imageio.ImageIO
import java.io._
import java.awt.Font


// This is the class acts as the game screen

class GameScreen(game: Game) extends Panel {

  var winner: String = ""
  var cardSelected = Vector[Card]()
  var cardUsed: Option[Card] = None

  def paintCom(g: Graphics2D) = {
    // Draw background
    g.setColor(new Color(0, 153, 0))
    g.fillRect(0, 0, 1200, 750)
    //Draw turn
    g.setColor(new Color(0, 0, 0))
    g.setFont(new Font("Arial", java.awt.Font.BOLD, 45))
    g.drawString(game.playerTurn.name + "'s turn", 18, 55)
    // Draw table for the turn
    // If the number of cards on table <= 14, it will be displayed in image
    // If the number of cards on table > 14, it will be displayed by text
    //if(game.table.allCard.length <= 14) {
    for(i <- game.table.allCard.indices) {
      val cardImage = game.table.allCard(i).image.getScaledInstance(95, 145, 76)
      val yCoord = if(i < 7) 80 else 250
      g.drawImage(cardImage, 120 + 140 * (i % 7), yCoord, null)
    }
    /* } else {
      g.setFont(new Font("Arial", 0, 30))
      g.drawString("Table:", 18, 100)
      g.setFont(new Font("Arial", 0, 40))
      for(i <- game.table.allCard.indices) {
        val yCoord = {
          if(i < 11) 110
          else if(i < 22) 190
          else if(i < 33) 270
          else 350
        }
        g.drawString(game.table.allCard(i).toString.toUpperCase, 130 + 95 * (i % 11), yCoord)
      }
    } */
    // Draw player hand
    g.setFont(new Font("Arial", 0, 30))
    g.drawString("Hand:", 18, 500)
    for(i <- game.playerTurn.hand.indices) {
      val cardImage = {
        if(game.playerTurn.isInstanceOf[ComputerPlayer]) ImageIO.read(new File("./images/cards/back.png")).getScaledInstance(75, 115, 76)
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
    // Draw capture and drop buttons
    g.setColor(new Color(247, 255, 0))
    val capture = ImageIO.read(new File("./images/other/capture.png")).getScaledInstance(50, 50, 52)
    val drop = ImageIO.read(new File("./images/other/drop.png")).getScaledInstance(50, 50, 52)
    val pile = ImageIO.read(new File("./images/other/pile.png")).getScaledInstance(50, 50, 52)
    g.drawImage(capture, 875, 430, null)
    g.drawImage(drop, 875, 495, null)
    g.drawImage(pile,700, 495, null )
    g.drawString("Capture", 935, 465)
    g.drawString("Drop", 935, 530)
    g.drawString("Pile", 760, 530)
  }

  override def paintComponent(g: Graphics2D) = {
    paintCom(g)
  }

  this.revalidate()
  this.repaint()

}
