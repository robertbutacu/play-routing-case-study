package controllers

import javax.inject.Inject
import play.api.mvc._

import scala.concurrent.ExecutionContext

class Exercise2Controller @Inject() (
  val controllerComponents: ControllerComponents
)(implicit ec: ExecutionContext) extends BaseController {
  /** Log the URL and headers from the request */
  def loggedRequest =
    Action {
      Ok("TODO: Complete")
    }

  /** Read the Authorization header:
    * - if it's present, return a 200 containing the header value;
    * - if it's missing, return a 403.
    */
  def authenticated =
    Action {
      Ok("TODO: Complete")
    }

  /** Log the status and body from the result */
  def loggedResult =
    Action {
      Ok("TODO: Complete")
    }

  /** Read a `Trace-Id` header and pass it on in the response. */
  def traced =
    Action {
      Ok("TODO: Complete")
    }
}
