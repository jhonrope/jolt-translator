package example.jolt.custom

import javax.inject.Inject

import com.bazaarvoice.jolt.exception.SpecException
import com.bazaarvoice.jolt.{SpecDriven, Transform}

import scala.runtime.Nothing$

/**
  * Created by Jhony on 27/12/2016.
  */
@Inject
class Translator(spec: Object) extends Transform with SpecDriven {
  if (spec == null) throw new SpecException("Shiftr expected a spec of Map type, got 'null'.")
  if (!spec.isInstanceOf[java.util.Map[_, _]]) throw new SpecException("Translator expected a spec of Map type, got " + spec.getClass.getSimpleName)


  override def transform(input: scala.Any): AnyRef = {
    println(s"hola pato $input \n spec $spec")
    input match {
      case x: AnyRef => x
      case _ => new Object
    }
  }
}
