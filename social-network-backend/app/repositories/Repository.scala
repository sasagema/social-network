
package repositories

import java.time.{LocalDate, LocalDateTime}

import javax.inject.{Inject, Singleton}
import models.{User, Friendship, LikeDislike, Post}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Repository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

    class UserTableDef(tag: Tag) extends Table[User](tag, _tableName = "USER") {
      def id = column[Long]("USER_ID", O.PrimaryKey, O.AutoInc)
      def firstName = column[String]("FIRST_NAME")
      def lastName = column[String]("LAST_NAME")
      def email = column[String]("EMAIL")
      def password = column[String]("PASSWORD")
      def info = column[String]("INFO")
      def * = (id.?, firstName, lastName, email, password, info) <> ((User.apply _).tupled, User.unapply)

    }

  val users = TableQuery[UserTableDef]
  val test = db.run(DBIO.seq(users.schema.createIfNotExists))
  println("user " + test.isCompleted)

  class PostTableDef(tag: Tag) extends Table[Post](tag, _tableName = "POST") {
    def id = column[Long]("POST_ID", O.PrimaryKey, O.AutoInc)
    def userId = column[Long]("USER_ID")
    def userFullName = column[String]("USER_FULL_NAME")
    def content = column[String]("CONTENT")
    def likesCount = column[Int]("LIKES_COUNT")
    def dislikesCount = column[Int]("DISLIKES_COUNT")
    def date = column[LocalDateTime]("DATE")
    def * = (id.?, userId, userFullName, content, likesCount, dislikesCount, date) <> ((Post.apply _).tupled, Post.unapply)
    def user = foreignKey("USER_FK", userId, users)(_.id)
  }
  val posts = TableQuery[PostTableDef]
  db.run(DBIO.seq(posts.schema.createIfNotExists))

  class LikeDislikeTableDef(tag: Tag) extends Table[LikeDislike](tag, _tableName = "LIKE_DISLIKE"){
    def id = column[Long]("LD_ID", O.PrimaryKey, O.AutoInc )
    def userId = column[Long]("USER_LIKE_ID")
    def postId = column[Long]("POST_LIKE_ID")
    def like = column[Boolean]("LIKE")
    def dislike = column[Boolean]("DISLIKE")
    def * = (id.?, userId, postId, like, dislike)<>((LikeDislike.apply _).tupled, LikeDislike.unapply)
    def user = foreignKey("USER_LIKE_FK", userId, users)(_.id)
    def post = foreignKey("POST_LIKE_FK", postId, posts)(_.id)
  }

  val likesDislikes = TableQuery[LikeDislikeTableDef]
  db.run(DBIO.seq(likesDislikes.schema.createIfNotExists))

  class FriendshipTableDef(tag: Tag) extends Table[Friendship](tag, _tableName = "FRIENDSHIP"){
    def id = column[Long]("FRENSHIP_ID", O.PrimaryKey, O.AutoInc )
    def userOneId = column[Long]("USER_ONE_ID")
    def userTwoId = column[Long]("USER_TWO_ID")
    def status = column[String]("STATUS")
    def sent = column[Boolean]("SENT")
    def accepted = column[Boolean]("ACCEPTED")
    def blocked = column[Boolean]("BLOCKED")
    def * = (id.?, userOneId, userTwoId, sent, accepted, blocked)<>((Friendship.apply _).tupled, Friendship.unapply)
    def userOne = foreignKey("USER_ONE_FK", userOneId, users)(_.id)
    def userTwo = foreignKey("USER_TWO_FK", userTwoId, users)(_.id)
  }

  val friendships = TableQuery[FriendshipTableDef]
  db.run(DBIO.seq(friendships.schema.createIfNotExists))


  //Schema
  val schema = users.schema ++ posts.schema ++ likesDislikes.schema ++ friendships.schema
  //db.run(DBIO.seq(schema.createIfNotExists))

  def createSchema() = db.run{
    DBIO.seq(schema.createIfNotExists)
  }
  def dropSchema() = db.run{
    DBIO.seq(schema.dropIfExists)
  }
  //USER
  def listUsers(): Future[Seq[User]] = db.run {
    users.result
  }

/*  def createUser(){}
  def updateUser(){}
  def deleteUser(){}
  def findUserByEmail(){}
  def findUserById(){}*/


  //POST
  def listPosts(): Future[Seq[Post]] = db.run {
    posts.result
  }
/*  def createPost(){}
  def updatePost(){}
  def deletePost(){}
  def findPostById(){}*/

  //FRIENDSHIP
  def listFriendships(userId: Long): Future[Seq[Friendship]] = db.run {
    friendships.filter(f => ((f.userOneId === userId || f.userTwoId === userId) && f.accepted === true)).result
  }
  /*def createFriendship(){}
  def updateFriendship(){}
  def deleteFriendship(){}
  def findFriendshipByUserOneIdAndUserTwoId(){}*/
  def listUserFriends(userId: Long): Future[Seq[User]] = {
    db.run(users.result)
  }
  def listFriendsPosts(userId: Long): Future[Seq[Post]] = {
    db.run(posts.result)
  }

  def listUsersPosts(userId: Long): Future[Seq[Post]] = {
    db.run(
      posts.filter(post => (post.userId === userId)).result
    )
  }
  //LIKE DISLIKE
/*  def createLikeDislike(){}
  def deleteLikeDislike(){}*/
}

