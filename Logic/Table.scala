package Logic
import scala.collection.mutable.Buffer


class Table {
  val cardsOnTable = Buffer[Card]()

  def addCard(card: Card) = cardsOnTable += card

  def removeCard(card: Card) = cardsOnTable -= card
}
