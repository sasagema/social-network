package models
import play.api.libs.json._


case class Friendship(
                     id: Option[Long],
                     userOneId: Long,
                     userTwoId: Long,
                     sent: Boolean,
                     accepted: Boolean,
                     blocked: Boolean
                     )
object Friendship {
  implicit val friendshipFormat = Json.format[Friendship]
  implicit val reads = Json.reads[Friendship]

}