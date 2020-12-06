import aoc.numeric.{AoCPart1Test, AoCPart2Test}


object Day06 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = groupsSeparatedByTwoNewlines(strings)
    .map(_.replace("\n", ""))
    .map(_.toSeq.distinct.size)
    .sum

  override def part2(strings: Seq[String]): Long = groupsSeparatedByTwoNewlines(strings)
    .map(_.split("\n").filterNot(_.isBlank))
    .map(respondents => respondents
      .flatMap(_.toSeq).toSeq.distinct
      .map(q => respondents.count(_.contains(q)))
      .count(_ == respondents.length))
    .sum
}
