package api.card

import api.card.PlayingCard.Denomination.Denomination
import api.card.PlayingCard.Suit.Suit

case class PlayingCard(suite: Suit, denomination: Denomination) extends Card {

  override def name: String = {
    val suiteSymbol = new String(Character.toChars(Integer.parseInt(suite.toString.drop(2), 16)))

    suiteSymbol + denomination.toString.tail
  }
}

object PlayingCard {

  def pack: Seq[PlayingCard] = {
    val cards = for {
      suit <- Suit.values
      pip <- Denomination.values
    } yield {
      PlayingCard(suit, pip)
    }

    cards.toSeq
  }

  object Suit extends Enumeration {

    type Suit = Value

    val ♠, ♡, ♣, ♢ = Value
  }

  object Denomination extends Enumeration(1) {

    type Denomination = Value

    val _A, _2, _3, _4, _5, _6, _7, _8, _9, _10, _J, _Q, _K = Value
  }

  implicit class Builder(suit: Suit) {

    def apply(denomination: Denomination): PlayingCard = PlayingCard(suit, denomination)
  }
}
