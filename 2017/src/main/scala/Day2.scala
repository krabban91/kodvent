import java.util.stream.IntStream

object Day2 extends App {
  def getInput(): Array[Array[Int]] = scala.io.Source.fromResource("day2.txt").getLines()
    .map(s => s.split("\t").map(i => Integer.parseInt(i)))
    .toArray

  def checkSum(input: Array[Array[Int]], function: Function[Array[Int], Int]): Int = input.map(function.apply).sum

  def part1(input: Array[Array[Int]]) = checkSum(input, a => a.max - a.min)

  def part2(input: Array[Array[Int]]) = checkSum(input, a => IntStream.range(0, a.length)
    .map(i => IntStream.range(0, a.length)
      .filter(j => j != i && a(i) > a(j) && a(i) % a(j) == 0)
      .map(j => a(i) / a(j))
      .findAny().orElse(0)).sum())

  val input = getInput()
  println(s"Starting ${this.getClass.getCanonicalName.stripSuffix("$")}")
  println(s"Part 1 is ${part1(input)}")
  println(s"Part 2 is ${part2(input)}")
}

