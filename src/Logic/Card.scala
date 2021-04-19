package src.Logic

import java.awt.image

case class Card(name: String, suit: String) {

  val value: Int = {
    this.name match {
      case "1" => 1
      case "2" => 2
      case "3" => 3
      case "4" => 4
      case "5" => 5
      case "6" => 6
      case "7" => 7
      case "8" => 8
      case "9" => 9
      case "0" => 10
      case "j" => 11
      case "q" => 12
      case "k" => 13
      case _   => 0 // Unknown card
    }
  }

  val handValue: Int = {
    this match {
      case Card("1", _  ) => 14
      case Card("0", "d") => 16
      case Card("2", "s") => 15
      case _              => value
    }
  }

  def getName = name + suit

}
