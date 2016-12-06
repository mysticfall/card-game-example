package api.game

import api.card.{ Card, Deck, Hand }
import api.game.Event._

trait Game[A <: Game[A]] extends Iterator[Round[A]] with EventHandler[A] {
  this: A =>

  type CardType <: Card

  type HandType <: Hand[A#CardType]

  type PlayerType <: Player[A]

  type GamblerType = A#PlayerType with Gambler[A]

  type DealerType = A#PlayerType with Dealer[A]

  val deck: Deck[A#CardType]

  val dealer: A#DealerType

  val gamblers: Seq[A#GamblerType]

  def players: Seq[A#PlayerType] = gamblers :+ dealer

  def bets: Seq[Bet[A]] = currentBets

  def round: Int = roundNumber

  def play() {
    publish {
      GameStartedEvent(this)
    }

    while (hasNext) next()

    publish {
      GameEndedEvent(this)
    }
  }

  override def hasNext: Boolean = !dealer.isBroke && !gamblers.forall(_.isBroke)

  override def next(): Round[A] = {
    this.currentBets = Nil

    deck.receive(players.flatMap(_.dropAll()): _*)
    deck.shuffle()

    val round = nextRound(roundNumber)

    publish {
      RoundStartedEvent(round)
    }

    while (round.hasNext) round.next()

    publish {
      RoundEndedEvent(round)
    }

    deck.receive(players.flatMap(_.dropAll()): _*)

    this.roundNumber += 1
    this.currentBets = Nil

    round
  }

  protected def nextRound(number: Int): Round[A]

  override def publish(event: Event[A]) {
    event match {
      case BetPlacedEvent(bet, _) =>
        this.currentBets +:= bet
      case e: RoundOutcomeEvent[A] =>
        this.currentBets = currentBets.filterNot(_.gambler == e.gambler)
      case _ =>
    }

    super.publish(event)
  }

  private var roundNumber: Int = 1

  private var currentBets: Seq[Bet[A]] = Nil
}
