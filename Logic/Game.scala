package Logic

import scala.collection.mutable.Buffer
import scala.util.Random
import Logic.IOHandler

class Game {

  val table = new Table
  val handler = new IOHandler

  private var players = Buffer[Player]()
  private var turn = players.head
  private val deck: Buffer[Card] = {
    var cardList = Buffer[Card]()
    val suit = Vector("s", "d", "c", "h")
    val name = (0 to 9).map( _.toString ).toVector ++ Vector("j", "q", "k")
    for(n <- 0 to 51) {
      cardList = cardList :+ new Card(name(n % name.length), suit(n % suit.length))
    }
    cardList
  }


  // Some methods to help getting values of the vars
  def playersList = players
  def playerTurn = this.turn

  // Use to advancing turn
  def advanceTurn() = { turn = players((players.indexOf(turn) + 1) % players.length) }

  //Use to set turn to a specific player
  def setTurn(playerName: String) = {
    if(players.map( _.name ).contains(playerName)) turn = players.find( _.name == playerName ).get
  }

  //Use to remove a specific card from the deck
  def removeFromDeck(card: Card) = if(deck.contains(card)) deck -= card

  def addPlayers(player: Player): Unit = { players = players :+ player }

  def shuffle = Random.shuffle(deck)

  def validCapture(cardUse: Card, cardTake: Vector[Vector[Card]]): Boolean = {
    val checkSum = cardTake.forall( cardVector => cardVector.map( _.value ).sum == cardUse.handValue )
    var checkOverlap = true
    for(vector <- cardTake) {
      val theRest = cardTake.filter( _ != vector ).flatten  // The flattened cardTake with the vector in check removed
      val overlap = vector.intersect(theRest).isEmpty       // Find out if the Rest and vector is overlap
      checkOverlap = checkOverlap && overlap                // Update the check overlap
    }
    checkSum && checkOverlap
  }

  //Deal cards to the targeted player until he has 4 cards
  def deal(player: Player): Unit = {
    if(players.contains(player)) {
      this.shuffle
      while(player.hand.size < 4 && deck.nonEmpty) {
        player.addCardHand(deck.head)
        deck -= deck.head
      }
    }
  }

  //Deal cards to the table
  def dealTable(): Unit = {
    table.addCard(deck.head)
    deck -= deck.head
  }

  def playAgain: Game = {
    val ng = new Game
    players.foreach(ng.addPlayers)
    ng
  }

  def calculateScore(player: Player): Unit = {
    var score = player.rawScore
    val maxHandInTable = players.map( _.hand.length ).max      // The highest number of card in the hand of a player in the whole table
    val maxSpadeInTable = players.map( _.numberOfSpades ).max  // The highest number of spades in the hand of a player in the whole table
    if(player.hand.length == maxHandInTable) score += 1
    if(player.numberOfSpades == maxSpadeInTable) score += 2
    player.updateScore(score)
  }

}
