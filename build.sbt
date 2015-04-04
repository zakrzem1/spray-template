

organization  := "com.example"

version       := "0.1"

name          := "spray-can-rpi-bbb"

scalaVersion  := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

resolvers += "Paho Releases" at "https://repo.eclipse.org/content/repositories/paho-releases"

libraryDependencies ++= Seq(
  "io.spray"            %%   "spray-can"     % "1.3.2",
  "io.spray"            %%   "spray-routing" % "1.3.2",
  "io.spray"            %%   "spray-testkit" % "1.3.2" % "test",
  "com.typesafe.akka"   %%  "akka-actor"    % "2.3.9",
  "com.typesafe.akka"   %%  "akka-testkit"  % "2.3.9" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  //"org.specs2"          %%  "specs2"        % "3.0.1" % "test" ,
  "org.eclipse.paho"    %  "org.eclipse.paho.client.mqttv3" % "1.0.2",
  "ch.qos.logback" % "logback-classic" % "1.0.9"
)

seq(Revolver.settings: _*)

assemblySettings