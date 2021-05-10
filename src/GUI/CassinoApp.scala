package src.GUI

import scala.swing._
import scala.swing.event.ButtonClicked
import src.Logic.{ComputerPlayer, Game, Player}

object CassinoApp extends SimpleSwingApplication {

  val save = new MenuItem("Save")
  val load = new MenuItem("Load")
  val ins  = new MenuItem("Instruction")
  val game = new Menu("Game") { contents ++= Vector(save, load) }
  val help = new Menu("Help") { contents += ins }
  val menu = new MenuBar { contents ++= Vector(game, help) }
  val newGame = new GameScreen(new Game)
  val content: Panel = new BoxPanel(Orientation.Vertical) {
    contents += FirstScreen
    contents += newGame
  }

  def top = new MainFrame {
    contents = content
    size = new Dimension(1200, 750)
    resizable = false
    menuBar = menu
  }

  this.listenTo(ins)
  this.listenTo(FirstScreen.nameCf)
  this.reactions += {
    case b: ButtonClicked => {
      val source = b.source
      source match {
        case this.ins => InstructionScreen.top.visible = true
        case FirstScreen.nameCf => {
          val game = new Game
          val nrHuman = FirstScreen.humPlayers.item
          val nrCmp = FirstScreen.cmpPlayers.item
          val names = FirstScreen.namePanels.map( _.text )
          if(names.toList.length != names.length) {
            Dialog.showMessage(content, "Please provide enough and distinct names for the players")
          } else {
            for(i <- names.indices) {
              val plr = new Player(names(i), game)
              newGame.game.addPlayer(plr)
            }
          }
          for(i <- 1 to nrCmp) {
            val cmp = new ComputerPlayer("Cmp" + i, game)
            newGame.game.addPlayer(cmp)
          }
          FirstScreen.visible = false
          content.revalidate()
          content.repaint()
        }
      }
    }
  }

}
