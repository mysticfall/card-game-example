package api.game.blackjack

import api.card.EvaluationStrategy.Evaluatable
import api.card.{ Hand, HandManager, PlayingCard }
import api.card.PlayingCard.Denomination._A
import api.game.blackjack.BlackJackEvaluationStrategy.{ AceAsElevenEvaluationStrategy, AceAsOneEvaluationStrategy }

sealed trait BlackJackHand extends Hand[PlayingCard] {

  def bestValue(cards: Seq[PlayingCard]): Int
}

object BlackJackHand extends HandManager[PlayingCard, BlackJackHand] {

  object Natural extends BlackJackHand {

    override def apply(cards: Seq[PlayingCard]): Boolean = {
      implicit val strategy = AceAsElevenEvaluationStrategy

      cards.size == 2 && cards.map(_.value).sum == 21
    }

    def bestValue(cards: Seq[PlayingCard]): Int = 21
  }

  object Soft extends BlackJackHand {

    override def apply(cards: Seq[PlayingCard]): Boolean = {
      !Natural(cards) && !Bust(cards) && cards.map(_.denomination).contains(_A)
    }

    def bestValue(cards: Seq[PlayingCard]): Int = {
      val values = Seq(
        calculate(cards)(AceAsElevenEvaluationStrategy),
        calculate(cards)(AceAsOneEvaluationStrategy))

      values.filterNot(_ > 21).max
    }

    private def calculate(cards: Seq[PlayingCard])(implicit s: BlackJackEvaluationStrategy) =
      cards.map(_.value).sum
  }

  object Hard extends BlackJackHand {

    implicit private val strategy = AceAsOneEvaluationStrategy

    override def apply(cards: Seq[PlayingCard]): Boolean = {
      !Natural(cards) && !Bust(cards) && !Soft(cards)
    }

    def bestValue(cards: Seq[PlayingCard]): Int = cards.map(_.value).sum
  }

  object Bust extends BlackJackHand {

    implicit private val strategy = AceAsOneEvaluationStrategy

    override def apply(cards: Seq[PlayingCard]): Boolean = cards.map(_.value).sum > 21

    def bestValue(cards: Seq[PlayingCard]): Int = 0
  }

  override def allHands: Seq[BlackJackHand] = Seq(Natural, Soft, Hard, Bust)
}
