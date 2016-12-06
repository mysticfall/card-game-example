package api.card

trait CardHolder[A <: Card] {

  def cards: Seq[A] = cardList

  def receive(cards: A*): Seq[A] = {
    this.cardList ++= cards
    cards
  }

  def replace(cards: Seq[A]) {
    this.cardList = cards
  }

  def dropAll(): Seq[A] = {
    val removed = cards

    this.cardList = Nil

    removed
  }

  private var cardList: Seq[A] = Nil
}