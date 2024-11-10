package com.github.nullptr7
package scalamongodbdocker
package db

import org.mongodb.scala._

class MongoService(mongoClient: MongoClient) {

  def ping(): Observable[Document] =
    mongoClient
      .getDatabase("admin")
      .runCommand[Document](Document("ping" -> 1))

}
