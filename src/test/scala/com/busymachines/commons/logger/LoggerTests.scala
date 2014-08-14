package com.busymachines.commons.logger

import com.busymachines.commons.CommonException
import org.apache.logging.log4j.LogManager
import org.scalatest.FlatSpec

/**
 * Created by Alexandru Matei on 14.08.2014.
 */
class LoggerTests extends FlatSpec {

  val logger = LogManager.getLogger()

  behavior of "Logger.error"

  it should "log commons exceptions properly" in {
    val exc = new CommonException("This is a common exception", Some("12"), Map("party" -> "BusyMachines", "user" -> "Alexandru"), Some(new IndexOutOfBoundsException()))
    logger.error(this.suiteName, exc)
  }

  it should "log default java exceptions" in {
    val exc = new IllegalArgumentException("Test illegal argument")
    logger.error(this.suiteName, exc)
  }

  behavior of "Logger.debug"

  it should "debug stuff" in {

  }
}
