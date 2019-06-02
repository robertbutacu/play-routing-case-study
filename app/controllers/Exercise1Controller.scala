package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.Future

class Exercise1Controller @Inject() (
  val controllerComponents: ControllerComponents
) extends BaseController {
  /** A plain action. Nothing to do here. */
  def plain =
    Action {
      Ok("TODO: Complete")
    }

  /** Read the headers from the request and return them to the user. */
  def headers =
    Action { request: Request[AnyContent] =>
      Ok(Json.toJson(request.headers.headers))
    }

  /** Turn this into an asynchronous action */
  def async =
    Action.async {
      Future.successful(Ok("TODO: Complete"))
    }

  /** Return JSON to the user */
  def json =
    Action {
      Ok(Json.toJson(Model("", "")))
    }

  /** Read JSON from the request body and echo it back to the user */
  def jsonEcho =
    Action(parse.json[Model]) { request =>
      Ok(Json.toJson(request.body))
    }

  /** Read JSON from the request body and return it to the user asynchronously */
  def jsonAsync =
    Action.async(parse.json) { request =>
      Future.successful(Ok(request.body))
    }
}
