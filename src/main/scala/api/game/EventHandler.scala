package api.game

trait EventHandler[A <: Game[A]] {

  def publish(event: Event[A]) {
    if (onEvent.isDefinedAt(event)) onEvent(event)
  }

  protected def onEvent: PartialFunction[Event[A], Unit] = {
    case _ => // No-Op.
  }
}
