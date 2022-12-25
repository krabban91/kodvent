import aoc.string.AoCPart1StringTest

object Day25 extends App with AoCPart1StringTest {

  printResultPart1Test
  printResultPart1

  override def part1(strings: Seq[String]): String = {
    toSNAFU(strings.map(fromSNAFU).sum)
  }

  def toSNAFU(v: Long): String = {
    var divider = math.pow(5, 20).toLong

    while (v / divider == 0) {
      divider = divider / 5
    }
    var tests = Seq((v, ""))
    while (divider != 0) {
      tests = tests.flatMap { case (inValue, outS) => Seq("=", "-", "0", "1", "2")
        .map {
          case "=" => (inValue + (2 * divider), outS + "=")
          case "-" => (inValue + (1 * divider), outS + "-")
          case s => (inValue - (s.toLong * divider), outS + s)
        }
        .filter(kv => math.abs(kv._1) <= divider)
      }
      divider = divider / 5
    }
    tests
      .find(_._1 == 0)
      .map(_._2).get
  }

  def fromSNAFU(snafu: String): Long = {
    snafu.zipWithIndex.map { case (c, i) =>
      val v = math.pow(5, snafu.length - 1 - i)
      val out = c match {
        case '=' => -2 * v
        case '-' => -1 * v
        case _ => s"$c".toLong * v
      }
      out
    }.map(_.toLong).sum
  }
}
