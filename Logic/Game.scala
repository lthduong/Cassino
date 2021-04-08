package Logic
import scala.collection.mutable.Buffer
import scala.util.Random
import scala.math._

class Game {

  val table = new Table
  private var players = Buffer[Player]()
  private var turn = players.head

  private val deck: Buffer[Card] = {
    var cardList = Buffer[Card]()
    val suit = Vector("S", "D", "C", "H")
    val name = (1 to 10).map( _.toString ).toVector ++ Vector("J", "Q", "K")
    for(n <- 0 to 51) {
      cardList = cardList :+ new Card(name(n % name.length), suit(n % suit.length), (n % 13) + 1)
    }
    cardList
  }    // TODO: Test this

  def playerTurn = this.turn

  def advanceTurn() = {
    turn = players((players.indexOf(turn) + 1) % players.length)
  }

  def addPlayers(playersLít: Buffer[Player]): Unit = { players = players ++ playersLít }

  def shuffle = Random.shuffle(deck)

  def validCapture(cardUse: Card, cardTake: Vector[Vector[Card]]): Boolean = {
    cardTake.forall( cardVector => cardVector.map( _.value ).sum == cardUse.handValue )
  }

  //Deal cards to the target player until he has 4 cards
  def deal(player: Player): Unit = {
    if(players.contains(player)) {
      this.shuffle
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

  def playAgain: Game = {
    val ng = new Game
    ng.addPlayers(players)
    ng
  }

  def score(player: Player): Int = {
    var score = player.rawScore
    val maxHandInTable = players.map( _.hand.length ).max      // The highest number of card in the hand of a player in the whole table
    val maxSpadeInTable = players.map( _.numberOfSpades ).max  // The highest number of spades in the hand of a player in the whole table
    if(player.hand.length == maxHandInTable) score += 1
    if(player.numberOfSpades == maxSpadeInTable) score += 2
    score
  }

}
