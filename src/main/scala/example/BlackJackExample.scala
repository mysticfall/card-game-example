package example

import java.text.NumberFormat

import api.fund.InfiniteCashSource
import api.game.Event._
import api.game.blackjack.BlackJackHand.Natural
import api.game.blackjack.{ BlackJack, BlackJackDealer, EndingTurn }
import api.game.{ Bet, Event }

class BlackJackExample(val gamblers: Seq[BlackJack#GamblerType]) extends BlackJack {

  override val dealer: BlackJackDealer = new BlackJackDealer with InfiniteCashSource

  override protected def onEvent: PartialFunction[Event[BlackJack], Unit] = {
    case GameStartedEvent(_) =>
      println("Welcome to the BlackJack example.")
    case RoundStartedEvent(round) =>
      println()
      println(s"Starting round ${ round.number }.")
      println()
      println(s"Remaining players:")

      round.game.gamblers.filterNot(_.isBroke) foreach {
        player => println(s" * ${ player.name } (${ nf.format(player.cash) })")
      }

      println()
    case TurnStartedEvent(turn: EndingTurn) if turn.game.bets.headOption.exists(_.gambler == turn.player) =>
      println(s"${ turn.dealer.name }'s hand is ${ turn.dealer.cards.map(_.toString).mkString(", ") }.")
    case BetPlacedEvent(Bet(gambler, amount), _) if !gambler.isInstanceOf[HumanPlayer] =>
      println(s"${ gambler.name } has bet ${ nf.format(amount) }.")
    case CardDrawnEvent(player, card, _) =>
      println(s"${ player.name } has drawn $card (Hand: ${ player.cards.map(_.toString).mkString(", ") }).")

      if (player.isBusted) {
        println(s"${ player.name } was busted!")
      } else if (player.hand.contains(Natural)) {
        println(s"${ player.name } has natural!")
      }
    case WonEvent(player, amount, _) =>
      println(s"${ player.name } has won ${ nf.format(amount) } (Total: ${ nf.format(player.cash) }).")
    case DrawEvent(player, _) =>
      println(s"${ player.name } was tied with the dealer (Total: ${ nf.format(player.cash) }).")
    case LostEvent(player, amount, _) =>
      println(s"${ player.name } has lost ${ nf.format(amount) } (Total: ${ nf.format(player.cash) }).")

      if (player.isBroke) {
        println(s"${ player.name } is broke!")
      }
    case GameEndedEvent(_) =>
      println()
      println("Good bye!")
  }

  private val nf = NumberFormat.getCurrencyInstance
}

object BlackJackExample {

  def main(args: Array[String]): Unit = {
    val players = Seq(HumanPlayer("Player", 5000))
    val game = new BlackJackExample(players)

    game.play()
  }
}