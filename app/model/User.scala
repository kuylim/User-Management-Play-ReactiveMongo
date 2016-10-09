package model

import java.util.UUID

import play.api.libs.json._
import reactivemongo.bson.{Macros, BSONDocumentReader, BSONObjectID}


/**
  * Created by KUYLIM on 10/8/2016.
  */
case class User (
                  name: String,
                  email: String
                )

object JsonFormats
{
  implicit object UserWrites extends OWrites[User] {
    def writes(user: User): JsObject = Json.obj(
      "name" -> user.name,
      "email" -> user.email
    )
  }

  implicit object UserReads extends Reads[User] {
    def reads(json: JsValue): JsResult[User] = json match {
      case obj: JsObject => try {
        val name = (obj \ "name").as[String]
        val email = (obj \ "email").as[String]
        JsSuccess(User( name, email))

      } catch {
        case cause: Throwable => JsError(cause.getMessage)
      }
      case _ => JsError("expected.jsobject")
    }
  }
}
