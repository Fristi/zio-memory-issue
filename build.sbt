val commonSettings = Seq(
  scalaVersion := "2.13.3",
  fork in run := true,
  javaOptions += "-Xmx100M"
)

lazy val rc20 = project.in(file("rc20"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.github.fd4s" %% "fs2-kafka"        % "1.0.0",
      "org.typelevel" %% "cats-effect" % "2.1.3",
      "dev.zio"         %% "zio"              % "1.0.0-RC20",
      "dev.zio"         %% "zio-interop-cats" % "2.1.3.0-RC15"
    )
  )

lazy val rc21 = project.in(file("rc21"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.github.fd4s" %% "fs2-kafka"        % "1.0.0",
      "org.typelevel" %% "cats-effect" % "2.1.4",
      "dev.zio"         %% "zio"              % "1.0.0-RC21-2",
      "dev.zio"         %% "zio-interop-cats" % "2.1.4.0-RC17"
    )
  )
