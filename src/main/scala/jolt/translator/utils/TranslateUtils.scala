package jolt.translator.utils

import java.util

import com.bazaarvoice.jolt.JsonUtils

import scala.collection.JavaConversions._
import scala.collection.Map

object TranslateUtils {

  private def asMap(obj: AnyRef): util.Map[String, String] = {
    obj match {
      case m: util.Map[String, String] => m
      case _ => throw new Exception("El mapa no tiene la estructura requerida.")
    }
  }

  def parse(translationsJson: String): util.Map[String, util.Map[String, String]] = {
    val lista: util.List[AnyRef] = JsonUtils.jsonToList(translationsJson)

    val listTraduccion = lista.map { case m: util.Map[AnyRef, AnyRef] =>
      val origen = asMap(m.get("origen"))
      val destino = asMap(m.get("destino"))

      origen.get("tipo") -> (origen.get("valor") -> destino.get("valor"))
    }

    val agrupacion = listTraduccion.groupBy(_._1).mapValues(_.map(_._2).toMap)

    val mapa: Map[String, util.Map[String, String]] = mapAsJavaMap(agrupacion).mapValues(mapAsJavaMap)

    mapa
  }
}
