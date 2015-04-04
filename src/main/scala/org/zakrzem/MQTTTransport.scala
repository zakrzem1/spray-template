package org.zakrzem

import org.eclipse.paho.client.mqttv3._
import org.slf4j.LoggerFactory

object MQTTTransport extends App {
  val topic = "w112/sensors/temperature/kitchen"
  //localhost
  val brokerUrl = "tcp://192.168.1.103:1883"
  val clientId = "MzaAkka"
  //  val persistence = new MemoryPersistence()
  val mqttAsyncClient = new MqttAsyncClient(brokerUrl, clientId)
  val quitCmds = List("quit", "exit")

  def subscribe(): Unit = {
    //    val opts = new MqttConnectOptions()
    //    opts.set
    val calback = new MzaMqttCallback()
    val subscribeOnConnect = new MzaConnectListener(mqttAsyncClient, topic, 0)
    mqttAsyncClient.connect(MQTTTransport, subscribeOnConnect)
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

class MzaMqttCallback extends MqttCallback {

  import MzaMqttCallback._

  override def deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken): Unit = {
    log.debug("[deliveryComplete] msg id: {}", iMqttDeliveryToken.getMessageId)
  }

  override def messageArrived(s: String, mqttMessage: MqttMessage): Unit = {
    log.debug("[messageArrived] s:{}, msg:{}", s.asInstanceOf[Any], mqttMessage.asInstanceOf[Any])
    //FIXME convert from JSON (or BSON?) to a case class and then put to db via an Actor
    //mqttMessage.getPayload
  }

  override def connectionLost(throwable: Throwable): Unit = {
    log.error("[connectionLost] with ", throwable)
  }

}

class MzaConnectListener(client: IMqttAsyncClient, topic: String, qos: Int) extends IMqttActionListener {

  import MzaMqttCallback._

  override def onFailure(asyncActionToken: IMqttToken, exception: Throwable): Unit = {
    log.error("Failed to connect to broker. Token is: {}", asyncActionToken)
  }

  override def onSuccess(asyncActionToken: IMqttToken): Unit = {
    log.info("connected: {}. subscribing to kitchen temp sensor readings", asyncActionToken)
    client.subscribe(topic, qos)
  }
}

object MzaMqttCallback {
  val log = LoggerFactory.getLogger(getClass)

  def tokenAsString(token: IMqttToken): String = {
    s"client: ${token.getClient}, exception ${token.getException}, msgId: ${token.getMessageId}, resp: ${token.getResponse}, topics: ${token.getTopics}"
  }
}