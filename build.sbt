name := "jolt-translator"

version := "1.0"

scalaVersion := "2.11.8"


libraryDependencies ++= Seq(
  "com.bazaarvoice.jolt" % "jolt-core" % "0.1.0",
  "com.bazaarvoice.jolt" % "json-utils" % "0.1.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)