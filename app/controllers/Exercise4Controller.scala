package controllers

import java.time.LocalDate

import javax.inject.Inject
import play.api.mvc._

import scala.util.Try

case class UserId(value: Long)

object UserId {
  implicit val pathBindable: PathBindable[UserId] = new PathBindable[UserId] {
    override def bind(key: String, value: String): Either[String, UserId] = {
      Try{
        value.toInt
      }.toEither
        .left.map(_ => "Unable to bind")
        .right.map(i => UserId(i))
    }

    override def unbind(key: String, value: UserId): String = {
      value.value.toString
    }
  }
}

class Exercise4Controller @Inject() (
  val controllerComponents: ControllerComponents
) extends BaseController {
  /** Example: add two integers */
  def add(a: Int, b: Int) =
    Action {
      val total = a + b
      Ok(total.toString)
    }

  /** Return the number of days between `from` and `to` */
  def interval(from: LocalDate, to: LocalDate) =
    Action {
      Ok("TODO: Complete")
    }

  def testAction(userId: UserId) = Action {
    Ok("test Action")
  }
}
