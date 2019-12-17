package controllers

import DTOs.LoginDto
import repositories.UsersRepository
import javax.inject._
import models.{LikeDislike, User}
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.ExecutionContext


@Singleton
class LoginController @Inject()(cc: ControllerComponents, usersRepository: UsersRepository) (implicit ec: ExecutionContext) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */


  //TODO: Popraviti login
  def login() = Action.async(parse.json) {  request =>
    val loginDto = request.body.as[LoginDto]
    /*
    val x = usersRepository.findByEmail(loginDto.email).map {
      case Some(user) =>

      case None => BadRequest("neispravni kredencijali")
    }
    */
      usersRepository.findByEmail(loginDto.email).map{
        case Some(user) => {
          if(user.password.equals(loginDto.password)){
            Ok(Json.toJson(user))
          }else{
            BadRequest("Bad credentials")
          }
        }
        case None => BadRequest("Bad credentials")
      }

    /*
    usersRepository.findByEmail(loginDto.email).map{ user =>
      if(user.get.password.equals(loginDto.password)){
        Ok(Json.toJson(user))
      }else{
        BadRequest("neispravni kredencijali")
      }

    }
    */

  }
  def register() = Action.async(parse.json) {  request =>
    //val user = Json.fromJson(request.body)
    //println( user)
    val newUser = request.body.as[User]
    println("------")
    println(request.body)
    println("------")
    println(newUser)
    usersRepository.findByEmail(newUser.email).map{ user =>
      if(user.nonEmpty){
        BadRequest("Vec ste registrovani")
      }else{
        usersRepository.insert(newUser)
        Ok("Uspesno ste se registovali.")
      }

    }

  }
}
