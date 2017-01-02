package example.jolt

import com.bazaarvoice.jolt.{Chainr, JsonUtils}

object Boot extends App {

  val chainrSpecJSON = JsonUtils.classpathToList("/simple/spec.json")
  val chainr = Chainr.fromSpec(chainrSpecJSON)

  val inputJSON = JsonUtils.classpathToObject("/simple/input.json")
  val outputJSON = JsonUtils.classpathToObject("/simple/output.json")

  val transformedOutput = chainr.transform(inputJSON)

  System.out.println(JsonUtils.toJsonString(transformedOutput))

  assert(transformedOutput == outputJSON)
}
