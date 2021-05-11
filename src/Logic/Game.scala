package src.Logic

import scala.collection.mutable.Buffer
import scala.util.Random


object Game {

  val table = new Table
  val handler = new IOHandler
  var lastCapturer: Option[Player] = None
  var nrCmpPlr = 0
  var nrHumPlr = 0

  private var players = Buffer[Player]()
  private var turn    = 0

  private var deck: Buffer[Card] = {
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
  def getTurn     = turn
  def playerTurn  = players(turn % players.length)
  def getDeck     = this.deck
  def isOver      = players.forall( _.hand.isEmpty )
  def winners     = {
    val playerByScore = players.map( calculateScore(_) ).zip(players).groupBy( _._1 )
    playerByScore(playerByScore.keys.max).map( _._2 )
  }

  // Use to advancing turn
  def advanceTurn() = { turn += 1 }

  // Use to set turn to a specific player
  def setTurn(number: Int) = { turn = number }

  // Use to remove a specific card from the deck
  def removeFromDeck(card: Card) = if(deck.contains(card)) deck -= card

  def addPlayer(player: Player): Unit = { players = players :+ player }

  def shuffle() = deck = Random.shuffle(deck)

  // To check if the card vector is overlapped, first zip the vector of combo with its values and convert to Set, and then
  // sum all the value, if the value is n * cardUse.handValue, then there is no overlap
  def validCapture(cardUse: Card, cardTake: Vector[Card]): Boolean = {
    val cardSum = cardTake.zip(cardTake.map( _.value )).toSet.toMap.values.sum
    (cardSum != 0) && (cardSum % cardUse.handValue == 0) && (cardTake.forall( _.value <= cardUse.handValue ))
  }

  // Deal cards to the targeted player until he has 4 cards
  def deal(player: Player): Unit = {
    this.shuffle()
    while(player.hand.size < 4 && deck.nonEmpty) {
      player.addCardHand(deck.head)
      deck -= deck.head
    }
  }

  //Deal cards to the table
  def dealTable(): Unit = {
    table.addCard(deck.head)
    deck -= deck.head
  }

  def calculateScore(player: Player): Int = {
    var score = player.rawScore
    val maxPile = players.map( _.pile.length ).max      // The highest number of card in the hand of a player in the whole table
    val maxSpadeInTable = players.map( _.numberOfSpades ).max  // The highest number of spades in the hand of a player in the whole table
    if(player.pile.length == maxPile) score += 1
    if(player.numberOfSpades == maxSpadeInTable) score += 2
    player.updateScore(score)
    score
  }

  def newGame(cmpPlayer: Int, player: Vector[Player]): Unit = {
    players = player.toBuffer
    nrCmpPlr = cmpPlayer
    nrHumPlr = player.length
    for(i <- 1 to cmpPlayer) {
      this.addPlayer( new ComputerPlayer("Cmp" + i) )
    }
    this.playersList.foreach( deal(_) )
    (1 to 4).foreach( i => this.dealTable() )
  }

  def reset() = {
    Game.table.allCard.foreach( Game.table.removeCard(_) )
    players = Buffer[Player]()
    nrHumPlr = 0
    nrCmpPlr = 0
    lastCapturer = None
    turn = 0
    deck = {
      var cardList = Buffer[Card]()
      val suit = Vector("s", "d", "c", "h")
      val name = (0 to 9).map( _.toString ).toVector ++ Vector("j", "q", "k")
      for(n <- 0 to 51) {
        cardList = cardList :+ new Card(name(n % name.length), suit(n % suit.length))
      }
      cardList
    }
  }

}
