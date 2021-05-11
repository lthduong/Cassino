package src.GUI

import scala.swing._
import javax.imageio.ImageIO
import java.io.File
import java.awt.{Graphics2D, Color}

object InstructionScreen extends SimpleSwingApplication {

  val rule: String = {
    " 1. The deck is shuffled in the beginning of every round and each player has 4 cards, also there are 4 cards on the table (visible for everyone).\n\n" +
    " 2. Each round, a player can either use a card to take other cards from the table or to put it on the table. If the player cannot take anything from the\n" +
    " table, he/she must put one of his/her cards on the table. If the player takes cards from the table, he/she puts them in a separate pile of his/her own.\n" +
    " 3. The pile is used to count the points after the round has ended.\n\n" +
    " 4. The player can use a card to take one or more cards of the same value and cards with their summed value equal to i * used card for some integer i.\n\n" +
    " 5. If some player gets all the cards from the table at the same time, he/she gets a so- called sweep which is written down.\n\n" +
    " 6. There are a couple of cards that are more valuable in the hand than in the table,\n" +
    "       Aces: 14 in hand, 1 on table\n" +
    "       Diamonds-10: 16 in hand, 10 on table\n" +
    "       Spades-2: 15 in hand, 2 on table\n\n" +
    " 7. When every player runs out of cards, the last player to take cards gets the rest of the cards on the table, and the point are calculated using the rule:\n" +
    "       Each sweep grants 1 point.\n" +
    "       Each Ace grants 1 point.\n" +
    "       The player with most cards gets 1 point.\n" +
    "       The player with most spades gets 2 points.\n" +
    "       The player with Diamonds-10 gets 2 points.\n" +
    "       The player with Spades-2 gets 1 point.\n"
  }

  val instructionTitle = new Label("Instruction") {
    font = new Font("Arial", java.awt.Font.BOLD, 40)
    foreground = new Color(216, 8, 8)
  }

  val pane = new BoxPanel(Orientation.Vertical) {
    contents += new FlowPanel {
      contents += instructionTitle
      override def paintComponent(g: Graphics2D) = {
        val image = ImageIO.read(new File("./images/other/instruction.png")).getScaledInstance(52, 52, 55)
        g.drawImage(image, 490, 3, null)
      }
    }
    contents += new TextArea(rule) {
      editable = false
      font = new Font("Arial", 0, 20)
      background = new Color(0, 153, 0)
    }
    background = new Color(0, 153, 0)
  }

  val instruction = new ScrollPane {
    contents = pane
    background = new Color(0, 153, 0)
  }

  def top = new Frame {
    title = "Instruction"
    contents = instruction
    resizable = false
  }

}