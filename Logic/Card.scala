package Logic

case class Card(name: String, suit: String, value: Int) {

  val handValue: Int = this match {
    case Card("1", _  , 1 ) => 14
    case Card("0", "d", 10) => 16
    case Card("2", "s", 2 ) => 15
    case _                  => value
  }

  def getName = name + suit

}
