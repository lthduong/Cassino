package src.Logic

import scala.collection.mutable.Buffer


case class Player(name: String) {

  protected var score     = 0
  protected var sweep     = 0
  protected var handCards = Buffer[Card]()
  protected var pileCards = Buffer[Card]()


  // These three used to get the var
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

  // The score regardless of the number of Spades and Card
  def rawScore       =  this.sweep + numberOfAce + hasDiamondTen * 2 + hasSpadeTwo


  override def toString = this.name

}


class ComputerPlayer(name: String) extends Player(name) {


  def findCards(cardUsed: Card): Vector[Vector[Card]] = {
    val allCombos = Game.table.allCard.filter( _.value <= cardUsed.handValue ).toSet.subsets()
    val possibleCombos = allCombos.filter( combo => combo.map( _.value ).sum == cardUsed.handValue ).toVector.map( subset => subset.toVector )
    val comboCombination = possibleCombos.toSet.subsets.toVector.map( subset => subset.toVector.map( _.toVector ) )
    comboCombination.filter( setOfCombos => Game.validCapture(cardUsed, setOfCombos.flatten) ).map( _.flatten )
  }


  private def findMaxValueCard(cardVector: Vector[Card]) = {
    val cardMap = cardVector.map( _.value ).zip(cardVector).toMap
    cardMap(cardMap.keys.max)
  }

  private def findMaxElementsSubset(card: Card) = {
    val cardMap = findCards(card).map( _.length ).zip(findCards(card)).toMap
    println(cardMap.keys)
    cardMap(cardMap.keys.max)
  }

  def optimalMove(): Vector[Card] = {
    val sweepCard = this.handCards.find( card => Game.table.allCard.forall( _.value <= card.handValue ) && Game.table.allCard.map( _.value ).sum % card.handValue == 0 )
    if(sweepCard.isDefined) {
      val cardGet = Game.table.allCard.toVector
      capture(sweepCard.get, cardGet)
      Vector(sweepCard.get) ++ cardGet
    }
    else {
      val possibleCardToUse = {
        if(Game.table.allCard.length > 9) handCards.filter( _.handValue < 13 ).filter( findCards(_).nonEmpty )
        else if(Game.table.allCard.length > 11) handCards.filter( _.handValue < 10 ).filter( findCards(_).nonEmpty )
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
        println("minSum1: " + minSum._1 + ", minSum2: " + minSum._2)
        capture(minSum._1, minSum._2.toList.toVector)
        Vector(minSum._1) ++ minSum._2.toList.toVector
      }
    }
      // If sum of all cards on table = n * some card in hand's handValue (i.e, sweepable), sweep: Done
      // If there are no cards on table or no card that can be used, drop the highest value card: Done
      // If the number of card on table is 16 or greater, and there are no cards smaller than 6 in hand, drop: Done
      // 1: calculate all possible combination for each card. For each card, getting the combination that has maximum length
      // and sum it up with the cardUse value and choose base on that. Goal: Let the cards'value on the field as high as possible to not let others capture easily.
  }

}