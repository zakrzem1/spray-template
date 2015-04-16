package org.zakrzem.db

//import org.h2.engine.Database

import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import org.zakrzem.db.SensorReadingsH2Repository.log

import scala.slick.driver.H2Driver.simple._
import scala.slick.lifted.TableQuery

trait SensorReadingsRepository {

  def record(sensorId: String, when: DateTime, temp: BigDecimal, hdty: BigDecimal): Unit

}

class SensorReadingsH2Repository extends SensorReadingsRepository {
  // The query interface for the Suppliers table
  val sensorReadings: TableQuery[SensorReadings] = TableQuery[SensorReadings]

  val db = Database.forURL("jdbc:h2:mem:sensors;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

  // Create the schema by combining the DDLs for the Suppliers and Coffees
  // tables using the query interfaces
  db.withSession { implicit session =>
    log.debug("[constructor] creating table")
    sensorReadings.ddl.create
  }

  override def record(sensorId: String, when: DateTime, temp: BigDecimal, hdty: BigDecimal) = {
    db.withSession { implicit session =>
      //suppliers += (101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199")
      //      sensorReadings.insert((0,sensorId, when, temp, hdty))
      sensorReadings +=(0, sensorId, when, temp, hdty)
      log.debug("[record] sensor readings :{}",sensorReadings.list)
      //      sensorReadings.take()
    }
  }
}

object SensorReadingsH2Repository {
  val log = LoggerFactory.getLogger(getClass)
}