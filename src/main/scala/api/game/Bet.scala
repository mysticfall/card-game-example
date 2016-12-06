package api.game

case class Bet[A <: Game[A]](gambler: A#GamblerType, amount: Double)
