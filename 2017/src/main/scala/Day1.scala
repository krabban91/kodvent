import java.util.stream.IntStream

object Day1 extends App {
  def getInput(): String = scala.io.Source.fromResource("day1.txt").getLines().next()

  def inverseCaptcha(input: String, next: Function[Int, Int]): Int = IntStream.range(0, input.length)
    .filter(i => input.charAt(i).equals(input.charAt(next.apply(i))))
    .map(i => Integer.parseInt(input.charAt(i) + ""))
    .sum()

  def part1(input: String) = inverseCaptcha(input, i => (i + 1) % input.length)
  def part2(input: String) = inverseCaptcha(input, i => (i + input.length / 2) % input.length)

  val input = getInput()
  println(part1(input))
  println(part2(input))
}

