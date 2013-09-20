package models

import play.api.data.Form
import play.api.data.Forms._

/**
 * Copyright 2013 www.smartdeveloping.com
 * User: andrewboss
 * Date: 15/09/2013
 * Time: 21:07
 */

case class User(
  username: String,
  password: String,
  firstName: String,
  lastName: String
)

object User {
  val form = Form(
    mapping(
      "username" -> text,
      "password" -> text,
      "firstName" -> text,
      "lastName" -> text
    )(User.apply _)(User.unapply _))
}

object JsonFormats {
  import play.api.libs.json.Json
  import play.api.data._
  import play.api.data.Forms._

  // Generates Writes and Reads for User
  implicit val userFormat = Json.format[User]

  val userForm = Form(
    mapping(
      "username" -> text,
      "password" -> text,
      "firstName" -> text,
      "lastName" -> text
    )(User.apply _)(User.unapply _))

}
