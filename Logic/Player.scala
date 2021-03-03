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


  def drop(cardDrop: Card): Unit = {
    if(hand.contains(cardDrop)) {
      game.table.addCard(cardDrop)
      hand -= cardDrop
    }
  }

}


class ComputerPlayer(name: String, game: Game) extends Player(name, game) {

  def optimizeMove: Unit = ???

}