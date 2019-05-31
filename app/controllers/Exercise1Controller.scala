package controllers

import javax.inject.Inject
import play.api.mvc._

import scala.concurrent.ExecutionContext

class Exercise1Controller @Inject() (
  val controllerComponents: ControllerComponents
)(implicit ec: ExecutionContext) extends BaseController {
  /** A plain action. Nothing to do here. */
  def plain =
    Action {
      Ok("TODO: Complete")
    }

  /** Turn this into an asynchronous action */
  def async =
    Action {
      Ok("TODO: Complete")
    }

  /** Return JSON to the user */
  def json =
    Action {
      Ok("TODO: Complete")
    }

  /** Read JSON from the request body and echo it back to the user */
  def jsonEcho =
    Action {
      Ok("TODO: Complete")
    }

  /** Read JSON from the request body and return it to the user asynchronously */
  def jsonAsync =
    Action {
      Ok("TODO: Complete")
    }
}
