package api.game.blackjack

import api.game.Event.{ DrawEvent, LostEvent, WonEvent }
import api.game.{ Bet, Event, Turn }

case class EndingTurn(player: BlackJack#GamblerType, round: BlackJackRound) extends Turn[BlackJack] {

  val bet: Bet[BlackJack] = game.bets.find(_.gambler == player) getOrElse {
    throw new IllegalStateException(s"${ player.name } did not bet anything.")
  }

  override def hasNext: Boolean = !finished

  override def next(): Event[BlackJack] = {
    this.finished = true

    if (player.isBusted) {
      player.transfer(bet.amount, dealer)

      LostEvent[BlackJack](player, bet.amount, round)
    } else if (dealer.isBusted) {
      dealer.transfer(bet.amount, player)

      WonEvent[BlackJack](player, bet.amount, round)
    } else {
      val dealerValue = dealer.hand.map(_.bestValue(dealer.cards)) getOrElse 0
      val playerValue = player.hand.map(_.bestValue(player.cards)) getOrElse 0

      if (playerValue == dealerValue) {
        DrawEvent[BlackJack](player, round)
      } else if (dealerValue > playerValue) {
        player.transfer(bet.amount, dealer)

        LostEvent[BlackJack](player, bet.amount, round)
      } else {
        dealer.transfer(bet.amount, player)

        WonEvent[BlackJack](player, bet.amount, round)
      }
    }
  }

  private var finished = false
}
