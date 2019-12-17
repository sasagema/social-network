package models

import java.time.{LocalDate, LocalDateTime}
import play.api.libs.json._


case class Post(
                 id: Option[Long],
                 userId: Long,
                 userFullName: String,
                 content: String,
                 likesCount: Int = 0,
                 dislikesCount: Int = 0,
                 date: LocalDateTime
               )
object Post {
  implicit val postFormat = Json.format[Post]
}