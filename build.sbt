name := "xmlchanger"

version := "1.0"

scalaVersion := "2.11.7"

cancelable in Global := true

//unmanagedBase := file("C:\\Users\\Администратор\\lib")

libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    // if scala 2.11+ is used, add dependency on scala-xml module
    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
      libraryDependencies.value ++ Seq(
        "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.5",
        //"org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
        //"org.scala-lang.modules" %% "scala-swing" % "1.0.1",
        "org.scala-lang" % "scala-compiler" % scalaVersion.value,
        "com.typesafe" % "config" % "1.3.0",
        "com.typesafe.akka" %% "akka-actor" % "2.4.1",
        "javax.mail" % "javax.mail-api" % "1.5.4",
        "commons-io" % "commons-io" % "2.4",
        "com.sun.mail" % "javax.mail" % "1.5.4")
    case _ =>
      // or just libraryDependencies.value if you don't depend on scala-swing
      libraryDependencies.value :+ "org.scala-lang" % "scala-swing" % scalaVersion.value
  }
}

resolvers += "softprops-maven" at "http://dl.bintray.com/content/softprops/maven"
