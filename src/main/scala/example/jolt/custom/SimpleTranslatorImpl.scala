package example.jolt.custom

import java.util
import javax.inject.Inject

import com.bazaarvoice.jolt.JsonUtils
import jolt.Translator
import jolt.translator.utils.TranslateUtils

@Inject
class SimpleTranslatorImpl(spec: Object) extends Translator(spec) {

  override def getTranslations(): util.Map[String, util.Map[String, String]] = {
    val jsonObject = JsonUtils.classpathToObject("/translations/responseLike.json")
    val jsonString = JsonUtils.toJsonString(jsonObject)

    TranslateUtils.parse(jsonString)

  }
}