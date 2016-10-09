package repository

import java.nio.channels.Selector

import model.User
import play.api.libs.json.{Json, JsObject}

import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.{Future, ExecutionContext}

/**
  * Created by KUYLIM on 10/9/2016.
  */
trait UserRepository {

  def find()(implicit ec: ExecutionContext): Future[List[JsObject]]

  def select(id: BSONDocument)(implicit ec: ExecutionContext): Future[Option[JsObject]]

  def update(id: BSONDocument, update: BSONDocument)(implicit ec: ExecutionContext): Future[WriteResult]

  def remove(id: BSONDocument)(implicit ec: ExecutionContext): Future[WriteResult]

  def save(document: User)(implicit ec: ExecutionContext): Future[WriteResult]

}
