package repositories

import javax.inject.Inject
import models.Friendship

import scala.concurrent.{ExecutionContext, Future}

class FriendshipsRepository @Inject()(val repo: Repository) (implicit ec: ExecutionContext) {

  import repo.dbConfig._
  import profile.api._

  val friendships = repo.friendships

  def list() = db.run(
    friendships.result
  )
  def insert(friendship: Friendship) = db
    .run(friendships returning friendships.map(_.id) += friendship)
    .map(id => friendship.copy(id = Some(id)))


  def delete(id: Long) = db.run(friendships.filter(_.id === id).delete) map {_ > 0}

  def userFriendships(userId: Long): Future[Seq[Friendship]] = db.run {
    friendships.filter(f => ((f.userOneId === userId || f.userTwoId === userId) && f.accepted === true)).result
  }

  def findFriendship(userOneId: Long, userTwoId: Long): Future[Option[Friendship]] = db.run {
    friendships.filter(f => (f.userOneId === userOneId && f.userTwoId === userTwoId)).result.headOption
  }
}

