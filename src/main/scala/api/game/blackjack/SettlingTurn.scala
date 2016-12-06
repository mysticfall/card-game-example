package api.game.blackjack

import api.game.Event.{ DrawEvent, LostEvent, WonEvent }
import api.game.blackjack.BlackJackHand.Natural
import api.game.{ Bet, Event, Turn }

case class SettlingTurn(player: BlackJack#GamblerType, round: BlackJackRound) extends Turn[BlackJack] {

  val bet: Bet[BlackJack] = game.bets.find(_.gambler == player) getOrElse {
    throw new IllegalStateException(s"${ player.name } did not bet anything.")
  }

  val playerHasNatural: Boolean = player.hand.contains(Natural)

  val dealerHasNatural: Boolean = dealer.hand.contains(Natural)

  override def hasNext: Boolean = (playerHasNatural || dealerHasNatural) && !finished

  override def next(): Event[BlackJack] = {
    this.finished = true

    if (playerHasNatural) {
      if (dealerHasNatural) {
        DrawEvent[BlackJack](player, round)
      } else {
        val amount = bet.amount * game.winRatioOnBlackJack

        dealer.transfer(amount, player)

        WonEvent[BlackJack](player, amount, round)
      }
    } else {
      player.transfer(bet.amount, dealer)

      LostEvent[BlackJack](player, bet.amount, round)
    }
  }

  private var finished = false
}
