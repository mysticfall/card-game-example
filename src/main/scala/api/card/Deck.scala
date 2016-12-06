package api.card

import scala.util.Random

class Deck[A <: Card](initialCards: Seq[A]) extends CardHolder[A] {

  receive(initialCards: _*)

  @throws[NoSuchElementException]
  def draw(): A = cards.toList match {
    case head :: tail =>
      replace(tail)
      head
    case Nil => throw new NoSuchElementException
  }

  def shuffle(): Seq[A] = {
    val result = Random.shuffle(cards)

    replace(result)

    result
  }
}
