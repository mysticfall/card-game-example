package api.game

trait Dealer[A <: Game[A]] extends Player[A] {

  override def name: String = "Dealer"

  def needsMoreCards(player: A#PlayerType, game: A): Boolean

  def deal(player: A#PlayerType, game: A): A#CardType

  override def toString: String = name
}
