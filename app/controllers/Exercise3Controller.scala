package controllers

import javax.inject.Inject
import play.api.mvc._

import scala.concurrent.ExecutionContext

class Exercise3Controller @Inject() (
  val controllerComponents: ControllerComponents
)(implicit ec: ExecutionContext) extends BaseController {
  /** Assume the user will post a list of numbers of the form "1 2 3 etc...".
    * - Use a custom BodyParser to parse this as a List[Int].
    * - Add the numbers and return the result.
    */
  def readNumbers =
    Action {
      Ok("TODO: Complete")
    }

  /** Assume the user will post a list of numbers of the form "1 2 3 etc...".
    * - Use a custom BodyParser to parse this as a List[Int].
    * - Use a custom Writeable to return the numbers to the user.
    */
  def echoNumbers =
    Action {
      Ok("TODO: Complete")
    }
}
