package api.game.blackjack

import api.game.Event.BetPlacedEvent
import api.game.{ Bet, Event, Round, Turn }

case class BettingTurn(player: BlackJack#GamblerType, round: Round[BlackJack]) extends Turn[BlackJack] {

  override def hasNext: Boolean = !player.isBusted && !finished

  override def next(): Event[BlackJack] = {
    this.finished = true

    val amount = player.bet(game)
    val bet = Bet[BlackJack](player, amount)

    BetPlacedEvent[BlackJack](bet, game)
  }

  private var finished = false
}
