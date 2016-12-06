package api.game.blackjack

import api.card.EvaluationStrategy.Evaluatable
import api.card.PlayingCard
import api.fund.FundProvider
import api.game.Dealer
import api.game.blackjack.BlackJackEvaluationStrategy.AceAsElevenEvaluationStrategy
import api.game.blackjack.BlackJackPlayer.Action.{ Action, Hit, Stand }

trait BlackJackDealer extends BlackJackPlayer with Dealer[BlackJack] {
  this: FundProvider =>

  override def needsMoreCards(player: BlackJackPlayer, game: BlackJack): Boolean = {
    player.cards.size < 2 ||
      !player.isBusted && !player.hand.exists(_.bestValue(player.cards) == 21) && player.standOrHit(game) == Hit
  }

  override def deal(player: BlackJackPlayer, game: BlackJack): PlayingCard = {
    require(player.isStanding)

    val deck = game.deck
    val card = player.cards.size match {
      case 1 if player == this => deck.draw().fold()
      case _ => deck.draw().open()
    }

    player.receive(card)

    card
  }

  override protected def doStandOrHit(game: BlackJack): Action = {
    implicit val strategy = AceAsElevenEvaluationStrategy

    if (cards.map(_.value).sum >= 17) Stand else Hit
  }
}