package models

import play.api.libs.json.Json

case class LikeDislike(
                      id: Option[Long],
                      userId: Long,
                      postId: Long,
                      like: Boolean,
                      dislike: Boolean
                      )
object LikeDislike {
  implicit val likeDislikeFormat = Json.format[LikeDislike]
  implicit val reads = Json.reads[LikeDislike]

}