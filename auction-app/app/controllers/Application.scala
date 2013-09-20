package controllers

import play.api._
import play.api.mvc._

import reactivemongo.api._

import play.modules.reactivemongo._
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.json._
import scala.concurrent.Future
import models.User
import play.api.Play.current

object Application extends Controller with MongoController {

  def collection: JSONCollection = db.collection[JSONCollection]("users")

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def create(username: String, password: String, firstName: String, lastName: String) = Action {
    Async {
      val json = Json.obj(
        "username" -> username,
        "password" -> password,
        "firstName" -> firstName,
        "lastName" -> lastName,
        "created" -> new java.util.Date().getTime(),
        "updated" -> new java.util.Date().getTime()
      )

      collection.insert(json).map(lastError => Ok("Mongo lastError: %s".format(lastError)))
    }
  }

  def createFromJson = Action(parse.json) { request =>
    Async {
      collection.insert(request.body).map(lastError =>
        Ok("Mongo LastErorr:%s".format(lastError)))
    }
  }

  def findByUsername(username: String) = Action {
    Async {

      val cursor: Cursor[JsObject] = collection.find(Json.obj("username" -> username)).sort(Json.obj("created" -> -1)).cursor[JsObject]

      // gather all the JsObjects in a list
      val futureUsersList: Future[List[JsObject]] = cursor.toList

      // transform the list into a JsArray
      val futureUsersJsonArray: Future[JsArray] = futureUsersList.map { users =>
        Json.arr(users)
      }

      // everything's ok! Let's reply with the array
      futureUsersJsonArray.map { users =>
        Ok(users)
      }
    }
  }

  def showCreationForm = Action {
    Ok(views.html.editUsers(User.form))
  }

}