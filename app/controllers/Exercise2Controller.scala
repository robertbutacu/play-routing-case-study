package controllers

import akka.stream.Materializer
import akka.util.ByteString
import javax.inject.Inject
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class Exercise2Controller @Inject() (
  val loggedAction1: LoggedAction1[AnyContent],
  val loggedAction2: LoggedAction2[AnyContent],
  val tracedAction: TracedAction[AnyContent],
  val authenticatedAction: AuthenticatedAction[AnyContent],
  val controllerComponents: ControllerComponents
) extends BaseController {
  /** Log the URL and headers from the request */
  def loggedRequest: Action[AnyContent] =
    loggedAction1 {
      Ok("Yay")
    }

  /** Read a `Trace-Id` header and pass it on in the response. */
  def traced: Action[AnyContent] =
    tracedAction {
      Ok("Yay")
    }

  /** Read the Authorization header:
    * - if it's present, return a 200 containing the header value;
    * - if it's missing, return a 403.
    */
  def authenticated: Action[AnyContent] =
    authenticatedAction {
      Ok("Yay")
    }

  /** Log the status and body from the result */
  def loggedResult: Action[AnyContent] =
    loggedAction2 {
      Ok("Yay")
    }

  val kitchenSinkAction: ActionBuilder[AuthenticatedRequest, AnyContent] =
    loggedAction2.andThen(tracedAction).andThen(authenticatedAction)

  /** Log the request and response, trace, and authenticate in one go */
  def kitchenSink: Action[AnyContent] =
    kitchenSinkAction {
      Ok("Yay")
    }
}

class LoggedAction1[B] @Inject() (parser: BodyParser[B])(implicit ec: ExecutionContext, mat: Materializer) extends ActionBuilderImpl[B](parser) {
  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    println(requestToString(request))
    block(request)
  }

  private def requestToString[A](request: Request[A]): String = {
    val part1 = request.method + " " + request.uri
    val part2 = request.headers.toSimpleMap
      .map { case (name, value) => s"$name: $value" }
      .mkString("\n")
    val part3 = request.body
    part1 + "\n" + part2 + "\n\n" + part3
  }
}

class TracedAction[B] @Inject() (parser: BodyParser[B])(implicit ec: ExecutionContext, mat: Materializer) extends ActionBuilderImpl[B](parser) {
  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    request.headers.get("Trace-Id") match {
      case Some(traceId) =>
        block(request).map(result => result.withHeaders("Trace-Id" -> traceId))
      case None =>
        block(request)
    }
  }
}

case class AuthenticatedRequest[B](request: Request[B], authHeader: String)

class AuthenticatedAction[B](val parser: BodyParser[B])(implicit val executionContext: ExecutionContext) extends ActionBuilder[AuthenticatedRequest, B] with Results {
  override def invokeBlock[A](request: Request[A], block: AuthenticatedRequest[A] => Future[Result]): Future[Result] =
    request.headers.get("Authorization") match {
      case Some(auth) => block(request.map(body => (auth, body)))
      case None       => Future.successful(Unauthorized)
    }
}

class LoggedAction2[B] @Inject() (parser: BodyParser[B])(implicit ec: ExecutionContext, mat: Materializer) extends ActionBuilderImpl[B](parser) {
  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    println(requestToString(request))

    block(request).flatMap { result =>
      result.body.consumeData.map { content =>
        println(resultToString(result, content))
        result
      }
    }
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
