package org.zakrzem

import org.joda.time.DateTime

// {"hum": 47.599998474121094, "temp": 23.5, "tstamp": "2015-04-09 01:12:16.165078"
case class SensorReadingTransport(hum: BigDecimal, temp: BigDecimal, tstamp: DateTime) {

}
