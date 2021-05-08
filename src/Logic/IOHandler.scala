package src.Logic

import scala.collection.mutable.Buffer
import java.io.{BufferedReader, BufferedWriter, FileNotFoundException, FileReader, FileWriter, IOException}


class IOHandler {

  def saveGame(filePath: String, game: Game) = {

    var fileContent = Buffer[String]()
    val players = game.playersList

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
      //fileContent += "Score:" + player.getScore
    }

    // Adding turn info
    fileContent += "#Turn"
    fileContent += "Turn: "  + game.getTurn
    fileContent += "Table: " + game.table.allCard.map( _.toString ).mkString("")

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



  def loadGame(filePath: String): Game = {

    val game = new Game

    val fileReader = new FileReader(filePath)
    val lineReader = new BufferedReader(fileReader)

    try {

      var currentLine = lineReader.readLine().trim.toLowerCase


      if(!(currentLine.startsWith("#cassino") && currentLine.endsWith("1.0"))) {
        throw new CorruptedCassinoFIleException("Unknown file type")
      }

      // Getting all of the lines except the header in the file
      val wholeData = {
        var buffer = Buffer[String]()
        currentLine = lineReader.readLine()
        while(currentLine != null) {
          buffer += currentLine
          currentLine = lineReader.readLine().trim
        }
        buffer
      }

      // Getting metadata into a buffer
      val dataBuffer = {
        val lowerData = wholeData.map( _.toLowerCase )
        val fromMetaOnward = wholeData.drop(lowerData.indexOf("#metadata:") + 1)
        var returnBuffer = Buffer[String]()
        var n = 0
        while(n <= (fromMetaOnward.size - 1) && !fromMetaOnward(n).startsWith("#")) {
          returnBuffer += fromMetaOnward(n)
          n += 1
        }
        returnBuffer.filter( _ != "" )
      }

      // Getting players infomation into a buffer
      var playerBuffer = {
        val lowerData = wholeData.map( _.toLowerCase )
        val fromPlayerOnward = wholeData.drop(lowerData.indexOf("#players:") + 1)
        var returnBuffer = Buffer[String]()
        var n = 0
        while(n <= (fromPlayerOnward.size - 1) && !fromPlayerOnward(n).startsWith("#")) {
          returnBuffer += fromPlayerOnward(n)
          n += 1
        }
        returnBuffer.filter( _ != "" )
      }

      // Getting turn infomation into a buffer
      val turnBuffer = {
        val lowerData = wholeData.map( _.toLowerCase )
        val fromTurnOnward = wholeData.drop(lowerData.indexOf("turn:") + 1)
        var returnBuffer = Buffer[String]()
        var n = 0
        while(n <= (fromTurnOnward.size - 1) && !fromTurnOnward(n).startsWith("#")) {
          returnBuffer += fromTurnOnward(n)
          n += 1
        }
        returnBuffer.filter( _ != "" )
      }

      // Getting the number of player
      val numberOfPlayers = dataBuffer.head.drop(dataBuffer.head.indexOf(':') + 1).trim.toInt

      //These are the cards that will not be in the deck
      val cardsNotInDeck = Buffer[Card]()


      // Adding players to the game

      // Thsse are the two helper method to add card to hand and pile
      def addCardToHand(cardString: String, player: Player): Unit = {
        var handString = cardString.toLowerCase
        while(handString.nonEmpty) {
          val cardInfo = handString.take(2)
          val card = new Card(cardInfo(0).toString, cardInfo(2).toString)
          player.addCardHand(card)
          cardsNotInDeck += card
          handString = handString.drop(2)
        }
      }

      def addCardToPile(cardString: String, player: Player): Unit = {
        var handString = cardString.toLowerCase
        while(handString.nonEmpty) {
          val cardInfo = handString.take(2)
          val card = new Card(cardInfo(0).toString, cardInfo(2).toString)
          player.addCardPile(card)
          cardsNotInDeck += card
          handString = handString.drop(2)
        }
      }

      // Adding players to the game
      def addPlayerToGame(playerInfo: Buffer[String], game: Game) = {
        val cmp = playerInfo.head.drop(playerInfo.head.indexOf(':') + 1).trim.toLowerCase  // Checking if this player is a computer player or not
        val name = playerInfo(1).drop(playerInfo(1).indexOf(':') + 1).trim
        val handCards = playerInfo(2).drop(playerInfo(2).indexOf(':') + 1).trim
        val pileCards = playerInfo(3).drop(playerInfo(3).indexOf(':') + 1).trim
        val score = playerInfo(4).drop(playerInfo(4).indexOf(':') + 1).trim
        val newPlayer = {
          if(cmp == "false") new Player(name, game)
          else if(cmp == "true") new ComputerPlayer(name, game)
          else throw new CorruptedCassinoFIleException("Something is wrong with the player infomation")
        }
        addCardToHand(handCards, newPlayer)
        addCardToPile(pileCards, newPlayer)
        //newPlayer.updateScore(score.toInt)
        game.addPlayer(newPlayer)
      }


      for(n <- 1 to numberOfPlayers) {
        val thisPlayer = playerBuffer.head +: playerBuffer.drop(1).takeWhile( !_.startsWith("*") )
        addPlayerToGame(thisPlayer, game)
        playerBuffer = playerBuffer.drop(thisPlayer.size)
      }


      // Setting the turn information

      // Helper method to add card to table
      def addCardToTable(cardString: String, game: Game): Unit = {
        var tableString = cardString.toLowerCase
        while(tableString.nonEmpty) {
          val cardInfo = tableString.take(2)
          val card = new Card(cardInfo(0).toString, cardInfo(2).toString)
          game.table.addCard(card)
          cardsNotInDeck += card
          tableString = tableString.drop(2)
        }
      }

      val turn = turnBuffer(0).drop(turnBuffer(0).indexOf(':') + 1).trim
      game.setTurn(turn.toInt)                                                 // Set up turn

      val tableCard = turnBuffer(1).drop(turnBuffer(1).indexOf(':') + 1).trim
      addCardToTable(tableCard, game)                                          // Set up table

      cardsNotInDeck.foreach( card => game.removeFromDeck(card) )              // Remove the cards taht are not in the deck from the deck

      // Return the game object created
      game

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
