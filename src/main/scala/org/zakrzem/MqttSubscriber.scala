package org.zakrzem

import java.nio.charset.Charset

import org.eclipse.paho.client.mqttv3._
import org.slf4j.LoggerFactory
import org.zakrzem.db.{SensorReadingsH2Repository, SensorReadingsRepository}


object MqttSubscriber extends App {
  val topic = "w112/sensors/temperature/kitchen"
  //localhost
  val brokerUrl = "tcp://192.168.1.103:1883"
  val clientId = "MzaAkka"
  //  val persistence = new MemoryPersistence()
  val mqttAsyncClient = new MqttAsyncClient(brokerUrl, clientId)
  val quitCmds = List("quit", "exit")
  val srs = new SensorReadingsH2Repository()
  def subscribe(): Unit = {
    //    val opts = new MqttConnectOptions()
    //    opts.set
    val calback = new MqttCallbackImpl(srs)
    val subscribeOnConnect = new MqttConnectListener(mqttAsyncClient, topic, 0)
    mqttAsyncClient.connect(MqttSubscriber, subscribeOnConnect)
    mqttAsyncClient.setCallback(calback)
  }

  def waitForQuit() = {
    //    Iterator.continually(Console.readLine).takeWhile(_.nonEmpty).foreach(line => println("read " + line))
    var run: Boolean = true
    while (run) {
      val lineRead = io.StdIn.readLine()
      if (lineRead == null || quitCmds.contains(lineRead.toLowerCase))
        run = false
      else println("tick")
    }
  }

  subscribe()
  waitForQuit()
}

class MqttCallbackImpl(sensorReadings : SensorReadingsRepository) extends MqttCallback {

  import MqttCallbackImpl._

  override def deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken): Unit = {
    log.debug("[deliveryComplete] msg id: {}", iMqttDeliveryToken.getMessageId)
  }

  val activeCharset = Charset.forName("UTF-8")

  override def messageArrived(s: String, mqttMessage: MqttMessage): Unit = {
    log.debug("[messageArrived] s:{}, msg:{}", s.asInstanceOf[Any], mqttMessage.asInstanceOf[Any])
    //FIXME convert from JSON (or BSON?) to a case class and then put to db via an Actor
    import org.json4s._
    import org.json4s.native.JsonMethods._
    import org.json4s.ext.JodaTimeSerializers
    implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

    val json = parse(new String(mqttMessage.getPayload, activeCharset))

    // Brings in default date formats etc

    val sr = json.extract[SensorReadingTransport]
    //yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
    // 2015-04-10 00:55:23.951767"
    sensorReadings.record(s.substring(s.lastIndexOf('/')+1),sr.tstamp,sr.temp, sr.hum)
  }

  override def connectionLost(throwable: Throwable): Unit = {
    log.error("[connectionLost] with ", throwable)
  }

}

object MqttCallbackImpl {
  val log = LoggerFactory.getLogger(getClass)

  def tokenAsString(token: IMqttToken): String = {
    s"client: ${token.getClient}, exception ${token.getException}, msgId: ${token.getMessageId}, resp: ${token.getResponse}, topics: ${token.getTopics}"
  }
}

class MqttConnectListener(client: IMqttAsyncClient, topic: String, qos: Int) extends IMqttActionListener {

  import MqttCallbackImpl._

  override def onFailure(asyncActionToken: IMqttToken, exception: Throwable): Unit = {
    log.error("Failed to connect to broker. Token is: {}", asyncActionToken)
  }

  override def onSuccess(asyncActionToken: IMqttToken): Unit = {
    log.info("connected: {}. subscribing to kitchen temp sensor readings", asyncActionToken)
    client.subscribe(topic, qos)
  }
}

