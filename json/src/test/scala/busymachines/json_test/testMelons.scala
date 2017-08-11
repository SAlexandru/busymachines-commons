package busymachines.json_test

/**
  *
  * @author Lorand Szakacs, lsz@lorandszakacs.com, lorand.szakacs@busymachines.com
  * @since 09 Aug 2017
  *
  */

private[json_test] object AnEnum extends Enumeration {
  type AnEnum = Value
  val One = Value
}

private[json_test] object AnEnumWithDifferentName extends Enumeration {
  type AnEnumWithDifferentName = Value
  val One = Value("o ne")
}

private[json_test] case class AnarchistMelon(
  noGods: Boolean,
  noMasters: Boolean,
  noSuperTypes: Boolean
)

private[json_test] sealed trait Melon {
  def weight: Int
}

private[json_test] case class WinterMelon(
  fuzzy: Boolean,
  weight: Int
) extends Melon

private[json_test] case class WaterMelon(
  seeds: Boolean,
  weight: Int
) extends Melon

private[json_test] case object SmallMelon extends Melon {
  override val weight: Int = 0
}

private[json_test] sealed trait Taste

private[json_test] case object SweetTaste extends Taste

//I ran out of ideas, ok? I'll think of better test data.
private[json_test] case object SourTaste extends Taste

private[json_test] sealed trait TastyMelon extends Melon {
  def tastes: Seq[Taste]
}

private[json_test] case class SquareMelon(
  weight: Int,
  tastes: Seq[Taste]
) extends TastyMelon


