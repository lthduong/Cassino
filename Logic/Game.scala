package Logic
import scala.collection.mutable.Buffer
import scala.util.Random

class Game {

  val table = new Table
  val deck = Buffer[Card]()  //TODO: Implement the deck creating algorithm
  val players = Buffer[Player]()

  def shuffle = Random.shuffle(deck)


  //Deal cards to the target player until he has 4 cards
  def deal(player: Player): Unit = {
    if(players.contains(player)) {
      while(player.hand.size < 4 && deck.nonEmpty) {
        player.hand += deck(0)
        deck -= deck(0)
      }
    }
  }

  //Deal cards to the table
  def dealTable: Unit = {
    table.addCard(deck(0))
    deck -= deck(0)
  }

  def calculateScore(player: Player): Unit = ???  //TODO: Implement this

}
