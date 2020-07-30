name := "zio-memory-issue"

version := "0.1"

scalaVersion := "2.13.3"
fork in run := true
javaOptions += "-Xmx100M"

val zioVersion            = "1.0.0-RC21-2"
val zioInteropCatsVersion = "2.1.4.0-RC17"

libraryDependencies ++= Seq(
  "com.github.fd4s" %% "fs2-kafka"        % "1.0.0",
  "org.typelevel" %% "cats-effect" % "2.1.4",
  "dev.zio"         %% "zio"              % zioVersion,
  "dev.zio"         %% "zio-interop-cats" % zioInteropCatsVersion,
//  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

