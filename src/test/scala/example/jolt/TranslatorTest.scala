package example.jolt

import com.bazaarvoice.jolt.JsonUtils
import com.bazaarvoice.jolt.exception.TransformException
import jolt.custom.Translator
import org.scalatest.{FlatSpec, Matchers}


class TranslatorTest extends FlatSpec with Matchers {

  "Translator" should "separar el mapa de homologaciones del spec" in {
    runTranslateTest("removeHomologaciones")
  }

  it should "devolver una exception si no se encuentran un mapa de homologaciones" in {

    intercept[TransformException] {
      runTranslateTest("withoutHomologaciones")
    }
  }

  it should "cargar las homologaciones enviadas en el spec" in {
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

  it should "reemplazar valores de los objetos dentro de una lista" in {
    pending
  }


  def runTranslateTest(testCaseName: String) = {

    val testPath = "/example/jolt/translator/" + testCaseName
    val testUnit: java.util.Map[String, Object] = JsonUtils.classpathToMap(testPath + ".json")

    val input = testUnit.get("input")
    val spec = testUnit.get("spec")
    val expected = testUnit.get("expected")


    val translator = new Translator(spec)
    val actual = translator.transform(input)

    JsonUtils.toJsonString(actual) shouldBe JsonUtils.toJsonString(expected)
  }

}
