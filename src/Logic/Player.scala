package src.Logic

import scala.collection.mutable.Buffer


case class Player(name: String) {

  // Variables to store the state of a player
  protected var score     = 0
  protected var sweep     = 0
  protected var handCards = Buffer[Card]()
  protected var pileCards = Buffer[Card]()


  // These are used to get the var
  def getScore  = score
  def getSweep  = sweep
  def hand   = handCards
  def pile   = pileCards


  // These are used to update the var
  def updateSweep()              = { sweep += 1       }
  def updateScore(newScore: Int) = { score = newScore }
  def addCardHand(card: Card)    = { handCards += card}
  def addCardPile(card: Card)    = { pileCards += card}


  def capture(cardUse: Card, cardTake: Vector[Card]): Unit = {
    pileCards ++= cardTake
    pileCards += cardUse
    cardTake.foreach( Game.table.removeCard(_) )
    handCards -= cardUse
    if(Game.table.allCard.isEmpty) sweep += 1
    Game.deal(this)
    Game.lastCapturer = Some(this)
  }

  def drop(cardDrop: Card): Unit = {
    Game.table.addCard(cardDrop)
    handCards -= cardDrop
    Game.deal(this)
  }


  // Aulixiary methods to calculate score
  def numberOfAce    = pileCards.count( _.name == "1" )
  def numberOfSpades = pileCards.count( _.suit == "s" )
  def hasDiamondTen  = pileCards.count( card => card.name == "0" && card.suit == "d" )
  def hasSpadeTwo    = pileCards.count( card => card.name == "2" && card.suit == "s" )
  // The score without taking into account the number of Spades and Card
  def rawScore       =  this.sweep + numberOfAce + hasDiamondTen * 2 + hasSpadeTwo


  override def toString = this.name

}


class ComputerPlayer(name: String) extends Player(name) {

  // This method is used to get the valid combinations of a given card
  def findCards(cardUsed: Card): Vector[Vector[Card]] = {
    val allCombos = Game.table.allCard.filter( _.value <= cardUsed.handValue ).toSet.subsets()
    val possibleCombos = allCombos.filter( combo => combo.map( _.value ).sum == cardUsed.handValue ).toVector.map( subset => subset.toVector )
    val comboCombination = possibleCombos.toSet.subsets.toVector.map( subset => subset.toVector.map( _.toVector ) )
    comboCombination.filter( setOfCombos => Game.validCapture(cardUsed, setOfCombos.flatten) ).map( _.flatten )
  }

  // Some helper methods for the optimalMove
  private def findMaxValueCard(cardVector: Vector[Card]) = {
    val cardMap = cardVector.map( _.value ).zip(cardVector).toMap
    cardMap(cardMap.keys.max)
  }

  private def findMaxElementsSubset(card: Card) = {
    val cardMap = findCards(card).map( _.length ).zip(findCards(card)).toMap
    cardMap(cardMap.keys.max)
  }


  // The optimalMove method. This method first check if the table can be sweeped, if yes, sweep the table.
  // If the table cannot be sweeped, the medthod choose possible cards to used based on the number of cards left on the table.
  // If no card can be captured, the method drop the card with the highest value to the table.
  // Else the card that has the smallest sum of its handValue with the size of its maximum combo will be used.
  def optimalMove(): Vector[Card] = {
    val sweepCard = this.handCards.find( card => Game.validCapture(card, Game.table.allCard.toVector) )
    if(sweepCard.isDefined) {
      val cardGet = Game.table.allCard.toVector
      capture(sweepCard.get, cardGet)
      Vector(sweepCard.get) ++ cardGet
    }
    else {
      val possibleCardToUse = {
        if(Game.table.allCard.length > 9) handCards.filter( _.handValue < 13 ).filter( findCards(_).nonEmpty )
        else if(Game.table.allCard.length > 11) handCards.filter( _.handValue < 9 ).filter( findCards(_).nonEmpty )
        else handCards.filter( findCards(_).nonEmpty )
      }
      if(possibleCardToUse.isEmpty || Game.table.allCard.isEmpty) {
        val cardUsed = findMaxValueCard(handCards.toVector)
        drop(cardUsed)
        Vector(cardUsed)
      } else {
        val cardWithHighestSubset = possibleCardToUse.zip(possibleCardToUse.map( findMaxElementsSubset(_) )).toVector
        val sum = (cardWithHighestSubset.map( card => card._1.handValue + card._2.length )).zip(cardWithHighestSubset).toMap
        val minSum = sum(sum.keys.min)
        capture(minSum._1, minSum._2.toSet.toVector)
        Vector(minSum._1) ++ minSum._2.toSet.toVector
      }
    }
  }

}