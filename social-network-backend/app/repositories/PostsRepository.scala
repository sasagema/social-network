package repositories

import javax.inject.Inject
import models.Post

import scala.concurrent.{ExecutionContext, Future}

class PostsRepository @Inject()(val repo: Repository)(implicit ec: ExecutionContext) {

  import repo.dbConfig._
  import profile.api._

  val posts = repo.posts

  def list() = db.run(
    posts.result
  )
  def insert(post: Post) = db
    .run(posts returning posts.map(_.id) += post)
    .map(id => post.copy(id = Some(id)))

  def updatePost(post: Post) = db.run{
    val q = for { p <- posts if p.id === post.id } yield p.content
    q.update(post.content)

  }

  def addLike(postId: Long): Unit = {
    val likesQuery = db.run( posts.filter(_.id === postId).map(p => p.likesCount).result.headOption)
     likesQuery.map{l =>
      val count = l.get + 1
      db.run( posts.filter(_.id === postId).map(p => p.likesCount).update(count))
     }
  }
  def removeLike(postId: Long): Unit = {
    val likesQuery = db.run( posts.filter(_.id === postId).map(p => p.likesCount).result.headOption)
    likesQuery.map{l =>
      val count = l.get - 1
      if(count >= 0){
        db.run( posts.filter(_.id === postId).map(p => p.likesCount).update(count))
      }
    }
  }

  def delete(id: Long) = db.run(posts.filter(_.id === id).delete) map {_ > 0}

  def findPostsByUserId(userId: Long): Future[Seq[Post]] = {
    db.run(
      posts.filter(post => (post.userId === userId)).result
    )
  }
  def findPostsById(id: Long): Future[Option[Post]] = {
    db.run(
      posts.filter(post => (post.id === id)).result.headOption
    )
  }

  def findFriendsPosts(userId: Long): Future[Seq[Post]] = {

    db.run{
      val friendsIds = repo.friendships.filter(_.userOneId === userId).map(_.userTwoId)
      val query = posts.filter(_.userId in friendsIds) ++ posts.filter(_.userId === userId)
      query.result
    }
  }
  def findLikedPosts(userId: Long): Future[Seq[Post]] = {
    db.run{
      val postsIds = repo.likesDislikes.filter(_.userId === userId).map(_.postId)
      val query = posts.filter(_.id in postsIds)
      query.result
    }
  }
  def findLikedPostsIds(userId: Long): Future[Seq[Long]] = {
    db.run {
      val postsIds = repo.likesDislikes.filter(_.userId === userId).map(_.postId)
      postsIds.result
    }
  }

}
