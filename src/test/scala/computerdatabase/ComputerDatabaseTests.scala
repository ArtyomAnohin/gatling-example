package computerdatabase

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.netty.util.internal.ThreadLocalRandom

/**
  * Created by User on 12-Nov-16.
  */
class ComputerDatabaseTests extends Simulation {
  val feeder = csv("Computers.csv").random

  object Home {
    val home = exec(http("Home")
      .get("/")
      .check(status.is(200)))

  }

  object PostNew {
    val browse = exec(http("Get New computer page")
      .get("/computers/new"))
      .pause(3)
    val edit = feed(feeder)
      .exec(http("Post new computer")
        .post("/computers")
        .formParam("name", "${ComputerName}")
        .formParam("introduced", "${IntroducedDate}")
        .formParam("discontinued", "${DiscontinuedDate}")
        .formParam("company", "${Company}")
        .check(
          status.find.in(200),
          css(".alert-message.warning").is("Done! Computer ${ComputerName} has been created") //verify alert text
        )
      )
      .pause(5)
  }

  object Delete {
    val delete = feed(feeder)
      .exec(http("Open new computer page")
        .get("/computers?f=${ComputerName}")
        .check(css("a:contains('${ComputerName}')", "href").saveAs("computerURL"))
      )
      .pause(1)
      .exec(http("05 Delete computer")
        .post("${computerURL}/delete")
        .check(
          status.find.in(200),
          css(".alert-message.warning").is("Done! Computer has been deleted") //verify alert text
        )
      )
  }

  val httpConf = http
    .baseURL("http://computer-database.gatling.io")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val users = scenario("Add Delete computer").exec(Home.home, PostNew.browse, PostNew.edit, Delete.delete)

  setUp(
    users.inject(constantUsersPerSec(50) during (2 minutes))
  ).protocols(httpConf)
}
