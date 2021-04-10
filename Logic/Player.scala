package Logic

import scala.collection.mutable.Buffer


case class Player(name: String, game: Game) {

  protected var score     = 0    // Might not be needed
  protected var sweep     = 0
  protected var hidden    = false
  protected var handCards = Buffer[Card]()
  protected var pileCards = Buffer[Card]()


  // These three used to get the var
  def getScore  = score       // Might not be needed
  def getSweep  = sweep
  def getHidden = hidden
  def hand   = handCards
  def pile   = pileCards


  // These three used to update the var
  def updateHidden()             = { hidden = !hidden }
  def updateSweep()              = { sweep += 1       }
  def updateScore(newScore: Int) = { score = newScore }
  def addCardHand(card: Card)    = { handCards += card}
  def addCardPile(card: Card)    = { pileCards += card}



  def capture(cardUse: Card, cardTake: Vector[Vector[Card]]) = {
    if(handCards.contains(cardUse) && game.validCapture(cardUse, cardTake)) {
      cardTake.foreach( cardVector => handCards ++= cardVector )
      handCards += cardUse
      if(game.table.allCard.isEmpty) sweep += 1
      game.deal(this)
      game.advanceTurn()
    }
  }

  def drop(cardDrop: Card): Unit = {
    if(handCards.contains(cardDrop)) {
      game.table.addCard(cardDrop)
      handCards -= cardDrop
      game.deal(this)
      game.advanceTurn()
    }
  }

  // Aulixiary methods to calculate score
  def numberOfAce    = handCards.count( _.name == "1" )
  def numberOfSpades = handCards.count( _.suit == "s" )
  def hasDiamondTen  = handCards.count( card => card.name == "0" && card.suit == "d" )
  def hasSpadeTwo    = handCards.count( card => card.name == "2" && card.suit == "s" )

  // The score regardless of the number of Spades and Card
  def rawScore       = this.sweep + numberOfAce + hasDiamondTen * 2 + hasSpadeTwo

}


class ComputerPlayer(name: String, game: Game) extends Player(name, game) {

  def optimalMove(): Unit = ???

}