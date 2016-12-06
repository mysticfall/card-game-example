package api.game.blackjack

import api.card.{ Deck, PlayingCard }
import api.game.{ Game, Round }

trait BlackJack extends Game[BlackJack] {
  this: BlackJack =>

  override type CardType = PlayingCard

  override type HandType = BlackJackHand

  override type PlayerType = BlackJackPlayer

  override val deck: Deck[CardType] = new Deck(PlayingCard.pack)

  def winRatioOnBlackJack: Float = 1.5f

  override protected def nextRound(number: Int): Round[BlackJack] = BlackJackRound(number, this)
}
