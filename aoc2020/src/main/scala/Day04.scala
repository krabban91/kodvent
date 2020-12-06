import aoc.numeric.{AoCPart1Test, AoCPart2}


object Day04 extends App with AoCPart1Test with AoCPart2 {

  override def part1(strings: Seq[String]): Long = {
    groupsSeparatedByTwoNewlines(strings).map(Passport(_)).count(_.isPresent)
  }

  override def part2(strings: Seq[String]): Long = {
    groupsSeparatedByTwoNewlines(strings).map(Passport(_)).count(_.isValid)
  }

  case class Passport(byr: Option[String], iyr: Option[String],
                      eyr: Option[String], hgt: Option[String],
                      hcl: Option[String], ecl: Option[String],
                      pid: Option[String], cid: Option[String]) {

    def isPresent: Boolean = {
      byr.isDefined &&
        iyr.isDefined &&
        eyr.isDefined &&
        hgt.isDefined &&
        hcl.isDefined &&
        ecl.isDefined &&
        pid.isDefined
    }

    def isValid: Boolean = {
      isPresent &&
        validBirth &&
        validIssue &&
        validExpiration &&
        validHeight &&
        validHairColor &&
        validEyeColor &&
        validPid
    }

    def validBirth: Boolean = yearWithin(byr, 1920, 2002)

    def validIssue: Boolean = yearWithin(iyr, 2010, 2020)

    def validExpiration: Boolean = yearWithin(eyr, 2020, 2030)


    def validHeight: Boolean = {
      val h = hgt.getOrElse("0")
      if (h.contains("cm")) {
        val ih = h.replace("cm", "").toInt
        return ih >= 150 && ih <= 193
      }
      if (h.contains("in")) {
        val ih = h.replace("in", "").toInt
        return ih >= 59 && ih <= 76
      }
      false
    }

    def validHairColor: Boolean = {
      if (!hcl.get.charAt(0).equals('#')) {
        return false
      }
      val h = hcl.get.replaceFirst("#", "")
      h.length == 6 && h.count(c => (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f')) == 6
    }

    def validEyeColor: Boolean = {
      ecl.get.equals("amb") ||
        ecl.get.equals("blu") ||
        ecl.get.equals("brn") ||
        ecl.get.equals("gry") ||
        ecl.get.equals("grn") ||
        ecl.get.equals("hzl") ||
        ecl.get.equals("oth")
    }

    def validPid: Boolean = {
      pid.get.length == 9 && pid.get.count(c => (c >= '0' && c <= '9')) == 9
    }

    private def yearWithin(input: Option[String], start: Int, end: Int): Boolean = {
      val y = input.getOrElse("0").toInt
      y >= start && y <= end
    }
  }

  object Passport {
    def apply(s: String): Passport = {
      val tokens: Seq[String] = s.replace("\n", "\t").replace(" ", "\t").split("\t")
      val m = tokens.filterNot(_.isBlank)
        .map(v => v.split(":")(0) -> v.split(":")(1)).toMap
      Passport(m.get("byr"), m.get("iyr"), m.get("eyr"), m.get("hgt"), m.get("hcl"), m.get("ecl"), m.get("pid"), m.get("cid"))
    }
  }

}
