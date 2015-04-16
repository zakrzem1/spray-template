package org.zakrzem.db

import org.joda.time.DateTime

import scala.slick.driver.H2Driver.simple._
import com.github.tototoshi.slick.H2JodaSupport._
import scala.slick.lifted.{ProvenShape, Tag}

//PostgresJodaSupport

class SensorReadings(tag: Tag) extends Table[(Int, String, DateTime, BigDecimal, BigDecimal)](tag, "READINGS") {
  def id: Column[Int] = column[Int]("READ_ID", O.PrimaryKey, O.AutoInc)

  def sensorId: Column[String] = column[String]("SENSOR_ID")

  def timestamp: Column[DateTime] = column[DateTime]("TSTAMP")

  //.setScale(2, RoundingMode.CEILING)
  def temperature: Column[BigDecimal] = column[BigDecimal]("TEMPERATURE", O.DBType("decimal(5, 2)"))

  def humidity: Column[BigDecimal] = column[BigDecimal]("HUMIDITY", O.DBType("decimal(5, 2)"))

  // Every table needs a * projection with the same type as the table's type parameter
  def * : ProvenShape[(Int, String, DateTime, BigDecimal, BigDecimal)] =
    (id, sensorId, timestamp, temperature, humidity)

//  def autoInc = (sensorId, timestamp, temperature, humidity) returning id
}
