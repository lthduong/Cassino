package src.GUI

import scala.swing._
import scala.swing.event._
import java.awt.{BasicStroke, Color, Font}
import src.Logic._
import javax.imageio.ImageIO
import java.io._


// This is the class acts as the game screen

class GameScreen(game: Game) extends Panel {

  var winner: String = ""
  var cardSelected = Vector[Card]()
  var cardUsed: Option[Card] = None

  // Used to draw a selection highlight
  def drawSelection(g: Graphics2D, x: Int, y: Int, width: Int, length: Int) = {
    g.setStroke(new BasicStroke(3))
    g.drawLine(x        , y         , x + width, y         )
    g.drawLine(x        , y         , x        , y + length)
    g.drawLine(x + width, y + length, x + width, y         )
    g.drawLine(x + width, y + length, x        , y + length)
  }

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
      if(cardSelected.contains(game.table.allCard(i))) {
         g.setColor(new Color(0, 213, 255))
        drawSelection(g, 120 + 140 * (i % 7), yCoord, 95, 145)
      }
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
    g.setColor(new Color(0, 0, 0))
    g.setFont(new Font("Arial", 0, 30))
    g.drawString("Hand:", 18, 500)
    for(i <- game.playerTurn.hand.indices) {
      val cardImage = {
        if(game.playerTurn.isInstanceOf[ComputerPlayer]) ImageIO.read(new File("./images/cards/back.png")).getScaledInstance(75, 115, 76)
        else game.playerTurn.hand(i).image.getScaledInstance(75, 115, 76)
      }
      g.drawImage(cardImage, 150 + 140 * i, 430, null)
      if(cardUsed.contains(game.playerTurn.hand(i))) {
        g.setColor(new Color(216, 8, 8))
        drawSelection(g, 150 + 140 * i, 430, 75, 115)
      }
    }
    // Draw scoreboard
    g.setColor(new Color(0,0,0))
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

  this.listenTo(this.mouse.clicks)
  this.reactions += {
    case e: MousePressed =>
      // Pile button is clicked
      if(e.point.x >= 700 && e.point.y >= 495 && e.point.x <= 750 && e.point.y <= 545) {
        Dialog.showMessage(this, "Cards in pile: " + game.playerTurn.pile.map( _.toString ).mkString(", "), "Pile cards")
        this.repaint()
        this.revalidate()
      }

      // TODO: Change this to an appropriate reaction
      // Drop button is clicked
      if(e.point.x >= 875 && e.point.y >= 495 && e.point.x <= 925 && e.point.y <= 545) {
        Dialog.showMessage(this, "This button is reserved for drop method")
        this.repaint()
        this.revalidate()
      }

      // TODO: Change this to an appropriate reaction
      // Capture button is clicked
      if(e.point.x >= 875 && e.point.y >= 430 && e.point.x <= 925 && e.point.y <= 480) {
        Dialog.showMessage(this, "This button is reserved for capture method")
        this.repaint()
        this.revalidate()
      }

      // If a card on hand is select
      for(i <- 0 to 3) {
        val xCoord = 150 + 140 * i
        val yCoord = 430
        if(e.point.x >= xCoord && e.point.y >= yCoord && e.point.x <= xCoord + 75 && e.point.y <= yCoord + 115) {
          if(game.table.allCard.isDefinedAt(i)) {
            cardUsed = Some(game.playerTurn.hand(i))
             this.repaint()
             this.revalidate()
          }
        }
      }

      // If a card on the table is selected
      for(i <- 0 to 13) {
        val xCoord = 120 + 140 * (i % 7)
        val yCoord = if(i < 7) 80 else 250
        if(e.point.x >= xCoord && e.point.y >= yCoord && e.point.x <= xCoord + 95 && e.point.y <= yCoord + 145) {
          if(game.table.allCard.isDefinedAt(i)) {
            cardSelected = cardSelected :+ game.table.allCard(i)
             this.repaint()
             this.revalidate()
          }
        }
      }

  }

  this.revalidate()
  this.repaint()

}
