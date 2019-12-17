package controllers

import repositories.{LikesDislikesRepository, PostsRepository, Repository, UsersRepository}
import javax.inject.Inject
import models.{LikeDislike, Post}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class PostController  @Inject()(cc: MessagesControllerComponents,
                                postRepo: PostsRepository,
                                userRepo: UsersRepository,
                                likeRepo: LikesDislikesRepository
                               )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def addPost: Action[JsValue] = Action.async(parse.json){ implicit request =>
    val post = request.body.as[Post]
    val newPost = postRepo.insert(post)
    newPost.map(np =>
    Ok(Json.toJson(np)))
  }

  def likePost() = Action.async(parse.json) {  request =>
    val like = request.body.as[LikeDislike]
    if(like.dislike && !like.like){
      BadRequest("Error")
    }

    likeRepo.findLike(like.userId, like.postId).flatMap {
      case Some(likedislike) => Future.successful(BadRequest("Error"))
      case None =>
        val newLike = likeRepo.insert(like)
        newLike.map { nl =>
          if (nl.id.nonEmpty) {
            postRepo.addLike(nl.postId)
            Ok(Json.toJson(nl))
          }else {
            BadRequest("Error")
          }
        }
    }

  }
  def dislikePost() = Action.async(parse.json) {  request =>
    val like = request.body.as[LikeDislike]
    if(!like.dislike && like.like){
      BadRequest("Error")
    }
    val forDelete = likeRepo.findLike(like.userId, like.postId)
    forDelete.map { fd =>
      if (fd.get.id.nonEmpty) {
        postRepo.removeLike(fd.get.postId)
        likeRepo.delete(fd.get.id.get)
        Ok(Json.toJson(fd))
      }else {
        BadRequest("Error")
      }
    }
  }

  def getPosts = Action.async { implicit request =>
    postRepo.list().map { posts =>
      Ok(Json.toJson(posts))
    }
  }

  def getUsersPosts(userId: Long) = Action.async { implicit request =>
    postRepo.findPostsByUserId(userId).map { posts =>
      Ok(Json.toJson((posts)))
    }
  }
  def getUsersLikedPosts(userId: Long) = Action.async { implicit request =>
    postRepo.findLikedPostsIds(userId).map { posts =>
      Ok(Json.toJson((posts)))
    }
  }

  def deletePost(id: Long): Action[AnyContent] = Action.async { implicit request =>
    postRepo.delete(id).map { response =>
      if (response) {
        Ok("Post deleted")
      } else {
        NotFound("Post not found.")
      }
    }
  }

  def editPost: Action[JsValue] = Action.async(parse.json) { request =>
    val forUpdate = request.body.as[Post]
    postRepo.updatePost(forUpdate).map{
      case 1 => Ok("Post updated.")
      case 0 => BadRequest("Bad request")

    }
  }

  def getUsersFriendsPosts(userId: Long): Action[AnyContent] = Action.async { implicit request =>
    postRepo.findFriendsPosts(userId).map { posts =>
      Ok(Json.toJson(posts))

    }
  }



}
