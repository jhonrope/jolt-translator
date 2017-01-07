package jolt.translator.utils

import com.bazaarvoice.jolt.JsonUtils
import org.scalatest.{FlatSpec, Matchers}


class TranslationsFactoryTest extends FlatSpec with Matchers {

  "TranslationsFactory" should "parsear el string para construir las traducciones" in {
    val objectList = JsonUtils.classpathToObject("/jolt/translator/utils/TranslatiosList.json")
    val json: String = JsonUtils.toJsonString(objectList)

    val translations = TranslateUtils.parse(json)

    translations.keySet().size() shouldBe 2

    val ciudades = translations.get("CIUDAD")

    ciudades.size() shouldBe 2
    ciudades get "052" shouldBe "MEDELLIN"
    ciudades get "100" shouldBe "BOGOTA"

    val sucursales = translations.get("SUCURSAL")

    sucursales.size() shouldBe 1
    sucursales get "0001" shouldBe "Plaza Principal"
  }

}
