package DTOs

import play.api.libs.json.Json

case class LoginDto(
                   email: String,
                   password: String
                   )
object LoginDto {
  implicit val loginFormat = Json.format[LoginDto]
  implicit val reads = Json.reads[LoginDto]
  implicit val writes = Json.writes[LoginDto]

}
