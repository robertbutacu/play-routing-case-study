package controllers

import play.api.libs.json.Json

case class Model(name: String, surname: String)

object Model {
  implicit val format = Json.format[Model]
}
