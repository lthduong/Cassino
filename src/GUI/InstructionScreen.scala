package src.GUI

import scala.swing._

object InstructionScreen extends SimpleSwingApplication {

  val instruction = new ScrollPane(rules)

  def top = new Frame {
    title = "Instruction"
    contents = instruction
  }


}

object rules extends TextField {

}
