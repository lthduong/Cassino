package Logic

import scala.collection.mutable.Buffer
import java.io.{BufferedWriter, FileNotFoundException, FileWriter, IOException}


object IOHandler {

  def saveGame(filePath: String, game: Game) = {

    var fileContent = Buffer[String]()
    val players = game.playersList

    // Adding title and game metadata to the file content
    fileContent += "Casino save ver 1.0"
    fileContent += "#Game metadata:"
    fileContent += ("No. of players: " + players.length)
    fileContent += ("No. of comp players: " + players.count( _.isInstanceOf[ComputerPlayer] ))


    // Adding players
    fileContent += "#Players:"
    for(player <- players) {
      fileContent += "*" + player.name + ":"
      fileContent += "Cmp: " + player.isInstanceOf[ComputerPlayer].toString
      fileContent += "Hand:" + player.hand.map( _.getName ).mkString("")
      fileContent += "Pile:" + player.pile.map( _.getName ).mkString("")
      fileContent += "Score:" + player.getScore
    }

    // Adding turn info
    fileContent += "#Turn"
    fileContent += "Turn player: " + game.playerTurn.name
    fileContent += "Cards left: " + game.deckRemain
    fileContent += "Table: " + game.table.allCard.map( _.getName ).mkString("")

    fileContent += "#END"


    try {
      val fileWriter = new FileWriter(filePath)
      val bufferWriter = new BufferedWriter(fileWriter)
      try     { bufferWriter.write(fileContent.mkString("\n")) }
      finally { bufferWriter.close() }

    } catch {
      case _: FileNotFoundException => println("Error with saving game data: Save file not found")
      case _: IOException => println("Error with saving game data: IOException")
      case _: Throwable => println("Error with saving game data: Unknown exception.")
    }

  }


}
