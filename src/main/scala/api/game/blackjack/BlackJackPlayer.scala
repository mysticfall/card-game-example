package api.game.blackjack

import api.game.Player
import api.game.blackjack.BlackJackHand.Bust
import api.game.blackjack.BlackJackPlayer.Action.Action

trait BlackJackPlayer extends Player[BlackJack] {

  def isBusted: Boolean = hand.contains(Bust)

  def isStanding: Boolean = !isBusted

  @throws[IllegalStateException]
  def standOrHit(game: BlackJack): Action = {
    if (isBusted) {
      throw new IllegalStateException(s"$this has been busted already.")
    }

    doStandOrHit(game)
  }

  protected def doStandOrHit(game: BlackJack): Action

  override def hand: Option[BlackJackHand] = BlackJackHand(cards)
}

object BlackJackPlayer {

  object Action extends Enumeration {

    type Action = Value

    val Stand, Hit = Value
  }
}