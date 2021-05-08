package src.Test

import src.Logic._

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

  // Deal cards to table and check
  for(n <- 1 to 10) game.shuffle()
  for(n <- 1 to 16) game.dealTable()
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
  cmpPlayer.optimalMove()
  println("Turn: " + game.playerTurn)
  println("Table Card: " + game.table.allCard)
  println("Cmp hand: " + cmpPlayer.hand)
  println("Cmp pile: " + cmpPlayer.pile)

  game.winners.foreach( winner => println( winner + "\n" ) )

}
