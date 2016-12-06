package api.card

trait EvaluationStrategy[A <: Card] {

  def valueOf(card: A): Int
}

object EvaluationStrategy {

  implicit class Evaluatable[A <: Card : EvaluationStrategy](card: A) {

    def value: Int = implicitly[EvaluationStrategy[A]].valueOf(card)
  }
}