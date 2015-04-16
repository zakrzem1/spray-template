package com.example


import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.{MustMatchers, BeforeAndAfterAll, FlatSpecLike}
import spray.testkit.ScalatestRouteTest

//with Specs2RouteTest
class TemperatureServiceSpec(_system: ActorSystem) extends
//TestKit(_system) with
FlatSpecLike with TemperatureService with BeforeAndAfterAll
with ScalatestRouteTest with MustMatchers{
  def this() = this(ActorSystem("TemperatureServiceSpec"))
  def actorRefFactory = system
  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }
  behavior of "a Temperature Service"
  it should "return a greeting for GET requests to the root path" in {
      Get() ~> myRoute ~> check {
        responseAs[String] must include("Say hello")
      }
    }
//
//    "leave GET requests to other paths unhandled" in {
//      Get("/kermit") ~> myRoute ~> check {
//        handled must beFalse
//      }
//    }
//
//    "return a MethodNotAllowed error for PUT requests to the root path" in {
//      Put() ~> sealRoute(myRoute) ~> check {
//        status === MethodNotAllowed
//        entityAs[String] === "HTTP method not allowed, supported methods: GET"
//      }
//    }
//  }
}