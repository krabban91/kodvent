import aoc.numeric.{AoCPart1Test, AoCPart2}


object Day04 extends App with AoCPart1Test with AoCPart2 {

  override def part1(strings: Seq[String]): Long = {
    groupsSeparatedByTwoNewlines(strings).map(Passport(_)).count(_.isPresent)
  }

  override def part2(strings: Seq[String]): Long = {
    groupsSeparatedByTwoNewlines(strings).map(Passport(_)).count(_.isValid)
  }

  case class Passport(fields: Map[String, String]) {

    def isPresent: Boolean = {
      fields.contains("byr") &&
        fields.contains("iyr") &&
        fields.contains("eyr") &&
        fields.contains("hgt") &&
        fields.contains("hcl") &&
        fields.contains("ecl") &&
        fields.contains("pid")
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

    def validBirth: Boolean = yearWithin(fields("byr"), 1920, 2002)

    def validIssue: Boolean = yearWithin(fields("iyr"), 2010, 2020)

    def validExpiration: Boolean = yearWithin(fields("eyr"), 2020, 2030)


    def validHeight: Boolean = {
      """(\d+)cm""".r.findAllIn(fields("hgt")).matchData.exists(m => m.group(1).toInt >= 150 && m.group(1).toInt <= 193) ||
        """(\d+)in""".r.findAllIn(fields("hgt")).matchData.exists(m => m.group(1).toInt >= 59 && m.group(1).toInt <= 76)
    }

    def validHairColor: Boolean = fields("hcl").matches("(#)([a-f,0-9]{6})")

    def validEyeColor: Boolean = fields("ecl").matches("amb|blu|brn|gry|grn|hzl|oth")

    def validPid: Boolean = fields("pid").matches("([0-9]{9})")

    private def yearWithin(input: String, start: Int, end: Int): Boolean = {
      val y = input.toInt
      y >= start && y <= end
    }
  }

  object Passport {
    def apply(s: String): Passport = Passport(s.split("\\s+").filterNot(_.isBlank)
      .map(v => v.split(":")(0) -> v.split(":")(1))
      .toMap)
  }

}
