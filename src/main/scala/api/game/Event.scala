package api.game

trait Event[A <: Game[A]] {

  def game: A
}

object Event {

  trait GameEvent[A <: Game[A]] extends Event[A]

  case class GameStartedEvent[A <: Game[A]](game: A) extends GameEvent[A]

  case class GameEndedEvent[A <: Game[A]](game: A) extends GameEvent[A]

  trait RoundEvent[A <: Game[A]] extends Event[A] {

    def round: Round[A]

    override def game: A = round.game
  }

  case class RoundStartedEvent[A <: Game[A]](round: Round[A]) extends RoundEvent[A]

  case class RoundEndedEvent[A <: Game[A]](round: Round[A]) extends RoundEvent[A]

  trait TurnEvent[A <: Game[A]] extends Event[A] {

    def turn: Turn[A]

    override def game: A = turn.game
  }

  case class TurnStartedEvent[A <: Game[A]](turn: Turn[A]) extends TurnEvent[A]

  case class TurnEndedEvent[A <: Game[A]](turn: Turn[A]) extends TurnEvent[A]

  case class CardDrawnEvent[A <: Game[A]](player: A#PlayerType, card: A#CardType, game: A) extends Event[A]

  case class BetPlacedEvent[A <: Game[A]](bet: Bet[A], game: A) extends Event[A]

  trait RoundOutcomeEvent[A <: Game[A]] extends RoundEvent[A] {

    def gambler: A#GamblerType
  }

  case class DrawEvent[A <: Game[A]](gambler: A#GamblerType, round: Round[A]) extends RoundOutcomeEvent[A]

  case class WonEvent[A <: Game[A]](gambler: A#GamblerType, amount: Double, round: Round[A]) extends RoundOutcomeEvent[A]

  case class LostEvent[A <: Game[A]](gambler: A#GamblerType, amount: Double, round: Round[A]) extends RoundOutcomeEvent[A]
}
