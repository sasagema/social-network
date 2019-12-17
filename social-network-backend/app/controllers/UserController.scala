package controllers

import repositories.{FriendshipsRepository, Repository, UsersRepository}
import javax.inject.{Inject, Singleton}
import models.{Friendship, User}
import play.api.libs.json.{JsPath, Json, Reads}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject() (cc: MessagesControllerComponents,
                                repo: UsersRepository,
                                friendshipRepo: FriendshipsRepository
                               )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {


  def getUsers = Action.async { implicit request =>
    repo.list.map { users =>
      Ok(Json.toJson(users))
    }
  }

  def getUsers2 = Action.async { implicit request =>
    repo.list().map { users =>
      Ok(Json.toJson(users))
    }
  }
  def getUser(id: Long) = Action.async{ implicit  request =>
    repo.findById(id).map{ user =>
      Ok(Json.toJson(user))
    }
  }
  def getUsersFriends(id: Long) = Action.async { implicit  request =>
    repo.findUsersFriendsById(id).map { users =>
      Ok(Json.toJson(users))
    }
  }
  def getUsersFriendsIds(id: Long) = Action.async { implicit  request =>
    repo.findFriendsIds(id).map { users =>
      Ok(Json.toJson(users))
    }
  }

  def addFriend() = Action.async(parse.json) {  request =>

    val friendship = request.body.as[Friendship]
    val newFriendship = friendshipRepo.insert(friendship)

    newFriendship.map { nf =>
      if (nf.id.nonEmpty) {

        Ok("Friend added.")
      }else {
        BadRequest("Error")
      }
    }
  }
    def removeFriend() = Action.async(parse.json) {  request =>
      val friendship = request.body.as[Friendship]
      val f = friendshipRepo.findFriendship(friendship.userOneId, friendship.userTwoId)
      f.map { ff =>
        if (ff.get.id.nonEmpty) {
         friendshipRepo.delete(ff.get.id.get)
          Ok("Friend removed.")
        }else {
          BadRequest("Error")
        }
      }
  }
}
