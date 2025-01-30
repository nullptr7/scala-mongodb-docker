package com.github.nullptr7.scalamongodbdocker.server

import org.http4s.dsl.Http4sDsl

trait Http4sDslHelper[F[_]] {

  protected final val dsl = new Http4sDsl[F] {}

}
