package api.game.blackjack

import api.game.blackjack.BlackJackRound._
import api.game.{ Round, Turn }

case class BlackJackRound(number: Int, game: BlackJack) extends Round[BlackJack] {

  override def hasNext: Boolean = phase match {
    case _: Settling | _: Ending => game.bets.nonEmpty
    case _ => true
  }

  override protected def nextTurn(): Turn[BlackJack] = process()

  private def process(): Turn[BlackJack] = {
    phase match {
      case Initial =>
        this.phase = Betting(game.gamblers.toList)

        process()
      case Betting(gambler :: remaining) =>
        this.phase = Betting(remaining)

        BettingTurn(gambler, this)
      case Betting(Nil) =>
        this.phase = Dealing(game.players.toList)

        process()
      case Dealing(player :: remaining) =>
        this.phase = Dealing(remaining)

        DealingTurn(player, this)
      case Dealing(Nil) if game.players.forall(_.cards.size == 2) =>
        this.phase = Settling(game.gamblers.toList)

        game.dealer.cards.foreach(_.open())

        process()
      case Dealing(Nil) =>
        this.phase = Dealing(game.players.toList)

        process()
      case Settling(player :: remaining) =>
        this.phase = Settling(remaining)

        SettlingTurn(player, this)
      case Settling(Nil) =>
        this.phase = Playing(game.bets.map(_.gambler).toList :+ game.dealer)

        process()
      case Playing(player :: remaining) =>
        this.phase = Playing(remaining)

        DealingTurn(player, this)
      case Playing(Nil) =>
        this.phase = Ending(game.bets.map(_.gambler).toList)

        process()
      case Ending(player :: remaining) =>
        this.phase = Ending(remaining)

        EndingTurn(player, this)
      case _ =>
        throw new IllegalStateException(s"Illegal phase encountered: '$phase'.")
    }
  }

  private var phase: Phase = Initial
}

object BlackJackRound {

  sealed trait Phase

  object Initial extends Phase

  case class Betting(remaining: List[BlackJack#GamblerType]) extends Phase

  case class Dealing(remaining: List[BlackJackPlayer]) extends Phase

  case class Settling(remaining: List[BlackJack#GamblerType]) extends Phase

  case class Playing(remaining: List[BlackJackPlayer]) extends Phase

  case class Ending(remaining: List[BlackJack#GamblerType]) extends Phase
}
