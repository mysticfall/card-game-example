package api.game.blackjack

import scala.math.min

import api.card.PlayingCard.Denomination._
import api.card.{ EvaluationStrategy, PlayingCard }

sealed trait BlackJackEvaluationStrategy extends EvaluationStrategy[PlayingCard]

object BlackJackEvaluationStrategy {

  object AceAsOneEvaluationStrategy extends BlackJackEvaluationStrategy {

    override def valueOf(card: PlayingCard): Int = min(card.denomination.id, 10)
  }

  object AceAsElevenEvaluationStrategy extends BlackJackEvaluationStrategy {

    override def valueOf(card: PlayingCard): Int = {
      card.denomination match {
        case pip if pip == _A => 11
        case pip => min(pip.id, 10)
      }
    }
  }
}