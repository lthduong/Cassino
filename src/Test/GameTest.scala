package src.Test

import src.Logic._

import scala.collection.mutable.Buffer

object GameTest extends App {

  val game = new Game
  val table = game.table

  val player1 = new Player("P1", game)
  val player2 = new Player("P2", game)
  val player3 = new Player("P3", game)
  val cmpPlayer = new ComputerPlayer("CMP1", game)
  val allPlayers = Vector(player1, player2, player3, cmpPlayer)

  allPlayers.foreach( player => game.addPlayer(player) )

  //Get turn
  println("Turn player: " + game.playerTurn)

  /* Values for situational test
  val c1 = new Card("j", "c")
  val c2 = new Card("j", "h")
  val c3 = new Card("1", "c")
  val c4 = new Card("2", "h")
  Vector(c1, c2, c3, c4).foreach( game.removeFromDeck(_) )
  */

  // Deal cards to table and check
  for(n <- 1 to 10) game.shuffle()
  for(n <- 1 to 12) game.dealTable()


  /* Values for situational test
  val tb1 = new Card("3", "c")
  val tb2 = new Card("0", "c")
  val tb3 = new Card("6", "s")
  val tb4 = new Card("q", "h")
  val tb5 = new Card("7", "d")
  val tb6 = new Card("5", "s")
  val tb7 = new Card("8", "s")
  val tb8 = new Card("5", "d")
  val tb9 = new Card("7", "c")
  val tb10 = new Card("q", "d")
  val tb11 = new Card("0", "h")
  val tb12 = new Card("4", "h")
  Vector(tb1, tb2, tb3, tb4, tb5, tb6, tb7, tb8, tb9, tb10, tb11, tb12).foreach( game.table.addCard(_) )
  */


  println("Table Card: " + game.table.allCard)


  // Checking validTrade:
  val card1 = new Card("6", "h")
  val card2 = new Card("1", "d")
  val card3 = new Card("5", "s")
  val card4 = new Card("2", "c")
  val card5 = new Card("3", "s")
  val card6 = new Card("1", "s")
  //val valid = game.validCapture(card1, Vector(card2, card3, card6, card4, card5))
  //println(valid)

  // Deal cards to players and check
  game.playersList.foreach( player => game.deal(player) )
  game.playersList.foreach( player => println(player.name + " hand: " + player.hand) )

  // Checking findCards
  //println(cmpPlayer.findCards(new Card("6", "d")))
  //println(cmpPlayer.findCards(new Card("9", "h")))
  //println(cmpPlayer.findCards(new Card("0", "h")))
  //println(cmpPlayer.findCards(new Card("j", "h")))
  //println(cmpPlayer.findCards(new Card("q", "d")))
  //println(cmpPlayer.findCards(new Card("k", "h")))
  //println(cmpPlayer.findCards(new Card("1", "h")))
  //println(cmpPlayer.findCards(new Card("2", "s")))
  //println(cmpPlayer.findCards(new Card("0", "d")))
  // If number of cards on table is too large, and the card that need to find is higher than 13, then the method will take too long to run

  // Checking optimalMove
  //cmpPlayer.addHandManually(Buffer(c1, c2, c3, c4))
  cmpPlayer.optimalMove()
  println("Turn: " + game.playerTurn)
  println("Table Card: " + game.table.allCard)
  println("Cmp hand: " + cmpPlayer.hand)
  println("Cmp pile: " + cmpPlayer.pile)

  game.winners.foreach( winner => println( winner + "\n" ) )

}
