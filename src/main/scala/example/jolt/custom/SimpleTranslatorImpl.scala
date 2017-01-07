package example.jolt.custom

import java.util
import javax.inject.Inject

import jolt.Translator

@Inject
class SimpleTranslatorImpl(spec: Object) extends Translator(spec) {

  override def getTranslations(): util.Map[String, util.Map[String, String]] = {

    val mapaCiudad = new util.HashMap[String, String]()
    mapaCiudad.put("042", "MEDELLIN")

    val mapaTiposDocumentos = new util.HashMap[String, String]()
    mapaTiposDocumentos.put("C", "cedula")
    mapaTiposDocumentos.put("TI", "tarjeta.identidad")
    mapaTiposDocumentos.put("CE", "cedula.extranjeria")
    mapaTiposDocumentos.put("RC", "registro.civil")

    val mapa = new util.HashMap[String, util.Map[String, String]]()
    mapa.put("CIUDAD", mapaCiudad)
    mapa.put("TIPODOCUMENTOS", mapaTiposDocumentos)

    mapa
  }
}