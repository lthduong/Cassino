package Logic
import scala.collection.mutable.Buffer

class Player(name: String, game: Game) {

  protected var score  = 0
  protected var sweep  = 0
  protected var hidden = false

  val hand = Buffer[Card]()
  val pile = Buffer[Card]()


  //These three used to get the var
  def getScore  = score
  def getSweep  = sweep
  def getHidden = hidden


  //These three used to update the var
  def updateHidden()             = { hidden = !hidden }
  def updateSweep()              = { sweep += 1 }
  def updateScore(newScore: Int) = { score = newScore }

  def capture(cardUse: Card, cardTake: Vector[Vector[Card]]) = {
    if(hand.contains(cardUse) && game.validCapture(cardUse, cardTake)) {
      cardTake.foreach( cardVector => hand ++= cardVector )
      hand += cardUse
      if(game.table.allCard.isEmpty) sweep += 1
      game.deal(this)
      game.advanceTurn()
    }
  }

  def drop(cardDrop: Card): Unit = {
    if(hand.contains(cardDrop)) {
      game.table.addCard(cardDrop)
      hand -= cardDrop
      game.deal(this)
      game.advanceTurn()
    }
  }

  // Aulixiary methods to calculate score
  def numberOfAce    = hand.count( _.name == "1" )
  def numberOfSpades = hand.count( _.suit == "S" )
  def hasDiamondTen  = hand.count( card => card.name == "0" && card.suit == "D" )
  def hasSpadeTwo    = hand.count( card => card.name == "2" && card.suit == "S" )

  // The score without number of Spades and number of Card
  def rawScore       = this.sweep + numberOfAce + hasDiamondTen * 2 + hasSpadeTwo

}


class ComputerPlayer(name: String, game: Game) extends Player(name, game) {

  def optimalMove: Unit = ???

}