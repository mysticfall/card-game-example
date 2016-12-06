package api.game.blackjack

import api.game.Event.CardDrawnEvent
import api.game.{ Event, Turn }

case class DealingTurn(player: BlackJackPlayer, round: BlackJackRound) extends Turn[BlackJack] {

  override def hasNext: Boolean = !finished && dealer.needsMoreCards(player, game)

  override def next(): Event[BlackJack] = {
    val card = dealer.deal(player, game)

    this.finished = player.cards.size <= 2

    CardDrawnEvent[BlackJack](player, card, game)
  }

  private var finished = false
}
