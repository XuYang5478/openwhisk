package org.apache.openwhisk.core.containerpool.kubernetes

import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.apache.openwhisk.common.Logging

import spray.json._
import DefaultJsonProtocol._

object ServerClient {
  private val client = HttpClients.createDefault()

  def postRequest(url: String, data: Map[String, String])(implicit log: Logging): String = {
    val post = new HttpPost(url)
    post.addHeader("Content-Type", "application/json")
    post.setEntity(new StringEntity(data.toJson.compactPrint))

    var result = ""
    try {
      val response = client.execute(post)
      result = EntityUtils.toString(response.getEntity, "UTF-8")
    } catch {
      case ex: Exception =>
        log.error(this, ex.getMessage)
        result = "error"
    }

    log.info(this, s"访问 $url ，结果: $result")
    result
  }
}
