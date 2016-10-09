package repository.repositoryImpl

import javax.inject.Inject


import model.User
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import repository.UserRepository
import model.JsonFormats._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by KUYLIM on 10/9/2016.
  */
class UserRepoImpl @Inject() (reactiveMongoApi: ReactiveMongoApi) extends UserRepository {

  def collection = reactiveMongoApi.db.collection[JSONCollection]("users");

  override def find()(implicit ec: ExecutionContext): Future[List[JsObject]] = {
    val genericQueryBuilder = collection.find(Json.obj());
    val cursor = genericQueryBuilder.cursor[JsObject](ReadPreference.Primary);
    cursor.collect[List]()
  }

  override def update(id: BSONDocument, update: BSONDocument)(implicit ec: ExecutionContext): Future[WriteResult] = {
    collection.update(id, update)
  }

  override def remove(id: BSONDocument)(implicit ec: ExecutionContext): Future[WriteResult] = {
    collection.remove(id)
  }

  override def select(id: BSONDocument)(implicit ec: ExecutionContext): Future[Option[JsObject]] = {
    collection.find(id).one[JsObject]
  }

  override def save(document: User)(implicit ec: ExecutionContext): Future[WriteResult] = {
    collection.insert(document)
  }
}
