package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class Exercise1Controller @Inject() (
  val controllerComponents: ControllerComponents
)(implicit ec: ExecutionContext) extends BaseController {
  /** A plain action. Nothing to do here. */
  def plain =
    Action {
      Ok("Yay")
    }

  /** Read the headers from the request and return them to the user. */
  def headers =
    Action { request =>
      Ok(request.headers.toMap.toString)
    }

  /** Turn this into an asynchronous action */
  def async =
    Action.async {
      Future.successful(Ok("Yay"))
    }

  /** Return JSON to the user */
  def json =
    Action {
      Ok(Json.obj("foo" -> "bar"))
    }

  /** Read JSON from the request body and echo it back to the user */
  def jsonEcho =
    Action(parse.json) { request =>
      Ok(request.body)
    }

  /** Read JSON from the request body and return it to the user asynchronously */
  def jsonAsync =
    Action.async(parse.json) { request =>
      Future.successful(Ok(request.body))
    }
}
