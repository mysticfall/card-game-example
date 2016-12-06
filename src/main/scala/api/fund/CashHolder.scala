package api.fund

import api.fund.FundProvider.InsufficientFundException

trait CashHolder extends FundProvider {

  def cash: Double = total

  def isBroke: Boolean = cash <= 0

  override def withdraw(amount: Double) {
    require(amount > 0, "Amount should be a positive value.")

    if (amount > cash) {
      throw new InsufficientFundException(
        s"$this does not have $amount in cash.")
    }

    this.total -= amount
  }

  def withdrawAll(): Double = {
    val amount = cash

    withdraw(amount)

    amount
  }

  override def receive(amount: Double) {
    require(amount > 0, "Amount should be a positive value.")

    this.total += amount
  }

  private var total: Double = 0
}
