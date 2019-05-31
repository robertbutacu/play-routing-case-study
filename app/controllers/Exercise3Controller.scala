package controllers

import akka.util.ByteString
import javax.inject.Inject
import play.api.http.Writeable
import play.api.mvc._

import scala.concurrent.ExecutionContext

class Exercise3Controller @Inject() (
  val controllerComponents: ControllerComponents
)(implicit ec: ExecutionContext) extends BaseController {
  val intListBodyParser: BodyParser[List[Int]] =
    parse.tolerantText.validate { str =>
      util.Try(str.split(" ").toList.map(_.toInt))
        .fold(_ => Left(BadRequest("Could not parse request")), numbers => Right(numbers))
    }

  implicit val intListWriteable: Writeable[List[Int]] =
    Writeable(
      numbers => ByteString(numbers.mkString(" ")),
      Some("text/numbers")
    )

  /** Assume the user will post a list of numbers of the form "1 2 3 etc...".
    * - Use a custom BodyParser to parse this as a List[Int].
    * - Add the numbers and return the result.
    */
  def readNumbers =
    Action(intListBodyParser) { request =>
      Ok(request.body.sum)
    }

  /** Assume the user will post a list of numbers of the form "1 2 3 etc...".
    * - Use a custom BodyParser to parse this as a List[Int].
    * - Use a custom Writeable to return the numbers to the user.
    */
  def echoNumbers =
    Action(intListBodyParser) { request =>
      Ok(request.body)
    }
}
