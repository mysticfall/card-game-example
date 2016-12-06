package api.card

trait Card {

  def isOpen: Boolean = opened

  def open(): this.type = {
    this.opened = true
    this
  }

  def fold(): this.type = {
    this.opened = false
    this
  }

  def flip(): this.type = {
    this.opened = !opened
    this
  }

  def name: String

  override def toString: String = if (isOpen) name else "[Hidden]"

  private var opened: Boolean = true
}

