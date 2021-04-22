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

  override def toString = this.name

}


class ComputerPlayer(name: String, game: Game) extends Player(name, game) {

  // Aulixiary method to find subsets with duplicate elements, since set does not contains duplicate
  private def findDuplicatedSubsets(sum: Int): Buffer[Buffer[Int]] = {
    val res = Buffer[Buffer[Int]]()
    for(i <- 2 to 4) {
      if (sum % i == 0) {
        res += Array.fill(i)(sum / i).toBuffer
      }
    }
    res
  }

  // Likely unnecessary
  private def findCombinations(sum: Int, takeFrom: Buffer[Int]): Buffer[Buffer[Int]] = {
    val res = Buffer[Buffer[Int]]()
    val possibleNumbers = takeFrom.filter( _ <= sum ).toSet
    possibleNumbers.subsets().filter( _.sum == sum ).foreach( res += _.toBuffer )
    for(n <- possibleNumbers) {
      val duplicateSubSets = findDuplicatedSubsets(sum - n)
      if(duplicateSubSets.nonEmpty) {
        res ++= (duplicateSubSets.map( _ :+ n ))
      }
    }
    res.filter( subset => !(subset.forall( _ == sum / 5 )) )
  }

  // TODO: Change this, one possible way is to choose only the sets that has the sum == cardUse, which can reduce the time to compute
  def findCards(cardUsed: Card) /*: Vector[Vector[Vector[Card]]]*/ = {
    val allCombos = game.table.allCard.filter( _.value <= cardUsed.handValue ).toSet.subsets()
    val possibleCombos = allCombos.filter( combo => combo.map( _.value ).sum == cardUsed.handValue ).toVector.map( subset => subset.toVector )
    //val comboCombination = possibleCombos.toSet.subsets.toVector.map( subset => subset.toVector.map( _.toVector ) )
    //comboCombination.filter( setOfCombos => this.game.validCapture(cardUsed, setOfCombos) ).drop(1)
    possibleCombos
  }

  // TODO: Complete this. Note that it might be wise to include a function that if there are too many card on the table( >= 23 ), the cmp player will not drop
  def optimalMove(): Unit = ???

}