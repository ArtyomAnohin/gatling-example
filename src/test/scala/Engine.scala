import computerdatabase.{BasicSimulation, ComputerDatabaseTests}
import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object Engine extends App {
  val simClass = classOf[ComputerDatabaseTests].getName
	val props = new GatlingPropertiesBuilder
	props.dataDirectory(IDEPathHelper.dataDirectory.toString)
	props.resultsDirectory(IDEPathHelper.resultsDirectory.toString)
	props.bodiesDirectory(IDEPathHelper.bodiesDirectory.toString)
	props.binariesDirectory(IDEPathHelper.mavenBinariesDirectory.toString)
  props.simulationClass(simClass)
	Gatling.fromMap(props.build)
}
