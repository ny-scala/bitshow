organization := "com.example"

name := "bitshow"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.0-1"

libraryDependencies ++= Seq(
   "net.databinder" %% "unfiltered-filter" % "0.4.1",
   "net.databinder" %% "unfiltered-jetty" % "0.4.1",
   "net.databinder" %% "unfiltered-json" % "0.4.1",
   "net.databinder" %% "unfiltered-uploads" % "0.4.1",
   "org.clapper" %% "avsl" % "0.3.3",
   "com.mongodb.casbah" %% "casbah" % "2.1.5-1"
)

resolvers ++= Seq(
  "java m2" at "http://download.java.net/maven/2"
)
