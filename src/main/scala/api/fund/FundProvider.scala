package api.fund

import api.fund.FundProvider.InsufficientFundException

trait FundProvider {

  def isBroke: Boolean

  def receive(amount: Double): Unit

  @throws[InsufficientFundException]
  def withdraw(amount: Double): Unit

  @throws[InsufficientFundException]
  def transfer(amount: Double, receiver: FundProvider) {
    withdraw(amount)
    receiver.receive(amount)
  }
}

object FundProvider {

  class InsufficientFundException(message: String) extends Exception(message)
}
