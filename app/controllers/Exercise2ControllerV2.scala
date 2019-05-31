package controllers

import akka.stream.Materializer
import akka.util.ByteString
import javax.inject.Inject
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/** A version of the solution for Exercise 2 that uses
  * functions of type Request[A] => Future[Result]
  * instead of ActionBuilders.
  *
  * IMO this code is simpler than creating custom ActionBuilders.
  * However, we do end up with Futures and implicit Requests lying around
  * whether we need them in the action body or not.
  */
class Exercise2ControllerV2 @Inject() (
  val controllerComponents: ControllerComponents
)(implicit ec: ExecutionContext, mat: Materializer) extends BaseController {
  /** Log the URL and headers from the request */
  def loggedRequest: Action[AnyContent] =
    Action.async { implicit request =>
      withLogging {
        Future.successful(Ok("Yay"))
      }
    }

  /** Read a `Trace-Id` header and pass it on in the response. */
  def traced: Action[AnyContent] =
    Action.async { implicit request =>
      withTrace {
        Future.successful(Ok("Yay"))
      }
    }

  /** Read the Authorization header:
    * - if it's present, return a 200 containing the header value;
    * - if it's missing, return a 403.
    */
  def authenticated: Action[AnyContent] =
    Action.async { implicit request =>
      withAuth { auth =>
        Future.successful(Ok("Yay"))
      }
    }

  /** Log the status and body from the result */
  def loggedResult: Action[AnyContent] =
    Action.async { implicit request =>
      withLogging {
        Future.successful(Ok("Yay"))
      }
    }

  /** Log the request and response, trace, and authenticate in one go */
  def kitchenSink: Action[AnyContent] =
    Action.async { implicit request =>
      withLogging {
        withTrace {
          withAuth { (auth: String) =>
            Future.successful(Ok("Yay"))
          }
        }
      }
    }

  private def withLogging[A](result: => Future[Result])(implicit request: Request[A]): Future[Result] = {
    println(requestToString(request))
    result.flatMap { result =>
      result.body.consumeData.map { content =>
        println(resultToString(result, content))
        result
      }
    }
  }

  private def withTrace[A](result: => Future[Result])(implicit request: Request[A]): Future[Result] =
    request.headers.get("Trace-Id") match {
      case Some(id) => result.map(_.withHeaders("Trace-Id" -> id))
      case None     => result
    }

  private def withAuth[A](body: String => Future[Result])(implicit request: Request[A]): Future[Result] =
    request.headers.get("Authorization") match {
      case Some(auth) => body(auth)
      case None       => Future.successful(Unauthorized)
    }

  private def requestToString[A](request: Request[A]): String = {
    val part1 = request.method + " " + request.uri
    val part2 = request.headers.toSimpleMap
      .map { case (name, value) => s"$name: $value" }
      .mkString("\n")
    val part3 = request.body
    part1 + "\n" + part2 + "\n\n" + part3
  }

  private def resultToString(result: Result, content: ByteString): String = {
    val part1 = result.header.status + " " + result.header.reasonPhrase.getOrElse("")
    val part2 = result.header.headers
      .map { case (name, value) => s"$name: $value" }
      .mkString("\n")
    val part3 = content.decodeString("utf-8")
    part1 + "\n" + part2 + "\n\n" + part3
  }
}
