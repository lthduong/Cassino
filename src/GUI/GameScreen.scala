package src.GUI

import scala.swing._
import scala.swing.event._
import java.awt.{BasicStroke, Color, Font}
import src.Logic._
import javax.imageio.ImageIO
import java.io._


// This is the class acts as the game screen

object GameScreen extends Panel {

  var cardSelected = Vector[Card]()
  var cardUsed: Option[Card] = None
  var turnChange = false

  def endGame() = {
    if(Game.lastCapturer.isDefined && Game.table.allCard.nonEmpty) {
      Game.lastCapturer.get.addHandManually(Game.table.allCard)
      Game.table.allCard.foreach( Game.table.removeCard(_) )
    }
    Game.playersList.foreach( Game.calculateScore(_) )
    this.repaint()
    this.revalidate()
  }

  // Used to draw a selection highlight
  def drawSelection(g: Graphics2D, x: Int, y: Int, width: Int, length: Int) = {
    g.setStroke(new BasicStroke(3))
    g.drawLine(x        , y         , x + width, y         )
    g.drawLine(x        , y         , x        , y + length)
    g.drawLine(x + width, y + length, x + width, y         )
    g.drawLine(x + width, y + length, x        , y + length)
  }

  override def paintComponent(g: Graphics2D) = {
    // Draw background
    g.setColor(new Color(0, 153, 0))
    g.fillRect(0, 0, 1200, 750)

    if(!Game.isOver && !turnChange) {
      // Draw turn
      g.setColor(new Color(0, 0, 0))
      g.setFont(new Font("Arial", java.awt.Font.BOLD, 45))
      g.drawString(Game.playerTurn.name + "'s turn", 18, 55)
      // Draw table for the turn
      // If the number of cards on table <= 14, it will be displayed in image
      // If the number of cards on table > 14, it will be displayed by text
      // if(game.table.allCard.length <= 14) {
      for(i <- Game.table.allCard.indices) {
        val cardImage = Game.table.allCard(i).image.getScaledInstance(95, 145, 76)
        val yCoord = if(i < 7) 80 else 250
        g.drawImage(cardImage, 120 + 140 * (i % 7), yCoord, null)
        if(cardSelected.contains(Game.table.allCard(i))) {
           g.setColor(new Color(0, 213, 255))
          drawSelection(g, 120 + 140 * (i % 7), yCoord, 95, 145)
        }
      }
      // Draw player hand
      g.setColor(new Color(0, 0, 0))
      g.setFont(new Font("Arial", 0, 30))
      g.drawString("Hand:", 18, 500)
      for(i <- Game.playerTurn.hand.indices) {
        val cardImage = {
          if(Game.playerTurn.isInstanceOf[ComputerPlayer]) ImageIO.read(new File("./images/cards/back.png")).getScaledInstance(75, 115, 76)
          else Game.playerTurn.hand(i).image.getScaledInstance(75, 115, 76)
        }
        g.drawImage(cardImage, 150 + 140 * i, 430, null)
        if(cardUsed.contains(Game.playerTurn.hand(i))) {
          g.setColor(new Color(216, 8, 8))
          drawSelection(g, 150 + 140 * i, 430, 75, 115)
        }
      }
      // Draw scoreboard
      g.setColor(new Color(0,0,0))
      g.drawString("Score:", 18, 625)
      g.setColor(new Color(0, 12, 175))
      for(i <- Game.playersList.indices) {
        val yCoord = if(Game.playersList(i).isInstanceOf[ComputerPlayer]) 680 else 625
        g.drawString(Game.playersList(i).name + ": " + Game.playersList(i).getScore, 150 + 165 * (i % 6), yCoord)
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
    } else if(!Game.isOver && turnChange) {
      g.setColor(new Color(0, 0, 0))
      g.setFont(new Font("Arial", 0, 40))
      g.drawString("Move to " + Game.playerTurn.name + "'s turn", 150, 290)
      g.setFont(new Font("Arial", 0, 20))
      g.drawString("Press the screen to continue", 150, 340)
    } else {
      endGame()
      for(i <- Game.playersList.indices) {
        g.setFont(new Font("Arial", 0, 45))
        g.setColor(new Color(247, 255, 0))
        g.drawString("Game over! Here are the scores:", 250, 150)
        if(Game.winners.contains(Game.playersList(i))) g.setColor(new Color(216, 8, 8))
        else g.setColor(new Color(0, 0, 0))
        g.setFont(new Font("Arial", 0, 30))
        g.drawString(Game.playersList(i) + ": " + Game.playersList(i).getScore, 495, 200 + 50 * i)
      }
    }
  }


  this.revalidate()
  this.repaint()


  this.listenTo(this.mouse.clicks)
  this.reactions += {
    case e: MousePressed =>
      // If the screen is press when the turn is changing
      if(turnChange) {
        turnChange = false
        this.repaint()
        this.revalidate()
      }

      // Pile button is clicked
      if(e.point.x >= 700 && e.point.y >= 495 && e.point.x <= 750 && e.point.y <= 545) {
        Dialog.showMessage(this, "Cards in pile: " + Game.playerTurn.pile.map( _.toString ).mkString(", "), "Pile cards")
        this.repaint()
        this.revalidate()
      }

      // Drop button is clicked
      if(e.point.x >= 875 && e.point.y >= 495 && e.point.x <= 925 && e.point.y <= 545) {
        if(cardUsed.isDefined) {
          Game.playerTurn.drop(cardUsed.get)
          Game.advanceTurn()
          turnChange = true
        } else {
          Dialog.showMessage(this, "Choose a card to drop.", "Action failed.")
        }
        this.repaint()
        this.revalidate()
      }

      // Capture button is clicked
      if(e.point.x >= 875 && e.point.y >= 430 && e.point.x <= 925 && e.point.y <= 480) {
        if(cardUsed.isDefined && cardSelected.nonEmpty) {
          if(Game.validCapture(cardUsed.get, cardSelected)) {
            Game.playerTurn.capture(cardUsed.get, cardSelected)
            Game.advanceTurn()
            turnChange = true
          } else {
            cardSelected = Vector[Card]()
            cardUsed = None
            Dialog.showMessage(this, "The capture is invalid. Please try again.", "Action failed.")
          }
        } else {
          Dialog.showMessage(this, "Choose a card to perform capture", "Action failed")
        }
        this.repaint()
        this.revalidate()
      }

      // If a card on hand is select
      for(i <- 0 to 3) {
        val xCoord = 150 + 140 * i
        val yCoord = 430
        if(e.point.x >= xCoord && e.point.y >= yCoord && e.point.x <= xCoord + 75 && e.point.y <= yCoord + 115) {
          if(Game.playerTurn.hand.isDefinedAt(i)) {
            cardUsed = Some(Game.playerTurn.hand(i))
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
          if(Game.table.allCard.isDefinedAt(i)) {
            cardSelected = cardSelected :+ Game.table.allCard(i)
             this.repaint()
             this.revalidate()
          }
        }
      }

  }


}
