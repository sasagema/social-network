package repositories

import javax.inject.Inject
import models.LikeDislike

import scala.concurrent.{ExecutionContext, Future}

class LikesDislikesRepository @Inject()(val repo: Repository)(implicit ec: ExecutionContext) {

  import repo.dbConfig._
  import profile.api._

  val likes = repo.likesDislikes

  def insert(like: LikeDislike) = db
    .run(likes returning likes.map(_.id) += like)
    .map(id => like.copy(id = Some(id)))

  def findLike(userId: Long, postId: Long): Future[Option[LikeDislike]] = db.run {
    likes.filter(f => (f.userId === userId && f.postId === postId && f.like === true)).result.headOption
  }
  def findUsersLikedPosts(userId:  Long): Future[Seq[LikeDislike]] = db.run {
    likes.filter(f => f.userId === userId).result
  }

  def delete(id: Long) = db.run(likes.filter(_.id === id).delete) map {_ > 0}

}
