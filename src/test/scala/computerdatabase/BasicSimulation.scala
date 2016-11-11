package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicSimulation extends Simulation {

  @volatile var token = ""

  val theHttpProtocolBuilder = http
    .baseURL("http://computer-database.gatling.io")

  val theScenarioBuilder = scenario("Scenario1")
    .exec(
      http("Request Computers List")
        .get("/computers?f=macbook")
        /* Several checks on the response can be specified. */
        .check(
        /* Check that the HTTP status returned is 200, 201 or 202. */
        status.find.in(200, 202),
        /* Check that there is at least one match of the supplied regular expression in the response body. */
        regex("Computer database").count.greaterThanOrEqual(1),
        css(".computers.zebra-striped>tbody>tr>td>a").is("MacBook").saveAs(token) //verify body contains text

      )

    )

    .pause(7)
    .exec(
      http("Request " + token + "222")
        .get("/computers?f=" + token + "222")
        /* Several checks on the response can be specified. */
        .check(
        /* Check that the HTTP status returned is 200, 201 or 202. */
        status.find.in(200, 202),
        /* Check that there is at least one match of the supplied regular expression in the response body. */
        css(".well>em").is("Nothing to display") //verify body contains text

      )
    )
  setUp(
    theScenarioBuilder.inject(atOnceUsers(1))
  ).protocols(theHttpProtocolBuilder)


}
