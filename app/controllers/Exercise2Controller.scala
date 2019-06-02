package controllers

import javax.inject.Inject
import play.api.Logging
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class Exercise2Controller @Inject()(
                                     val controllerComponents: ControllerComponents,
                                     loggerAction: LoggingAction,
                                     composedActions: ComposedActions,
                                   ) extends BaseController {
  /** Log the URL and headers from the request */
  def loggedRequest =
    loggerAction {
      Ok("TODO: Complete")
    }

  /** Read a `Trace-Id` header and pass it on in the response. */
  def traced =
    loggerAction { request =>
      Ok(request.headers.get("Trace-Id").get)
    }

  /** Read the Authorization header:
    * - if it's present, return a 200 containing the header value;
    * - if it's missing, return a 403.
    */
  def authenticated = composedActions.authorizedAction {
    request =>
      Ok(request.authorization)
  }

  /** Log the status and body from the result */
  def loggedResult =
    Action { request =>
      Ok("TODO: Complete")
    }

  /** Log the request and response, trace, and authenticate in one go */
  def kitchenSink =
    Action {
      Ok("Yay")
    }
}

class LoggingAction @Inject()(parser: BodyParsers.Default)(implicit ec: ExecutionContext)
  extends ActionBuilderImpl(parser)
    with Logging {
  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    logger.info("Logging action")
    super.invokeBlock(request, block)
  }
}

class ComposedActions @Inject()(val controllerComponents: ControllerComponents,
                                authorizedFilter: AuthorizedAction) extends BaseController {
  def authorizedAction: ActionBuilder[AuthorizedRequest, AnyContent] = Action andThen authorizedFilter
}

case class AuthorizedRequest[A](authorization: String, request: Request[A]) extends WrappedRequest[A](request)

class AuthorizedAction @Inject()(val controllerComponents: ControllerComponents)(implicit val executionContext: ExecutionContext)
  extends ActionRefiner[Request, AuthorizedRequest]
    with BaseController {
  override protected def refine[A](request: Request[A]): Future[Either[Result, AuthorizedRequest[A]]] = {
    request.headers.get("Authorized") match {
      case None    => Future.successful(Left(Forbidden))
      case Some(a) => Future.successful(Right(AuthorizedRequest(a, request)))
    }
  }
}

class LoggedResult @Inject()(parser: BodyParsers.Default)(implicit ec: ExecutionContext)
  extends ActionBuilderImpl(parser)
    with Logging {
  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    super.invokeBlock(request, block)
      .map {
        r =>
          logger.info(r.header.status.toString)
         // r.body.consumeData.foreach(bs => logger.info(bs.toString()))
          r
      }
  }
}