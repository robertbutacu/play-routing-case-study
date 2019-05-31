package controllers

import java.time.LocalDate

import javax.inject.Inject
import play.api.mvc._

class Exercise4Controller @Inject() (
  val controllerComponents: ControllerComponents
) extends BaseController {
  /** Example: add two integers */
  def add(a: Int, b: Int) =
    Action {
      val total = a + b
      Ok(total.toString)
    }

  /** Return the number of days between `from` and `to` */
  def interval(from: LocalDate, to: LocalDate) =
    Action {
      Ok("TODO: Complete")
    }
}
