package api.fund

import api.fund.FundProvider.InsufficientFundException

trait InfiniteCashSource extends FundProvider {

  override def isBroke: Boolean = false

  override def receive(amount: Double): Unit = Unit

  @throws[InsufficientFundException]
  override def withdraw(amount: Double): Unit = Unit
}
