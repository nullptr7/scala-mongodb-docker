package com.github.nullptr7
package scalamongodbdocker
package exceptions

trait DatabaseException extends Exception

case class DatabaseConnectionException(t: Throwable) extends Exception {
  override def getCause:   Throwable =
    t
  override def getMessage: String    =
    "Exception occurred while connecting to database. Please check the logs..."

}

case class EmployeeDetailsFetchException(t: Throwable) extends DatabaseException {
  override def getCause:   Throwable =
    t
  override def getMessage: String    =
    "Exception occurred while connecting to database. Please check the logs..."

}
