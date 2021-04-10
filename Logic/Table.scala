package Logic

import scala.collection.mutable.Buffer


class Table {
  private val cardsOnTable = Buffer[Card]()

  def allCard = cardsOnTable

  def hasCard(card: Card)= cardsOnTable.contains(card)

  def addCard(card: Card) = cardsOnTable += card

  def removeCard(card: Card) = cardsOnTable -= card
}
