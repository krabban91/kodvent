import aoc.string.AoCPart1StringTest

object Day25 extends App with AoCPart1StringTest {

  override def part1(strings: Seq[String]): String = toSNAFU(strings.map(fromSNAFU).sum)

  def toSNAFU(v: Long): String = (0 to 20)
    .foldRight(Seq((v, ""))) { case (i, l) =>
      l.flatMap { case e@(inV, outS) =>
        val divider = math.pow(5, i).toLong
        if (outS == "" && inV / divider == 0) Seq(e)
        else conversions
          .map { case (c, m) => (inV - (m * divider), outS + c) }
          .filter(kv => math.abs(kv._1) <= divider)
      }
    }
    .find(_._1 == 0)
    .get._2

  def fromSNAFU(snafu: String): Long = snafu.zipWithIndex.map { case (c, i) =>
    val v = math.pow(5, snafu.length - 1 - i)
    c match {
      case '=' => -2 * v
      case '-' => -1 * v
      case _ => s"$c".toLong * v
    }
  }.map(_.toLong).sum

  private def conversions = Seq(("=", -2), ("-", -1), ("0", 0), ("1", 1), ("2", 2))
}
