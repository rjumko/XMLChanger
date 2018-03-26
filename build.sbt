name := "xmlchanger"

version := "1.0"

scalaVersion := "2.12.5"

cancelable in Global := true

libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
      libraryDependencies.value ++ Seq(
        "org.scala-lang.modules" %% "scala-xml" % "1.1.0",
        "org.scala-lang" % "scala-compiler" % scalaVersion.value,
        "com.typesafe" % "config" % "1.3.0",
        "com.typesafe.akka" %% "akka-actor" % "2.5.11",
        "javax.mail" % "javax.mail-api" % "1.5.4",
        "commons-io" % "commons-io" % "2.4",
        "ch.qos.logback" % "logback-classic" % "1.1.3",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
        "com.sun.mail" % "javax.mail" % "1.5.4")
    case _ =>
      // or just libraryDependencies.value if you don't depend on scala-swing
      libraryDependencies.value :+ "org.scala-lang" % "scala-swing" % scalaVersion.value
  }
}

resolvers += "softprops-maven" at "http://dl.bintray.com/content/softprops/maven"
