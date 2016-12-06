package api.game

import api.card.CardHolder
import api.fund.FundProvider

trait Player[A <: Game[A]] extends CardHolder[A#CardType] with FundProvider {

  def name: String

  def hand: Option[A#HandType]

  override def toString: String = name
}