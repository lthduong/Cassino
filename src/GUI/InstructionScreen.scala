package src.GUI

import scala.swing._

object InstructionScreen extends SimpleSwingApplication {

  val rule: String = "The deck is shuffled in the beginning of every round and the dealer deals 4 cards to every player (they are not visible to other players) and 4 cards on the table (visible for everyone). The rest of the cards are left on the table upside down. The player next to the dealer starts the game. On the next round he/she is the dealer.\nActions in each round:\nA player can play out one of his/her cards: it can be used either for taking cards from the table or to just putting it on the table. If the player cannot take anything from the table, he/she must put one of his/her cards on the table.\nIf the player takes cards from the table, he/she puts them in a separate pile of his/her own. The pile is used to count the points after the round has ended.\nThe number of cards on the table can vary. For example, if someone takes all the cards from the table, the next player must put a card on the empty table.\nPlayer must draw a new card from the deck after using a card so that he/she has always 4 cards in his/her hand. (When the deck runs out, everyone plays until there are no cards left in any player’s hand).\nPlayer can use a card to take one or more cards of the same value and cards such that their summed value is equal to the used card.\nSweep:\nIf some player gets all the cards from the table at the same time, he/she gets a so- called sweep which is written down.\nSpecial cards:\nThere are a couple of cards that are more valuable in the hand than in the table,\nAces: 14 in hand, 1 on table\nDiamonds-10: 16 in hand, 10 on table\nSpades-2: 15 in hand, 2 on table\nScoring: \nWhen every player runs out of cards, the last player to take cards from the table gets the rest of the cards from the table. After this the points are calculated and added to the existing scores.\nThe following things grant points:\nEach sweep grants 1 point.\nEach Ace grants 1 point.\nThe player with most cards gets 1 point.\nThe player with most spades gets 2 points.\nhe player with Diamonds-10 gets 2 points.\nThe player with Spades-2 gets 1 point."

  val instruction = new ScrollPane {
    contents = new Label(rule)
  }

  def top = new Frame {
    title = "Instruction"
    contents = instruction
  }

}