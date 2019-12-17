package models


import play.api.libs.json._


case class User(
                  id: Option[Long],
                  firstName: String,
                  lastName:String,
                  email: String,
                  password: String,
                  info: String
               )
object User {
  implicit val userFormat = Json.format[User]
  implicit val reads = Json.reads[User]

}
