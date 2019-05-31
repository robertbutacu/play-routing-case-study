package controllers

import java.time.{Duration, LocalDate}

import javax.inject.Inject
import play.api.mvc._

import scala.util.matching.Regex

class Exercise4Controller @Inject() (
  val controllerComponents: ControllerComponents
) extends BaseController {
  /** Example: add two integers */
  def add(a: Int, b: Int): Action[AnyContent] =
    Action {
      val total = a + b
      Ok(total.toString)
    }

  /** Return the number of days between `from` and `to` */
  def interval(from: LocalDate, to: LocalDate): Action[AnyContent] =
    Action {
      Ok((to.toEpochDay - from.toEpochDay) + " days")
    }
}

object Binders {
  implicit val localDatePathBindable: PathBindable[LocalDate] =
    new PathBindable[LocalDate] {
      val LocalDateRegex: Regex =
        "^([0-9]+)-([0-9]+)-([0-9]+)$".r

      override def bind(key: String, value: String): Either[String, LocalDate] = {
        value match {
          case LocalDateRegex(yyyy, mm, dd) =>
            Right(LocalDate.of(yyyy.toInt, mm.toInt, dd.toInt))

          case other =>
            Left("Expected LocalDate, found " + other)
        }
      }

      override def unbind(key: String, value: LocalDate): String =
        s"${value.getYear}-${value.getMonth}-${value.getDayOfMonth}"
    }

  implicit val localDateQueryStringBindable: QueryStringBindable[LocalDate] =
    new QueryStringBindable[LocalDate] {
      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, LocalDate]] =
        params.get(key).flatMap(_.headOption).map(localDatePathBindable.bind(key, _))

      override def unbind(key: String, value: LocalDate): String =
        s"$key=${localDatePathBindable.unbind(key, value)}"
    }
}