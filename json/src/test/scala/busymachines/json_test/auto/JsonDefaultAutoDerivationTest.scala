package busymachines.json_test.auto

import busymachines.json_test.AnEnum.{AnEnum, Value}
import busymachines.json_test.{AnEnumWithDifferentName, _}
import org.scalatest.FlatSpec

/**
  *
  * Here we test [[busymachines.json.auto]] derivation
  *
  * See the [[Melon]] hierarchy
  *
  * @author Lorand Szakacs, lsz@lorandszakacs.com, lorand.szakacs@busymachines.com
  * @since 10 Aug 2017
  *
  */
class JsonDefaultAutoDerivationTest extends FlatSpec {



  import busymachines.json.syntax._
  import busymachines.json.auto._
  import busymachines.json._

  //-----------------------------------------------------------------------------------------------

  it should "... be able to serialize/deserialize anarchist melon (i.e. not part of any hierarchy)" in {
    val anarchistMelon = AnarchistMelon(noGods = true, noMasters = true, noSuperTypes = true)
    val asJson = anarchistMelon.asJson.spaces2
    val read = asJson.unsafeDecodeAs[AnarchistMelon]
    assertResult(anarchistMelon)(read)
  }


  //-----------------------------------------------------------------------------------------------

  it should "... fail to compile when there is no explicitly defined codec for a type down in the hierarchy" in {
    withClue("... decoding part") {
      assertDoesNotCompile(
        """
          |val rawJson = "{}"
          |rawJson.unsafeDecodeAs[WinterMelon]
        """.stripMargin
      )
    }

    withClue("... encoding part") {
      assertDoesNotCompile(
        """
          |val winterMelon: WinterMelon = WinterMelon(fuzzy = true, weight = 45)
          |winterMelon.asJson
        """.stripMargin
      )
    }

  }

  //-----------------------------------------------------------------------------------------------

  it should "... be able to serialize/deserialize a case class from hierarchy when it is referred to as its super-type" in {
    val winterMelon: Melon = WinterMelon(fuzzy = true, weight = 45)
    val rawJson = winterMelon.asJson.spaces2
    val read = rawJson.unsafeDecodeAs[Melon]
    assertResult(winterMelon)(read)
  }

  //-----------------------------------------------------------------------------------------------

  it should "... be able to deserialize case objects of the hierarchy" in {
    val smallMelon: Melon = SmallMelon
    val rawJson = smallMelon.asJson.spaces2
    val read = rawJson.unsafeDecodeAs[Melon]
    assertResult(smallMelon)(read)
  }

  //-----------------------------------------------------------------------------------------------

  it should "... deserialize hierarchies of case objects using the default method with _type" in {
    val taste: List[Taste] = List(SweetTaste, SourTaste)
    val rawJson = taste.asJson.spaces2
    assert(rawJson.contains("_type"), "... tested default json serialization method")
    val read = rawJson.unsafeDecodeAs[List[Taste]]
    assertResult(read)(taste)
  }

  //-----------------------------------------------------------------------------------------------

  it should "... deserialize list of all case classes from the hierarchy" in {
    val winterMelon: Melon = WinterMelon(fuzzy = true, weight = 45)
    val waterMelon: Melon = WaterMelon(seeds = true, weight = 90)
    val smallMelon: Melon = SmallMelon
    val squareMelon: Melon = SquareMelon(weight = 10, tastes = Seq(SourTaste, SweetTaste))
    val melons = List[Melon](winterMelon, waterMelon, smallMelon, squareMelon)

    val rawJson = melons.asJson.spaces2
    val read: List[Melon] = rawJson.unsafeDecodeAs[List[Melon]]
    assertResult(melons)(read)
  }


  //-----------------------------------------------------------------------------------------------
  it should ".... serialize / deserialize an enum type when given with the correct string " in {


    val rawJson = AnEnum.One.asJson.noSpaces
    val read = rawJson.unsafeDecodeAs[AnEnum]

    assertResult(AnEnum.One)(read)

  }

  //-----------------------------------------------------------------------------------------------
  it should ".... deserialize values of an enum regardless of case type" in {
    val strings = Seq("one", "onE", "oNe", "oNE", "One", "OnE", "ONe", "ONE") map {str => s""" "$str" """.trim.decodeAs[AnEnum]}

    strings.find{_.isLeft} match {
      case Some(Left(failure)) => fail(failure)
      case _ => succeed
    }
  }

  //-----------------------------------------------------------------------------------------------
  it should ".... deserialize value to an  enum value should fail if the value / string doesn't exist in the enum" in {
    "\"two\"".decodeAs[AnEnum] match  {
      case Left(_) => succeed
      case _ => fail("AnEnum with only one value: One, was instantiated when decoding a string of value two ")
    }
  }


  //-----------------------------------------------------------------------------------------------
  it should ".... serialize / deserialize an enum type with custom names " in {
    val rawJson = AnEnumWithDifferentName.One.asJson.noSpaces
    val read = rawJson.unsafeDecodeAs[AnEnumWithDifferentName.AnEnumWithDifferentName]

    assert (rawJson equalsIgnoreCase "\"o ne\"")
    assertResult(AnEnumWithDifferentName.One)(read)
  }

  //-----------------------------------------------------------------------------------------------
}
