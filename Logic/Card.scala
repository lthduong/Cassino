package Logic

case class Card(name: String, suit: String, value: Int) {

  val handValue: Int = this match {
    case Card("1", _, 1)           => 14
    case Card("10", "Diamond", 10) => 16
    case Card("2", "Spade", 2)     => 15
    case _                         => value
  }

}
