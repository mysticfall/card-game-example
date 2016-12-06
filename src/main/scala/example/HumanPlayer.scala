package example

import java.text.NumberFormat

import scala.io.StdIn.{ readBoolean, readDouble }

import api.game.Gambler
import api.game.blackjack.BlackJackPlayer.Action._
import api.game.blackjack.{ BlackJack, BlackJackPlayer }

case class HumanPlayer(name: String, initialFund: Double) extends BlackJackPlayer with Gambler[BlackJack] {

  receive(initialFund)

  override def bet(game: BlackJack): Double = {
    print("How much money do you want to bet? ")

    try {
      val amount = readDouble()

      if (amount > cash) {
        val nf = NumberFormat.getCurrencyInstance

        println(s"You only have ${ nf.format(cash) }.")

        bet(game)
      } else {
        println()

        amount
      }
    } catch {
      case _: NumberFormatException =>
        println("Please enter valid amount.")

        bet(game)
    }
  }

  override protected def doStandOrHit(game: BlackJack): Action = {
    print(s"Your hand is ${ cards.map(_.toString).mkString(", ") }. ")
    print("Do you want to take another card? (Enter 'y' to take): ")

    if (readBoolean()) Hit else Stand
  }
}
