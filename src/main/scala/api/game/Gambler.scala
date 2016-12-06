package api.game

import api.fund.CashHolder

trait Gambler[A <: Game[A]] extends Player[A] with CashHolder {

  def bet(game: A): Double
}
