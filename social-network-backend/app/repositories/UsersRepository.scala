package repositories

import javax.inject.Inject
import models.User

import scala.concurrent.{ExecutionContext, Future}

class UsersRepository @Inject()(val repo: Repository) (implicit ec: ExecutionContext) {

  import repo.dbConfig._
  import profile.api._

  val users = repo.users

  def list() = db.run(
    users.result
  )
  def insert(user: User) = db
    .run(users returning users.map(_.id) += user)
    .map(id => user.copy(id = Some(id)))


  def delete(id: Long) = db.run(users.filter(_.id === id).delete) map {_ > 0}

  def findByEmail(email: String): Future[Option[User]] = db.run {
    users.filter(user => user.email === email).result.headOption
  }
  //option
  def findById(id: Long): Future[Option[User]] = db.run {
    users.filter(user => user.id === id).result.headOption
  }
  def findUsersFriendsById(id: Long): Future[Seq[User]] = db.run {
    val friendsIds = repo.friendships.filter(_.userOneId === id).map(_.userTwoId)
    users.filter(_.id in friendsIds).result
  }
  def findFriendsIds(id: Long): Future[Seq[Long]] = {
    db.run {
      val friendsIds = repo.friendships.filter(_.userOneId === id).map(_.userTwoId)
      friendsIds.result
    }
  }
}
