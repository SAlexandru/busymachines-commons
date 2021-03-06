package busymachines.json_test.auto

import busymachines.json_test._
import org.scalatest.FlatSpec

/**
  *
  * Here we test [[busymachines.json.auto]] derivation at compile time,
  * mostly to show what imports are required, and which are not.
  *
  * See the [[Melon]] hierarchy
  *
  * @author Lorand Szakacs, lsz@lorandszakacs.com, lorand.szakacs@busymachines.com
  * @since 10 Aug 2017
  *
  */
class JsonDefaultAutoDerivationCompilationTest extends FlatSpec {

  //-----------------------------------------------------------------------------------------------

  it should "... compile when having the correct imports" in {
    assertCompiles {
      """
        |import busymachines.json.syntax._
        |import busymachines.json.auto._
        |
        |val anarchistMelon = AnarchistMelon(noGods = true, noMasters = true, noSuperTypes = true)
        |val asJson = anarchistMelon.asJson.spaces2
        |val read = asJson.unsafeDecodeAs[AnarchistMelon]
        |
      """.stripMargin
    }
  }

  //-----------------------------------------------------------------------------------------------

  it should "... fail to compile when auto._ is imported together with semiauto._" in {
    /**
      * These two cannot work well together because both of them define a [[busymachines.json.Configuration]]
      * implicit that ensures that sealed trait hierarchies are serialized using the _type discriminator
      */
    assertDoesNotCompile {
      """
        |
        |import busymachines.json.syntax._
        |import busymachines.json.auto._
        |import busymachines.json.semiauto._
        |
        |val anarchistMelon = AnarchistMelon(noGods = true, noMasters = true, noSuperTypes = true)
        |val asJson = anarchistMelon.asJson.spaces2
        |val read = asJson.unsafeDecodeAs[AnarchistMelon]
        |
      """.stripMargin
    }
  }

  //-----------------------------------------------------------------------------------------------

  it should "... compile when auto._ is imported together with semiauto._, and one of them excludes the defaultConfiguration" in {
    /**
      * These two cannot work well together because both of them define a [[busymachines.json.Configuration]]
      * implicit that ensures that sealed trait hierarchies are serialized using the _type discriminator
      */
    assertCompiles {
      """
        |
        |import busymachines.json.syntax._
        |import busymachines.json.auto._
        |import busymachines.json.semiauto.{defaultDerivationConfiguration => _, _}
        |
        |val anarchistMelon = AnarchistMelon(noGods = true, noMasters = true, noSuperTypes = true)
        |val asJson = anarchistMelon.asJson.spaces2
        |val read = asJson.unsafeDecodeAs[AnarchistMelon]
        |
      """.stripMargin
    }
  }

  //-----------------------------------------------------------------------------------------------

}
