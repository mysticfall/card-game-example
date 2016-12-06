package api.game

trait Turn[A <: Game[A]] extends Iterator[Event[A]] {

  def game: A = round.game

  def round: Round[A]

  def dealer: A#DealerType = game.dealer

  def player: A#PlayerType
}
