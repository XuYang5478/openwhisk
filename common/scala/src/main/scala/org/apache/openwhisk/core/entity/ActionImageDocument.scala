package org.apache.openwhisk.core.entity

import spray.json.{DefaultJsonProtocol, JsObject, JsString, JsValue}

case class ActionImageDocument(imageName: String) extends WhiskDocument {
  /**
   * Gets unique document identifier for the document.
   */
  override protected def docid: DocId = DocId(imageName)

  /**
   * The representation as JSON, e.g. for REST calls. Does not include id/rev.
   */
  override def toJson: JsObject = JsObject("image" -> JsString(imageName))
}

object ActionImageJsonProtocol extends DefaultJsonProtocol {
  implicit def ImageFormat = jsonFormat1(ActionImageDocument)
}

object ActionImageReader extends DocumentReader {
  override def read[A](ma: Manifest[A], value: JsValue): WhiskDocument = value.asJsObject.getFields("imageName") match {
    case Seq(JsString(image)) => ActionImageDocument(image)
    case _ => ActionImageDocument("")
  }
}