package src.Logic

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


  // These are used to update the var
  def updateHidden()             = { hidden = !hidden }
  def updateSweep()              = { sweep += 1       }
  def updateScore(newScore: Int) = { score = newScore }
  def addCardHand(card: Card)    = { handCards += card}
  def addCardPile(card: Card)    = { pileCards += card}



  def capture(cardUse: Card, cardTake: Vector[Card]): Unit = {
    //cardTake.foreach( cardVector => pileCards ++= cardVector )
    pileCards ++= cardTake
    pileCards += cardUse
    cardTake.foreach( this.game.table.removeCard(_) )
    handCards -= cardUse
    if(game.table.allCard.isEmpty) sweep += 1
    game.deal(this)
    game.lastCapturer = this
    game.advanceTurn()
  }

  def addCardManually(cardAdd: Buffer[Card]) = {
    this.pile ++= cardAdd
    cardAdd.foreach( this.game.table.removeCard(_) )
  }

  def addHandManually(cardAdd: Buffer[Card]) = {
    this.hand ++= cardAdd
    //cardAdd.foreach( this.game.table.removeCard(_) )
  }

  def drop(cardDrop: Card): Unit = {
    game.table.addCard(cardDrop)
    handCards -= cardDrop
    game.deal(this)
    game.advanceTurn()
  }

  // Aulixiary methods to calculate score
  def numberOfAce    = handCards.count( _.name == "1" )
  def numberOfSpades = handCards.count( _.suit == "s" )
  def hasDiamondTen  = handCards.count( card => card.name == "0" && card.suit == "d" )
  def hasSpadeTwo    = handCards.count( card => card.name == "2" && card.suit == "s" )

  // The score regardless of the number of Spades and Card
  def rawScore       = this.sweep + numberOfAce + hasDiamondTen * 2 + hasSpadeTwo

  override def toString = this.name

}


class ComputerPlayer(name: String, game: Game) extends Player(name, game) {


  def findCards(cardUsed: Card): Vector[Vector[Card]] = {
    val allCombos = game.table.allCard.filter( _.value <= cardUsed.handValue ).toSet.subsets()
    val possibleCombos = allCombos.filter( combo => combo.map( _.value ).sum == cardUsed.handValue ).toVector.map( subset => subset.toVector )
    val comboCombination = possibleCombos.toSet.subsets.toVector.map( subset => subset.toVector.map( _.toVector ) )
    comboCombination.filter( setOfCombos => this.game.validCapture(cardUsed, setOfCombos.flatten) ).map( _.flatten )
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

  //TODO: Deal with if hand == 0
  def optimalMove(): Unit = {
    val sweepCard = this.handCards.find( card => this.game.table.allCard.map( _.value ).sum % card.handValue == 0 )
    if(sweepCard.isDefined) capture(sweepCard.get, this.game.table.allCard.toVector)
    else {
      val possibleCardToUse = {
        if(this.game.table.allCard.length > 9) handCards.filter( _.handValue < 13 ).filter( findCards(_).nonEmpty )
        //else if(this.game.table.allCard.length > 14) handCards.filter( _.handValue < 10 ).filter( findCards(_).nonEmpty )
        //else if(this.game.table.allCard.length > 16) handCards.filter( _.handValue < 6 ).filter( findCards(_).nonEmpty )
        else if(this.game.table.allCard.length > 11) handCards.filter( _.handValue < 10 ).filter( findCards(_).nonEmpty )
        else handCards.filter( findCards(_).nonEmpty )
      }
      println(possibleCardToUse)
      println(possibleCardToUse.map( findCards(_)))
      if(possibleCardToUse.isEmpty || this.game.table.allCard.isEmpty) {
        drop(findMaxValueCard(handCards.toVector))
      } else {
        println()
        val cardWithHighestSubset = possibleCardToUse.zip(possibleCardToUse.map( findMaxElementsSubset(_) )).toVector
        val sum = (cardWithHighestSubset.map( card => card._1.handValue + card._2.length )).zip(cardWithHighestSubset).toMap
        val minSum = sum(sum.keys.min)
        capture(minSum._1, minSum._2)
      }
    }
      // If sum of all cards on table = n * some card in hand's handValue (i.e, sweepable), sweep: Done
      // If there are no cards on table or no card that can be used, drop the highest value card: Done
      // If the number of card on table is 16 or greater, and there are no cards smaller than 6 in hand, drop: Done
      // 1: calculate all possible combination for each card. For each card, getting the combination that has maximum length
      // and sum it up with the cardUse value and choose base on that. Goal: Let the cards'value on the field as high as possible to not let others capture easily.
  }

}