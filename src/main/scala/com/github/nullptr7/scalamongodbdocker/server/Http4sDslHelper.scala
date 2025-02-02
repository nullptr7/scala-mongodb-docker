package com.github.nullptr7.scalamongodbdocker.server

import org.http4s.dsl.Http4sDsl

trait Http4sDslHelper[F[_]] {

  final protected val dsl: Http4sDsl[F] = new Http4sDsl[F] {}

}
