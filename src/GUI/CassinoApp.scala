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

  val content: Panel = new BoxPanel(Orientation.Vertical) {
    contents += FirstScreen
    contents += GameScreen
    this.listenTo(FirstScreen.nameCf)
    this.reactions += {
      case b: ButtonClicked => {
        val source = b.source
        source match {
          case FirstScreen.nameCf => {
            val nrHuman = FirstScreen.humPlayers.item
            val nrCmp = FirstScreen.cmpPlayers.item
            val names = FirstScreen.namePanels.map( _.text )
            val players = names.map( name => new Player(name) )
            Game.newGame(nrCmp, players)
            FirstScreen.visible = false
            GameScreen.visible = true
            GameScreen.revalidate()
            GameScreen.repaint()
          }
        }
      }
    }

  }

  def top = new MainFrame {
    contents = content
    size = new Dimension(1200, 750)
    resizable = false
    menuBar = menu
  }

  this.listenTo(ins)
  this.reactions += {
    case b: ButtonClicked => {
      val source = b.source
      source match {
        case ins => InstructionScreen.top.visible = true
      }
    }
  }


}
