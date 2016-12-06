package api.game

import api.game.Event.{ TurnEndedEvent, TurnStartedEvent }

trait Round[A <: Game[A]] extends Iterator[Turn[A]] {

  def game: A

  def number: Int

  override def next(): Turn[A] = {
    val turn = nextTurn()

    game publish {
      TurnStartedEvent(turn)
    }

    turn foreach {
      game.publish
    }

    game publish {
      TurnEndedEvent(turn)
    }

    turn
  }

  protected def nextTurn(): Turn[A]
}
