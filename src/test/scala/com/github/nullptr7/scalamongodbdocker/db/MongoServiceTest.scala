package com.github.nullptr7
package scalamongodbdocker
package db

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.mongodb.scala._
import org.mongodb.scala.bson.collection.immutable.Document
import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.mockito.MockitoSugar

class MongoServiceTest extends AnyFunSuite with MockitoSugar with ScalaFutures with OptionValues {

  test("ping should return successful ping response") {
    val mockMongoClient: MongoClient   = mock[MongoClient]
    val mockDatabase:    MongoDatabase = mock[MongoDatabase]
    val pingResponse:    Document      = Document("ok" -> 1)
    val mongoService:    MongoService  = new MongoService(mockMongoClient)

    when(mockMongoClient.getDatabase("admin")).thenReturn(mockDatabase)
    when(mockDatabase.runCommand[Document](any())(any(), any())).thenReturn(SingleObservable(pingResponse))

    whenReady(mongoService.ping().headOption())(_.value shouldBe pingResponse)
  }
}
