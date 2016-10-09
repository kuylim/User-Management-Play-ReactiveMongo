package controllers

import javax.inject.Inject

import model.User
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsObject, Json}
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.bson.{BSONObjectID, BSONDocument}
import repository.repositoryImpl.UserRepoImpl
import model.JsonFormats._


/**
  * Created by KUYLIM on 10/9/2016.
  */
class  RESTfulController @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents{

  def userRepo = new UserRepoImpl(reactiveMongoApi)

  def index = Action.async {
    implicit request =>
      userRepo.find().map(users => Ok(Json.toJson(users)))
  }

  def create = Action.async(BodyParsers.parse.json) { implicit request =>
    val user = (request.body).as[User]
    userRepo.save(user).map(result => Created)
  }

  def read(id: String) = Action.async {
    userRepo.select(BSONDocument("_id" -> BSONObjectID(id))).map(user => Ok(Json.toJson(user)))
  }

  def update(id: String) = Action.async(BodyParsers.parse.json) {
    { implicit request =>
      val user = (request.body).as[User]
      val selector = BSONDocument("_id" -> BSONObjectID(id))

      val modifier = BSONDocument(
        "$set" -> BSONDocument(
          "name" -> user.name,
          "email" -> user.email))
      userRepo.update(selector, modifier).map(result => Accepted)
    }
  }

  def delete(id: String) = Action.async {
    userRepo.remove(BSONDocument("_id" -> BSONObjectID(id)))
      .map(result => Accepted)
  }
}

