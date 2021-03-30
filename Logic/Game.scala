package Logic
import scala.collection.mutable.Buffer
import scala.util.Random
import scala.math._

class Game {

  val table = new Table
  val deck: Buffer[Card] = {
    val suit = Vector("S", "D", "C", "H")
    val name = (1 to 10).map( _.toString ).toVector ++ Vector("J", "Q", "K")
    for(n <- 0 to 51) {
      deck :+ new Card(name(n % name.length), suit(n % suit.length), (n % 13) + 1)
    }
    deck
  }    // TODO: Test this
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
  def dealTable(): Unit = {
    table.addCard(deck(0))
    deck -= deck(0)
  }

  def calculateScore(player: Player): Unit = ???  //TODO: Implement this

}
