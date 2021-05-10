package src.Logic

import scala.collection.mutable.Buffer
import java.io.{BufferedReader, BufferedWriter, FileNotFoundException, FileReader, FileWriter, IOException}


class IOHandler {

  def saveGame(filePath: String) = {

    var fileContent = Buffer[String]()
    val players = Game.playersList

    // Adding title and game metadata to the file content
    fileContent += "Casino save ver 1.0"
    fileContent += "#Metadata:"
    fileContent += ("No. of players: " + players.length)

    // Adding players info
    fileContent += "#Players:"
    for(player <- players) {
      fileContent += "*Cmp: " + player.isInstanceOf[ComputerPlayer].toString
      fileContent += "Name: " + player.name
      fileContent += "Hand:"  + player.hand.map( _.toString ).mkString("")
      fileContent += "Pile:"  + player.pile.map( _.toString ).mkString("")
      fileContent += "Score:" + player.getScore
    }

    // Adding turn info
    fileContent += "#Turn:"
    fileContent += "Turn: "  + Game.getTurn
    fileContent += "Table: " + Game.table.allCard.map( _.toString ).mkString("")

    fileContent += "#END"


    try {
      val fileWriter = new FileWriter(filePath)
      val bufferWriter = new BufferedWriter(fileWriter)
      try     { bufferWriter.write(fileContent.mkString("\n")) }
      finally { bufferWriter.close() }

    } catch {
      case e: FileNotFoundException => println("Saving the data failed: Save file not found")
      case e: IOException => println("Saving the data failed: IO exception")
      case e: Throwable => println("Saving the data failed: Unknown exception.")
    }

  }



  def loadGame(filePath: String) = {

    val fileReader = new FileReader(filePath)
    val lineReader = new BufferedReader(fileReader)

    try {

      var currentLine = lineReader.readLine().trim.toLowerCase


      if(!(currentLine.startsWith("casino") && currentLine.endsWith("1.0"))) {
        throw new CorruptedCassinoFIleException("Unknown file type")
      }

      // Getting all of the lines except the header in the file
      val wholeData = {
        var buffer = Buffer[String]()
        currentLine = lineReader.readLine()
        while(currentLine != "#END") {
          buffer += currentLine
          currentLine = lineReader.readLine().trim
        }
        buffer
      }

      val metadata = {
        val dataLower = wholeData.map( _.toLowerCase.trim )
        val fromMetaOnward = wholeData.drop(dataLower.indexOf("#metadata:") + 1)
        val res = Buffer[String]()
        var n = 0
        while(n < fromMetaOnward.length && !fromMetaOnward(n).startsWith("#")) {
          res += fromMetaOnward(n)
          n += 1
        }
        res
      }

      var players = {
        val playerLower = wholeData.map( _.toLowerCase.trim )
        val fromPlayerOnward = wholeData.drop(playerLower.indexOf("#players:") + 1)
        val res = Buffer[String]()
        var n = 0
        while(n < fromPlayerOnward.length && !fromPlayerOnward(n).startsWith("#")) {
          res += fromPlayerOnward(n)
          n += 1
        }
        res
      }

      val turnInfo = {
        val turnLower = wholeData.map( _.toLowerCase.trim )
        val fromTurnOnward = wholeData.drop(turnLower.indexOf("#turn:") + 1)
        val res = Buffer[String]()
        var n = 0
        while(n < fromTurnOnward.length && !fromTurnOnward(n).startsWith("#")) {
          res += fromTurnOnward(n)
          n += 1
        }
        res
      }

      val cardsNotInDeck = Buffer[Card]()
      Game.reset()

      val nrPlr = metadata.head.drop( metadata.head.indexOf(':') + 1 ).trim.toInt
      println(nrPlr)

      // Thsse are the two helper method to add card to hand and pile
      def addCardToHand(cardString: String, player: Player): Unit = {
        var handString = cardString.toLowerCase
        while(handString.nonEmpty) {
          val cardInfo = handString.take(2)
          println(cardInfo)
          val card = new Card(cardInfo(0).toString, cardInfo(1).toString)
          player.addCardHand(card)
          cardsNotInDeck += card
          handString = handString.drop(2)
        }
      }

      def addCardToPile(cardString: String, player: Player): Unit = {
        var handString = cardString.toLowerCase
        while(handString.nonEmpty) {
          val cardInfo = handString.take(2)
          val card = new Card(cardInfo(0).toString, cardInfo(1).toString)
          player.addCardPile(card)
          cardsNotInDeck += card
          handString = handString.drop(2)
        }
      }

      def addPlayerToGame(playerInfo: Buffer[String]) = {
        val cmp = playerInfo.head.drop(playerInfo.head.indexOf(':') + 1).trim.toLowerCase  // Checking if this player is a computer player or not
        val name = playerInfo(1).drop(playerInfo(1).indexOf(':') + 1).trim
        val handCards = playerInfo(2).drop(playerInfo(2).indexOf(':') + 1).trim
        val pileCards = playerInfo(3).drop(playerInfo(3).indexOf(':') + 1).trim
        val score = playerInfo(4).drop(playerInfo(4).indexOf(':') + 1).trim
        val newPlayer = {
          if(cmp == "false") new Player(name)
          else if(cmp == "true") new ComputerPlayer(name)
          else throw new CorruptedCassinoFIleException("Something is wrong with the player infomation")
        }
        addCardToHand(handCards, newPlayer)
        addCardToPile(pileCards, newPlayer)
        newPlayer.updateScore(score.toInt)
        Game.addPlayer(newPlayer)
      }


      for(n <- 1 to nrPlr) {
        val thisPlayer = players.head +: players.drop(1).takeWhile( !_.startsWith("*") )
        addPlayerToGame(thisPlayer)
        players = players.drop(thisPlayer.size)
      }

 // Setting the turn information

      // Helper method to add card to table
      def addCardToTable(cardString: String): Unit = {
        var tableString = cardString.toLowerCase
        while(tableString.nonEmpty) {
          val cardInfo = tableString.take(2)
          val card = new Card(cardInfo(0).toString, cardInfo(1).toString)
          Game.table.addCard(card)
          cardsNotInDeck += card
          tableString = tableString.drop(2)
        }
      }

      val turn = turnInfo(0).drop(turnInfo(0).indexOf(':') + 1).trim
      Game.setTurn(turn.toInt)                                                 // Set up turn

      val tableCard = turnInfo(1).drop(turnInfo(1).indexOf(':') + 1).trim
      addCardToTable(tableCard)                                                // Set up table

      cardsNotInDeck.foreach( card => Game.removeFromDeck(card) )              // Remove the cards taht are not in the deck from the deck

    } catch {
      case e: FileNotFoundException =>
              val cassinoExc = new CorruptedCassinoFIleException("Loading the data failed: Save file not found")
              cassinoExc.initCause(e)
              throw cassinoExc
      case e: IOException =>
              val cassinoExc = new CorruptedCassinoFIleException("Loading the data failed: IO exception")
              cassinoExc.initCause(e)
              throw cassinoExc
      case e: Throwable =>
              val cassinoExc = new CorruptedCassinoFIleException("Loading the data failed: Unknown exception")
              cassinoExc.initCause(e)
              throw cassinoExc
    }

  }

}
