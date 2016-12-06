package api.card

trait HandManager[A <: Card, B <: Hand[A]] extends (Seq[A] => Option[B]) with Ordering[B] {

  def allHands: Seq[B]

  override def apply(cards: Seq[A]): Option[B] = allHands.find(_.apply(cards))

  override def compare(a: B, b: B): Int = allHands.indexOf(a) compareTo allHands.indexOf(b)
}
