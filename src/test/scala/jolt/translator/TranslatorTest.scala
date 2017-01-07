package jolt.translator

import java.util

import com.bazaarvoice.jolt.JsonUtils
import com.bazaarvoice.jolt.exception.{SpecException, TransformException}
import org.scalatest.{FlatSpec, Matchers}


class TranslatorTest extends FlatSpec with Matchers {

  "Translator" should "devolver una exception si no se encuentran un mapa de translations" in {
    intercept[TransformException] {
      runTranslateTest("withoutHomologaciones")
    }
  }

  it should "cargar las translations enviadas en el spec" in {
    runTranslateTest("simple")
  }

  it should "reemplazar el valor de una llave de input y homologarlo al valor correspondiente dado el indicio del spec" in {
    runTranslateTest("simple")
  }

  it should "reemplazar los valores anidados" in {
    runTranslateTest("nested")
  }

  it should "reemplazar valores de una lista" in {
    runTranslateTest("list")
  }

  it should "reemplazar valores cuando el valor de entrada es una lista" in {
    runTranslateTest("inputList")
  }

  it should "continuar normalmente cuando el spec es vacio" in {
    runTranslateTest("withoutSpec")
  }

  it should "devolver una exception cuando el spec tiene numeros" in {
    intercept[SpecException] {
      runTranslateTest("numberInSpec")
    }
  }

  it should "devolver una exception cuando el spec tiene null" in {
    intercept[SpecException] {
      runTranslateTest("nullInSpec")
    }
  }

  def runTranslateTest(testCaseName: String) = {

    val testPath = "/jolt/translator/" + testCaseName
    val testUnit: java.util.Map[String, Object] = JsonUtils.classpathToMap(testPath + ".json")

    val input = testUnit.get("input")
    val spec = testUnit.get("spec")
    val map = testUnit.get("translations")
    val expected = testUnit.get("expected")

    val translator = new TranslatorImplTest(spec) {
      override def getTranslations(): util.Map[String, util.Map[String, String]] = {

        if (map != null) {
          map.asInstanceOf[java.util.Map[String, java.util.Map[String, String]]]
        } else {
          null
        }

      }
    }

    val actual = translator.transform(input)

    JsonUtils.toJsonString(actual) shouldBe JsonUtils.toJsonString(expected)
  }

}
