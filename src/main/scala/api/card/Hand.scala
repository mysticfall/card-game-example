package api.card

trait Hand[A <: Card] extends (Seq[A] => Boolean)